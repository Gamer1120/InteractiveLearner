package classifier;

import model.Document;
import utils.MutableInt;
import utils.Utils;

import java.io.Serializable;
import java.util.*;

public abstract class NaiveBayesClassifierBase implements Classifier, Serializable {
    /* Used for training */
    // List of documents used for training
    protected final List<Document> trainingSet;

    /* Used during training */
    // Map of classes and how many documents they have
    protected Map<String, MutableInt> categories;
    // Map of words with a map of categories and how many times the word occurred in that category
    protected Map<String, Map<String, MutableInt>> vocabulary;
    // Total amount of observed documents
    private int documents;
    // Total amount of observed words
    private int words;

    /* Generated after training */
    // Map of categories and their prior probabilities
    protected Map<String, Double> priorProb;
    // Map of words with a map of categories and the conditional probability of the word occurring in that category
    protected Map<String, Map<String, Double>> condProb;

    /* Used in feature selection */
    // Which feature selection methods to use
    protected boolean stopWords, wordCount, chiSquare;
    // Minimum and maximum percentage of the total amount of observed words a word may occur when using word count feature selection
    protected double minPercent, maxPercent;
    // Chi-square critical value when using chi-square feature selection
    protected double criticalValue;

    /**
     * A base class for a Naive Bayes implementation of the classifier.
     */
    public NaiveBayesClassifierBase() {
        trainingSet = new ArrayList<>();
    }

    /**
     * @inheritDoc
     */
    @Override
    public abstract String classify(String text);

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
        // Add all documents to the categories and add all words to the vocabulary
        add();
        // Apply feature selection to the vocabulary
        featureSelection();
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
        words = 0;
    }

    /**
     * Releases the variables used during training.
     */
    private void release() {
        categories = null;
        vocabulary = null;
    }

    protected abstract void add();

    /**
     * Adds one to the total document count
     * and one to the document count of the specified category.
     *
     * @param category - the category of the document
     */
    protected void addDocument(String category) {
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
    protected void addWord(String word, String category, int count) {
        // Add one to the total word count
        words++;
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
     */
    private void featureSelection() {
        if (stopWords) {
            stopWords();
        }
        if (wordCount) {
            wordCount();
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
     * Remove rare and common words.
     */
    private void wordCount() {
        // Calculate the minimum and maximum amount of times a word may occur
        int minCount = (int) Math.floor(minPercent * words), maxCount = (int) Math.ceil(maxPercent * words);
        // For every word in the vocabulary
        for (Iterator<Map.Entry<String, Map<String, MutableInt>>> iterator = vocabulary.entrySet().iterator(); iterator.hasNext(); ) {
            // Total amount of times the word occurred
            int count = 0;
            // For every count of how many times the word occurred in a category
            for (MutableInt categoryCount : iterator.next().getValue().values()) {
                // Add it to the total amount
                count += categoryCount.intValue();
            }
            // If the word occurs less times than the minimum value or more times than the maximum value
            if (count < minCount || count > maxCount) {
                // Remove it from the vocabulary
                iterator.remove();
            }
        }
    }

    /**
     * Remove all words with a chi-square value below the critical value from the vocabulary.
     */
    private void chiSquare() {
        // For every word in the vocabulary
        for (Iterator<Map.Entry<String, Map<String, MutableInt>>> iterator = vocabulary.entrySet().iterator(); iterator.hasNext(); ) {
            // Get the map of categories and how many times the word occurred in that category
            Map<String, MutableInt> wordCategoryCount = iterator.next().getValue();
            int N0dot, N1dot, N00, N01, N10, N11;
            double maxScore = 0;
            // Count the number of documents that have the word
            N1dot = 0;
            for (MutableInt categoryCount : wordCategoryCount.values()) {
                N1dot += categoryCount.intValue();
            }
            // Calculate the number of documents that don't have the word
            N0dot = documents - N1dot;
            // For every category
            for (Map.Entry<String, MutableInt> entry : wordCategoryCount.entrySet()) {
                String category = entry.getKey();
                // Number of documents that have the word and belong on the category
                N11 = entry.getValue().intValue();
                // Number of documents that don't have the word but belong to the category
                N01 = categories.get(category).intValue() - N11;
                // Number of documents that don't have the word and don't belong to the category
                N00 = N0dot - N01;
                // Number of documents that have the word and don't belong to the category
                N10 = N1dot - N11;
                // Calculate the chi-square score
                double chiSquareScore = (documents * Math.pow(N11 * N00 - N10 * N01, 2)) / ((N11 + N01) * (N11 + N10) * (N10 + N00) * (N01 + N00));
                // Keep track of the highest score a word got for a category
                if (chiSquareScore > maxScore) {
                    maxScore = chiSquareScore;
                }
            }
            // If the highest score is lower than the critical value
            if (maxScore < criticalValue) {
                // Remove the word
                iterator.remove();
            }
        }
    }

    /**
     * Calculate all the prior and conditional probabilities
     */
    private void calculate() {
        // Initialize the maps
        priorProb = new HashMap<>(categories.size());
        condProb = new HashMap<>(vocabulary.size());
        // For every category
        categories.forEach(((category, count) -> {
            // Calculate the prior probability
            calculatePriorProb(category, count.intValue());
            // Calculate the conditional probability for all words in the vocabulary
            calculateCondProb(category);
        }));
    }

    /**
     * Calculate the prior probability for the given category.
     *
     * @param category - the category
     * @param count    - the amount of documents the category has
     */
    private void calculatePriorProb(String category, int count) {
        // Calculate the prior probability
        double prob = Math.log((double) count / (double) documents);
        // Add it to the map
        priorProb.put(category, prob);
    }

    /**
     * Calculate the conditional probability for the given category for all words in the vocabulary.
     *
     * @param category - the category
     */
    protected abstract void calculateCondProb(String category);

    /**
     * Get the known categories.
     * The classifier has to be trained before it knows any categories.
     *
     * @return a set of all the known categories, or an empty set if the classifier hasn't been trained yet
     */
    @Override
    public Set<String> getCategories() {
        // If the classifier has been trained
        if (priorProb != null) {
            // Return all known categories
            return priorProb.keySet();
        } else {
            // Return an empty set
            return Collections.emptySet();
        }
    }

    /**
     * Get the training set
     *
     * @return a list of all documents in the training set
     */
    @Override
    public List<Document> getTrainingSet() {
        return trainingSet;
    }
}
