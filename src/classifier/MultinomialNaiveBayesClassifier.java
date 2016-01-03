package classifier;

import utils.MutableDouble;
import utils.MutableInt;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * A Multinomial implementation of the Naive Bayes base.
 */
public class MultinomialNaiveBayesClassifier extends NaiveBayesClassifierBase {

    /**
     * Constructs the classifier with default values
     */
    public MultinomialNaiveBayesClassifier() {
        super(true, true, true, 0.01d, 0.7d, 10.83d);
    }

    /**
     * Constructs the classifier with the specified values
     */
    public MultinomialNaiveBayesClassifier(boolean stopWords, boolean wordCount, boolean chiSquare,
                                           double minPercent, double maxPercent, double criticalValue) {
        super(stopWords, wordCount, chiSquare, minPercent, maxPercent, criticalValue);
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
    protected void add() {
        // For every document in the training set
        trainingSet.forEach(document -> {
            // Add the document to the category
            addDocument(document.getCategory());
            // Add all the words of the document to the vocabulary
            document.getTokens().forEach((word, count) -> addWord(word, document.getCategory(), count.intValue()));
        });
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void calculateCondProb(String category) {
        // Count all the words in the category
        MutableInt words = new MutableInt();
        // For every word in the vocabulary
        vocabulary.values().forEach(categoryCount -> {
            // Get how many times the word occurred in the category
            MutableInt count = categoryCount.get(category);
            // If the word occurred
            if (count != null) {
                // Add it to the word count
                words.add(count.intValue());
            }
        });
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
            // Calculate the conditional probability
            double prob = Math.log((double) (count + 1) / (double) (words.intValue() + vocabulary.size()));
            // Add the category and the conditional probability to the map of conditional probabilities
            categoryCondProb.put(category, prob);
        });
    }
}
