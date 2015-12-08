package classifier;

import utils.MutableDouble;
import utils.MutableInt;

import java.util.*;

public class MultinomialNaiveBayesClassifier implements Classifier {
    // Total amount of documents
    private int documentCount;
    // Map of classes and their values
    private Map<String, ClassValues> classes;
    // All unique words
    private Set<String> vocabulary;

    public MultinomialNaiveBayesClassifier() {
        documentCount = 0;
        classes = new HashMap<>();
        vocabulary = new HashSet<>();
    }

    @Override
    public String classify(String... words) {
        Map<String, MutableDouble> score = new HashMap<>();
        classes.forEach((k, v) -> {
            double prior = Math.log((double) v.getDocumentCount() / (double) documentCount);
            score.put(k, new MutableDouble(prior));
        });
        for (String word : words) {
            if (vocabulary.contains(word)) {
                classes.forEach((k, v) -> {
                    MutableDouble value = score.get(k);
                    double condProb = Math.log((double) (v.getIndividualWordCount(word) + 1) / (double) (v.getTotalWordCount() + vocabulary.size()));
                    value.add(condProb);
                });
            }
        }
        return score.entrySet().stream().max((entry1, entry2) -> entry1.getValue().toDouble() > entry2.getValue().toDouble() ? 1 : -1).get().getKey();
    }

    @Override
    public void add(Document document) {
        ++documentCount;
        ClassValues classValues = classes.get(document.classification);
        if (classValues == null) {
            classValues = new ClassValues();
            classes.put(document.classification, classValues);
        }
        classValues.addDocument();
        for (String word : document.text) {
            vocabulary.add(word);
            classValues.addWord(word);
        }
    }

    @Override
    public void addAll(Collection<Document> documents) {
        documents.forEach(this::add);
    }

    private static class ClassValues {
        // Amount of documents
        private int documentCount;
        // Amount of words
        private int totalWordCount;
        // Number of times a word occurs
        private Map<String, MutableInt> individualWordCount;

        public ClassValues() {
            documentCount = 0;
            totalWordCount = 0;
            individualWordCount = new HashMap<>();
        }

        public void addDocument() {
            ++documentCount;
        }

        public void addWord(String word) {
            ++totalWordCount;
            MutableInt count = individualWordCount.get(word);
            if (count == null) {
                individualWordCount.put(word, new MutableInt(1));
            } else {
                count.add(1);
            }
        }

        public int getDocumentCount() {
            return documentCount;
        }

        public int getTotalWordCount() {
            return totalWordCount;
        }

        public int getIndividualWordCount(String word) {
            MutableInt count = individualWordCount.get(word);
            return count == null ? 0 : count.toInt();
        }
    }
}
