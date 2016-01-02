package classifier;

import utils.MutableDouble;
import utils.MutableInt;
import utils.Utils;

import java.util.*;

public class BinomialNaiveBayesClassifier extends NaiveBayesClassifierBase {

    /**
     * A Binomial implementation of the Naive Bayes base.
     */
    public BinomialNaiveBayesClassifier() {
        super();
        stopWords = true;
        wordCount = true;
        chiSquare = false;
        minCount = 4;
        maxCount = 100;
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
        // A set of all unique words in the text
        Set<String> words = new HashSet<>();
        // Add all words in the text to the set
        Collections.addAll(words, Utils.tokenize(text));
        // For every known word and for every category
        condProb.forEach((word, categoryCondProb) -> categoryCondProb.forEach((category, score) -> {
            // If the word is in the text
            if (words.contains(word)) {
                // Add the score to the category
                scores.get(category).add(Math.log(score));
            } else {
                // Add one minus the score to the category
                scores.get(category).add(Math.log(1d - score));
            }
        }));
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
    protected void add() {
        // For every document in the training set
        trainingSet.forEach(document -> {
            // Add the document to the category
            addDocument(document.getCategory());
            // Add all the words of the document to the vocabulary
            document.getTokens().keySet().forEach(word -> addWord(word, document.getCategory(), 1));
        });
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void calculateCondProb(String category) {
        // For every word in the vocabulary
        vocabulary.forEach((word, wordCategoryCount) -> {
            // Get the map of conditional probabilities for the word
            Map<String, Double> categoryCondProb = condProb.get(word);
            // Create and add a new map if it doesn't exists
            if (categoryCondProb == null) {
                categoryCondProb = new HashMap<>(priorProb.size());
                condProb.put(word, categoryCondProb);
            }
            // Get how many times the word occurred in the category
            MutableInt categoryCount = wordCategoryCount.get(category);
            // Store the amount of times the word occurred, or zero if it didn't occur
            int count = categoryCount == null ? 0 : categoryCount.intValue();
            // Get the amount of documents this category has
            int documents = categories.get(category).intValue();
            // Calculate the conditional probability
            double prob = (double) (count + 1) / (double) (documents + categories.size());
            // Add the category and the conditional probability to the map of conditional probabilities
            categoryCondProb.put(category, prob);
        });
    }
}
