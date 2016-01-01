package classifier;

import utils.MutableDouble;
import utils.MutableInt;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class MultinomialNaiveBayesClassifier extends NaiveBayesClassifierBase {

    /**
     * A Multinomial implementation of the Naive Bayes base.
     */
    public MultinomialNaiveBayesClassifier() {
        super();
        stopWords = true;
        wordCount = true;
        chiSquare = true;
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
        MutableInt words = new MutableInt();
        vocabulary.values().forEach(categoryCount -> {
            MutableInt count = categoryCount.get(category);
            if (count != null) {
                words.add(count.intValue());
            }
        });
        vocabulary.forEach((word, wordCategoryCount) -> {
            Map<String, Double> categoryCondProb = condProb.get(word);
            if (categoryCondProb == null) {
                categoryCondProb = new HashMap<>(priorProb.size());
                condProb.put(word, categoryCondProb);
            }
            MutableInt categoryCount = wordCategoryCount.get(category);
            int count = categoryCount == null ? 0 : categoryCount.intValue();
            double prob = Math.log((double) (count + 1) / (double) (words.intValue() + vocabulary.size()));
            categoryCondProb.put(category, prob);
        });
    }
}
