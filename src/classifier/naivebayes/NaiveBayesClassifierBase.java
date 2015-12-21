package classifier.naivebayes;

import classifier.Classifier;
import classifier.Document;
import fileparser.FileUtils;
import utils.MutableInt;

import java.util.*;

public abstract class NaiveBayesClassifierBase implements Classifier {
    // Total amount of documents
    protected int documentCount;
    // Map of classes and their values
    protected Map<String, ClassValues> classes;
    // Set of all words
    protected Set<String> vocabulary;

    /**
     * A base class for the Naive Bayes implementation of the classifier.
     */
    public NaiveBayesClassifierBase() {
        documentCount = 0;
        classes = new HashMap<>();
        vocabulary = new HashSet<>();
    }

    /**
     * The classify method has to be implemented by the Naive Bayes classifier that extends this base.
     *
     * @inheritDoc
     */
    @Override
    public abstract String classify(String text);

    /**
     * @inheritDoc
     */
    @Override
    public void add(Document document) {
        // Add one to the total document count
        ++documentCount;
        // Get the values for the classification of the document
        ClassValues classValues = classes.get(document.getClassification());
        // If the values don't exist, create a new classification with the specified name and a new set of values
        if (classValues == null) {
            classValues = new ClassValues();
            classes.put(document.getClassification(), new ClassValues());
        }
        // Add one to the document count of the classification
        classValues.addDocument();
        // For each word in the text
        for (String word : FileUtils.tokenize(document.getText())) {
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

    protected static class ClassValues {
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
