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
    protected int documents;

    /* Generated after training */
    // Map of categories and their prior probabilities
    protected Map<String, Double> priorProb;
    // Map of words with a map of categories and the conditional probability of the word occurring in that category
    protected Map<String, Map<String, Double>> condProb;

    /* Used in feature selection */
    // Which feature selection methods to use
    protected boolean stopWords, wordCount, chiSquare;
    // Minimum an maximum amount of times a word may occur
    protected int minCount, maxCount;
    // Chi-square critical value
    protected double criticalValue;

    /**
     * A base class for a Naive Bayes implementation of the classifier.
     */
    public NaiveBayesClassifierBase() {
        trainingSet = new ArrayList<>();
        criticalValue = 10.83;
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
    protected void init() {
        categories = new HashMap<>();
        vocabulary = new HashMap<>();
        documents = 0;
    }

    /**
     * Releases the variables used during training.
     */
    protected void release() {
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
    protected void featureSelection() {
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
    protected void stopWords() {
        // For every stop word
        for (String word : Utils.fileToString("db/common-english-words.txt").split(",")) {
            // Remove it form the vocabulary
            vocabulary.remove(word);
        }
    }

    /**
     * Remove rare and common words.
     */
    protected void wordCount() {
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
    protected void chiSquare() {
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
    protected void calculate() {
        priorProb = new HashMap<>(categories.size());
        condProb = new HashMap<>(vocabulary.size());
        categories.forEach(((category, count) -> {
            calculatePriorProb(category, count.intValue());
            calculateCondProb(category);
        }));
    }

    protected void calculatePriorProb(String category, int count) {
        double prob = Math.log((double) count / (double) documents);
        priorProb.put(category, prob);
    }

    protected abstract void calculateCondProb(String category);

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
