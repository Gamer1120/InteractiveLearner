package classifier;

import fileparser.FileUtils;
import utils.MutableDouble;
import utils.MutableInt;

import java.util.*;

public class MultinomialNaiveBayesClassifier {
    private static final double CRITICAL_VALUE = 10.83;

    private List<Document> trainingSet;
    private Map<String, MutableInt> categories;
    private Map<String, Map<String, MutableInt>> vocabulary;
    private Map<String, Double> priorProb;
    private Map<String, Map<String, Double>> condProb;
    private int documents;

    public MultinomialNaiveBayesClassifier() {
        trainingSet = new ArrayList<>();
    }

    public void add(Document document) {
        trainingSet.add(document);
    }

    public void addAll(Collection<Document> documents) {
        documents.forEach(this::add);
    }

    public void train() {
        init();
        trainingSet.forEach(document -> {
            addDocument(document.getCategory());
            document.getTokens().forEach((word, count) -> addWord(word, document.getCategory(), count.intValue()));
        });
        featureSelection(false, true);
        calculate();
        release();
    }

    public String classify(String text) {
        Map<String, MutableDouble> scores = new HashMap<>();
        priorProb.forEach((category, score) -> scores.put(category, new MutableDouble(score)));
        for (String word : FileUtils.tokenize(text)) {
            Map<String, Double> categoryCondProb = condProb.get(word);
            if (categoryCondProb != null) {
                categoryCondProb.forEach((category, score) -> scores.get(category).add(score));
            }
        }
        // Return the class with the highest score
        return scores.entrySet().stream()
                .max((entry1, entry2) -> entry1.getValue().doubleValue() > entry2.getValue().doubleValue() ? 1 : -1)
                .get()
                .getKey();
    }

    private void init() {
        vocabulary = new HashMap<>();
        categories = new HashMap<>();
        documents = 0;
    }

    private void release() {
        categories = null;
        vocabulary = null;
    }

    private void addDocument(String category) {
        documents++;
        MutableInt count = categories.get(category);
        if (count == null) {
            categories.put(category, new MutableInt(1));
        } else {
            count.add(1);
        }
    }

    private void addWord(String word, String category, int count) {
        Map<String, MutableInt> wordCategoryCount = vocabulary.get(word);
        if (wordCategoryCount == null) {
            wordCategoryCount = new HashMap<>();
            vocabulary.put(word, wordCategoryCount);
        }
        MutableInt categoryCount = wordCategoryCount.get(category);
        if (categoryCount == null) {
            wordCategoryCount.put(category, new MutableInt(count));
        } else {
            categoryCount.add(count);
        }
    }

    private void featureSelection(boolean stopWords, boolean chiSquare) {
        if (stopWords) {
            stopWords();
        }
        if (chiSquare) {
            chiSquare();
        }
    }

    private void stopWords() {
        for (String word : FileUtils.fileToString("db/common-english-words.txt").split(",")) {
            vocabulary.remove(word);
        }
    }

    private void chiSquare() {
        for (Iterator<Map.Entry<String, Map<String, MutableInt>>> iterator = vocabulary.entrySet().iterator(); iterator.hasNext(); ) {
            Map<String, MutableInt> wordCategoryCount = iterator.next().getValue();
            int N0dot, N1dot, N00, N01, N10, N11;
            double maxScore = 0;
            N1dot = 0;
            for (MutableInt categoryCount : wordCategoryCount.values()) {
                N1dot += categoryCount.intValue();
            }
            N0dot = documents - N1dot;
            for (Map.Entry<String, MutableInt> entry : wordCategoryCount.entrySet()) {
                String category = entry.getKey();
                N11 = entry.getValue().intValue();
                N01 = categories.get(category).intValue() - N11;
                N00 = N0dot - N01;
                N10 = N1dot - N11;
                double chiSquareScore = (documents * Math.pow(N11 * N00 - N10 * N01, 2)) / ((N11 + N01) * (N11 + N10) * (N10 + N00) * (N01 + N00));
                if (chiSquareScore > maxScore) {
                    maxScore = chiSquareScore;
                }
            }
            if (maxScore < CRITICAL_VALUE) {
                iterator.remove();
            }
        }
    }

    private void calculate() {
        priorProb = new HashMap<>(categories.size());
        condProb = new HashMap<>(vocabulary.size());
        categories.forEach(((category, count) -> {
            calculatePriorProb(category, count.intValue());
            int words = countWords(category);
            calculateCondProb(category, words);
        }));
    }

    private void calculatePriorProb(String category, int count) {
        double prob = Math.log((double) count / (double) documents);
        priorProb.put(category, prob);
    }

    private int countWords(String category) {
        int words = 0;
        for (Map<String, MutableInt> categoryCount : vocabulary.values()) {
            MutableInt count = categoryCount.get(category);
            if (count != null) {
                words += count.intValue();
            }
        }
        return words;
    }

    private void calculateCondProb(String category, int words) {
        vocabulary.forEach((word, wordCategoryCount) -> {
            Map<String, Double> categoryCondProb = condProb.get(word);
            if (categoryCondProb == null) {
                categoryCondProb = new HashMap<>(priorProb.size());
                condProb.put(word, categoryCondProb);
            }
            MutableInt categoryCount = wordCategoryCount.get(category);
            int count = categoryCount == null ? 0 : categoryCount.intValue();
            double prob = Math.log((double) (count + 1) / (double) (words + vocabulary.size()));
            categoryCondProb.put(category, prob);
        });
    }

    public List<Document> getTrainingSet() {
        return trainingSet;
    }

    public Set<String> getCategories() {
        if (priorProb != null) {
            return priorProb.keySet();
        } else {
            return null;
        }
    }
}
