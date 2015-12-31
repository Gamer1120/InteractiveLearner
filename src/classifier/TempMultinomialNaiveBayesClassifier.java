package classifier;

import fileparser.FileUtils;
import utils.MutableDouble;
import utils.MutableInt;

import java.io.Serializable;
import java.util.*;

public class TempMultinomialNaiveBayesClassifier implements Classifier, Serializable {
    // Feature Selection
    private final TempFeatureSelection featureSelection;
    // Map of classes and their values
    private final Map<String, ClassValues> classes;
    // Total amount of documents
    private int documentCount;

    /**
     * A Multinomial Naive Bayes implementation of the classifier.
     */
    public TempMultinomialNaiveBayesClassifier() {
        this(new TempFeatureSelection());
    }

    public TempMultinomialNaiveBayesClassifier(TempFeatureSelection featureSelection) {
        this.featureSelection = featureSelection;
        documentCount = 0;
        classes = new HashMap<>();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String classify(String text) {
        Set<String> vocabulary = getVocabulary();
        // Map of classes and their calculated scores
        Map<String, MutableDouble> scores = new HashMap<>();
        // Calculate and add the prior probability to the score for each class
        classes.forEach((className, classValues) -> {
            double priorProb = Math.log((double) classValues.getDocumentCount() / (double) documentCount);
            scores.put(className, new MutableDouble(priorProb));
        });
        // Calculate the score each known word adds for each class
        for (String word : FileUtils.tokenize(text)) {
            // Check if the word is known
            if (vocabulary.contains(word)) {
                // Calculate and add the conditional probability the word gives for each class with laplace smoothing
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
    public void add(TempDocument document) {
        // Add one to the total document count
        ++documentCount;
        // Get the values for the classification of the document
        ClassValues classValues = classes.get(document.getClassification());
        // If the values don't exist, create a new classification with the specified name and a new set of values
        if (classValues == null) {
            classValues = addClass(document.getClassification());
        }
        // Add one to the document count of the classification
        classValues.addDocument(document.getText());
    }

    /**
     * @inheritDoc
     */
    @Override
    public void addAll(Collection<TempDocument> documents) {
        // Add all documents
        documents.forEach(this::add);
    }

    public ClassValues addClass(String className) {
        ClassValues classValues = new ClassValues();
        classes.put(className, classValues);
        return classValues;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void delete(String className) {
        classes.remove(className);
    }

    public Set<String> getClasses() {
        return classes.keySet();
    }

    public Set<String> getVocabulary() {
        Set<String> vocabulary = new HashSet<>();
        classes.forEach((className, classValues) -> vocabulary.addAll(classValues.getIndividualWordCount().keySet()));
        //TODO feature selection
        return vocabulary;
    }

    public static class ClassValues implements Serializable {
        // Map of words and the number of times they occur
        private final Map<String, MutableInt> individualWordCount;
        // Amount of documents
        private int documentCount;
        // Amount of words
        private int totalWordCount;

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
        public void addDocument(String text) {
            documentCount++;
            // For each word in the text
            for (String word : FileUtils.tokenize(text)) {
                // Add the word to the values
                addWord(word);
            }
        }

        /**
         * Adds a word to the total and individual word count.
         *
         * @param word - the word to be added
         */
        public void addWord(String word) {
            // Add one to the total word count
            totalWordCount++;
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

        public void removeDocument(String text) {
            documentCount--;
            // For each word in the text
            for (String word : FileUtils.tokenize(text)) {
                // Remove the word from the values
                removeWord(word);
            }
        }

        public void removeWord(String word) {
            // Remove one from the total word count
            totalWordCount--;
            // Get the count for the specified word
            MutableInt count = individualWordCount.get(word);
            if (count != null) {
                if (count.intValue() == 1) {
                    individualWordCount.remove(word);
                } else {
                    count.add(-1);
                }
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
         * Get the count of all individual words.
         *
         * @return a map of words and their count
         */
        public Map<String, MutableInt> getIndividualWordCount() {
            return individualWordCount;
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
