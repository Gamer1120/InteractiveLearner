package classifier.naivebayes;

import fileparser.FileUtils;
import utils.MutableDouble;

import java.util.HashMap;
import java.util.Map;

public class MultinomialNaiveBayesClassifier extends NaiveBayesClassifierBase {

    /**
     * A multinomial implementation of the classify method of the Naive Bayes base class.
     *
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
}
