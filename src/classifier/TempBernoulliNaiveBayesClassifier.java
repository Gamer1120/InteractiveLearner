package classifier;

import fileparser.FileUtils;
import utils.MutableDouble;
import utils.MutableInt;

import java.io.Serializable;
import java.util.*;

public class TempBernoulliNaiveBayesClassifier implements Classifier, Serializable {
    //FIXME
    // Feature Selection
    private final TempFeatureSelection featureSelection;
    // Map of classes and their values
    private final Map<String, ClassValues> classes;
    // Set of all words
    private final Set<String> vocabulary;
    // Total amount of documents
    private int documentCount;

    /**
     * A Multinomial Naive Bayes implementation of the classifier.
     */
    public TempBernoulliNaiveBayesClassifier() {
        this(new TempFeatureSelection());
    }

    public TempBernoulliNaiveBayesClassifier(TempFeatureSelection featureSelection) {
        this.featureSelection = featureSelection;
        documentCount = 0;
        classes = new HashMap<>();
        vocabulary = new HashSet<>();
    }

    /**
     * @inheritDoc
     */
    @Override
    public String classify(String text) {
        // Map of classes and their calculated scores
        Map<String, MutableDouble> scores = new HashMap<>();
        // Calculate and add the prior probability to the score for each class
        classes.forEach((className, classValues) -> {
            double priorProb = Math.log((double) classValues.getDocumentCount() / (double) documentCount);
            scores.put(className, new MutableDouble(priorProb));
        });
        Set<String> words = textToSet(text);
        // Calculate the score each known word adds for each class
        for (String word : vocabulary) {
            // Calculate and add the conditional probability the word gives for each class with laplace smoothing
            if (words.contains(word)) {
                classes.forEach((className, classValues) -> {
                    MutableDouble score = scores.get(className);
                    double condProb = Math.log((double) (classValues.getIndividualWordCount(word) + 1) / (double) (classValues.getDocumentCount() + classes.size()));
                    score.add(condProb);
                });
            } else {
                classes.forEach((className, classValues) -> {
                    MutableDouble score = scores.get(className);
                    double condProb = Math.log(1d - ((double) (classValues.getIndividualWordCount(word) + 1) / (double) (classValues.getDocumentCount() + classes.size())));
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
            classValues = new ClassValues();
            classes.put(document.getClassification(), classValues);
        }
        // Add one to the document count of the classification
        classValues.addDocument();
        // For each unique word in the text
        Set<String> words = textToSet(document.getText());
        for (String word : words) {
            // Add the word to the vocabulary with feature selection
            if (featureSelection.select(word)) {
                vocabulary.add(word);
            }
            // Add the word to the class
            classValues.addWord(word);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void addAll(Collection<TempDocument> documents) {
        // Add all documents
        documents.forEach(this::add);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void delete(String className) {
        classes.remove(className);
    }

    private Set<String> textToSet(String text) {
        String[] tokens = FileUtils.tokenize(text);
        HashSet<String> set = new HashSet<>(tokens.length);
        Collections.addAll(set, tokens);
        return set;
    }

    private static class ClassValues implements Serializable {
        // Map of words and the number of times they occur
        private final Map<String, MutableInt> individualWordCount;
        // Amount of documents
        private int documentCount;

        /**
         * The values belonging to a class.
         */
        public ClassValues() {
            documentCount = 0;
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
            //FIXME?
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