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

    /**
     * A Multinomial Naive Bayes implementation of the classifier.
     */
    public MultinomialNaiveBayesClassifier() {
        documentCount = 0;
        classes = new HashMap<>();
        vocabulary = new HashSet<>();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String classify(String... words) {
        // Map of classes and their calculated scores
        Map<String, MutableDouble> scores = new HashMap<>();
        // Calculate and add the initial priority to the score for each class
        classes.forEach((className, classValues) -> {
            double prior = Math.log((double) classValues.getDocumentCount() / (double) documentCount);
            scores.put(className, new MutableDouble(prior));
        });
        // Calculate the score each known word adds for each class
        for (String word : words) {
            // Check if the word is known
            if (vocabulary.contains(word)) {
                // Calculate and add the score the word gives for each class using laplace smoothing
                classes.forEach((className, classValues) -> {
                    MutableDouble score = scores.get(className);
                    double condProb = Math.log((double) (classValues.getIndividualWordCount(word) + 1) / (double) (classValues.getTotalWordCount() + vocabulary.size()));
                    score.add(condProb);
                });
            }
        }
        // Return the class with the highest score
        return scores.entrySet().stream().max((entry1, entry2) -> entry1.getValue().doubleValue() > entry2.getValue().doubleValue() ? 1 : -1).get().getKey();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void add(Document document) {
        // Add one to the total document count
        ++documentCount;
        // Get the values for the classification of the document
        ClassValues classValues = classes.get(document.classification);
        // If the values don't exist, create a new classification with the specified name and a new set of values
        if (classValues == null) {
            classValues = new ClassValues();
            classes.put(document.classification, classValues);
        }
        // Add one to the document count of the classification
        classValues.addDocument();
        // For each word in the text
        for (String word : document.text) {
            // Add the word to the vocabulary
            vocabulary.add(word);
            // Add the word to the class
            classValues.addWord(word);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void addAll(Collection<Document> documents) {
        // Add all documents
        documents.forEach(this::add);
    }

    private static class ClassValues {
        // Amount of documents
        private int documentCount;
        // Amount of words
        private int totalWordCount;
        // Map of words and the number of times they occur
        private Map<String, MutableInt> individualWordCount;

        /**
         * The values belonging to a class.
         */
        public ClassValues() {
            documentCount = 0;
            totalWordCount = 0;
            individualWordCount = new HashMap<>();
        }

        /**
         * Adds a document to the document count.
         */
        public void addDocument() {
            ++documentCount;
        }

        /**
         * Adds a word to the total and individual word count.
         *
         * @param word - the word to be added
         */
        public void addWord(String word) {
            // Add one to the total word count
            ++totalWordCount;
            // Get the count for the specified word
            MutableInt count = individualWordCount.get(word);
            if (count == null) {
                // If the word doesn't exist, create a new entry for this word with count one
                individualWordCount.put(word, new MutableInt(1));
            } else {
                // Add one to the count
                count.add(1);
            }
        }

        /**
         * Get the document count.
         *
         * @return the document count
         */
        public int getDocumentCount() {
            return documentCount;
        }

        /**
         * Get the total word count.
         *
         * @return the total word count
         */
        public int getTotalWordCount() {
            return totalWordCount;
        }

        /**
         * Gets the count the specified word.
         *
         * @param word - the specified word
         * @return the count of the specified word
         */
        public int getIndividualWordCount(String word) {
            // Get the count for the specified word
            MutableInt count = individualWordCount.get(word);
            // Return the count of the word or zero if there is no entry for the word
            return count == null ? 0 : count.intValue();
        }
    }
}