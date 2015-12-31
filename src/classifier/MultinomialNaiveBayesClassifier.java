package classifier;

import model.Document;
import utils.MutableDouble;
import utils.MutableInt;
import utils.Utils;

import java.io.Serializable;
import java.util.*;

public class MultinomialNaiveBayesClassifier implements Classifier, Serializable {
    // Chi-square critical value for p = 0.001
    private static final double CRITICAL_VALUE = 10.83;

    /* Used for training */
    // List of documents used for training
    private final List<Document> trainingSet;

    /* Used during training */
    // Map of classes and how many documents they have
    private Map<String, MutableInt> categories;
    // Map of words with a map of categories and how many times the word occurred in that category
    private Map<String, Map<String, MutableInt>> vocabulary;
    // Total amount of observed documents
    private int documents;

    /* Generated after training */
    // Map of categories and their prior probabilities
    private Map<String, Double> priorProb;
    // Map of words with a map of categories and the conditional probability of the word occurring in that category
    private Map<String, Map<String, Double>> condProb;

    /**
     * A Multinomial Naive Bayes implementation of the classifier.
     */
    public MultinomialNaiveBayesClassifier() {
        trainingSet = new ArrayList<>();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String classify(String text) {
        // Map of categories and their calculated scores
        Map<String, MutableDouble> scores = new HashMap<>();
        // Use the prior probability as the initial score for each category
        priorProb.forEach((category, score) -> scores.put(category, new MutableDouble(score)));
        // For every word in the text
        for (String word : Utils.tokenize(text)) {
            // Get the map of categories and the conditional probability of the word occurring in that category
            Map<String, Double> categoryCondProb = condProb.get(word);
            // If the word is in the vocabulary
            if (categoryCondProb != null) {
                // Add the conditional probability of the word to the score for every category
                categoryCondProb.forEach((category, score) -> scores.get(category).add(score));
            }
        }
        // Return the category with the highest score
        return scores.entrySet().stream()
                .max((entry1, entry2) -> entry1.getValue().doubleValue() > entry2.getValue().doubleValue() ? 1 : -1)
                .get()
                .getKey();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void add(Document document) {
        // Add the document to the training set
        trainingSet.add(document);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void addAll(Collection<Document> documents) {
        // Add all documents to the training set
        trainingSet.addAll(documents);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void train() {
        // Initialize the variables used during training
        init();
        // For every document in the training set
        trainingSet.forEach(document -> {
            // Add the document to the category
            addDocument(document.getCategory());
            // Add all the words of the document to the vocabulary
            document.getTokens().forEach((word, count) -> addWord(word, document.getCategory(), count.intValue()));
        });
        // Apply feature selection to the vocabulary
        featureSelection(true, true);
        // Calculate the prior and the conditional probabilities
        calculate();
        // Release the variables used during training
        release();
    }

    /**
     * Initializes the variables used during training.
     */
    private void init() {
        categories = new HashMap<>();
        vocabulary = new HashMap<>();
        documents = 0;
    }

    /**
     * Releases the variables used during training.
     */
    private void release() {
        categories = null;
        vocabulary = null;
    }

    /**
     * Adds one to the total document count
     * and one to the document count of the specified category.
     *
     * @param category - the category of the document
     */
    private void addDocument(String category) {
        // Add one to the total document count
        documents++;
        // Get the document count of the category
        MutableInt count = categories.get(category);
        // If the category doesn't exist
        if (count == null) {
            // Add the category with a document count of one
            categories.put(category, new MutableInt(1));
        } else {
            // Add one to the document count of the category
            count.add(1);
        }
    }

    /**
     * Adds the word to the vocabulary.
     *
     * @param word     - the word
     * @param category - the category the word belongs to
     * @param count    - the amount of times the word occurred
     */
    private void addWord(String word, String category, int count) {
        // Get the map of categories and how many times the word occurred in that category
        Map<String, MutableInt> wordCategoryCount = vocabulary.get(word);
        // If the word doesn't exists
        if (wordCategoryCount == null) {
            // Add the word with an empty map
            wordCategoryCount = new HashMap<>();
            vocabulary.put(word, wordCategoryCount);
        }
        // Get how many times the word occurred in the category
        MutableInt categoryCount = wordCategoryCount.get(category);
        // If the category doesn't exist
        if (categoryCount == null) {
            // Add the category with the amount of times the word occurred
            wordCategoryCount.put(category, new MutableInt(count));
        } else {
            // Add the amount of times the word occurred to the count
            categoryCount.add(count);
        }
    }

    /**
     * Apply the specified feature selection methods.
     *
     * @param stopWords - apply stopwords if true
     * @param chiSquare - apply chi-square if true
     */
    private void featureSelection(boolean stopWords, boolean chiSquare) {
        if (stopWords) {
            stopWords();
        }
        if (chiSquare) {
            chiSquare();
        }
    }

    /**
     * Remove all stop words from the vocabulary.
     */
    private void stopWords() {
        // For every stop word
        for (String word : Utils.fileToString("db/common-english-words.txt").split(",")) {
            // Remove it form the vocabulary
            vocabulary.remove(word);
        }
    }

    /**
     * Remove all words with a chi-square value below the critical value from the vocabulary.
     */
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

    @Override
    public Set<String> getCategories() {
        if (priorProb != null) {
            return priorProb.keySet();
        } else {
            return null;
        }
    }

    @Override
    public List<Document> getTrainingSet() {
        return trainingSet;
    }
}
