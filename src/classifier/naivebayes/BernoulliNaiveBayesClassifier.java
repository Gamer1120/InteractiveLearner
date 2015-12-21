package classifier.naivebayes;

import fileparser.FileUtils;
import utils.MutableDouble;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BernoulliNaiveBayesClassifier extends NaiveBayesClassifierBase {

    /**
     * A binomial implementation of the classify method of the Naive Bayes base class.
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
        String[] tokens = FileUtils.tokenize(text);
        HashSet<String> words = new HashSet<>(tokens.length);
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
                    double condProb = 1 - Math.log((double) (classValues.getIndividualWordCount(word) + 1) / (double) (classValues.getDocumentCount() + classes.size()));
                    score.add(condProb);
                });
            }
        }
        // Return the class with the highest score
        return scores.entrySet().stream().max((entry1, entry2) -> entry1.getValue().doubleValue() > entry2.getValue().doubleValue() ? 1 : -1).get().getKey();
    }
}
