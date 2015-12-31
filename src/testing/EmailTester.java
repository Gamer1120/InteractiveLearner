package testing;

import classifier.MultinomialNaiveBayesClassifier;
import utils.Utils;

import java.util.List;

public class EmailTester {
    // The percentage of documents to be used for training
    private static final double TRAINING_PERCENTAGE = 0.10d;

    /**
     * Tests the classifier using the email set.
     */
    public static void main(String[] args) {
        // Create a tester for a multinomial naive bayes classifier
        Tester tester = new Tester(new MultinomialNaiveBayesClassifier());
        // Add the ham class texts
        add(tester, "ham", Utils.readFiles("db/emails/ham"));
        // Add the spam class texts
        add(tester, "spam", Utils.readFiles("db/emails/spam"));
        // Execute the test
        tester.test();
    }

    /**
     * Adds the training percentage of texts to the training set and all the other texts to the test set.
     *
     * @param tester - the tester to add the documents to.
     * @param texts  - the texts to be added.
     */
    private static void add(Tester tester, String category, List<String> texts) {
        // Calculate the amount of documents for training using the training percentage
        int trainingSetSize = (int) (TRAINING_PERCENTAGE * texts.size());
        // Add the training documents to the training set of the classifier
        tester.addAllTraining(Utils.toDocuments(texts.subList(0, trainingSetSize), category));
        // Add the test documents to the test set of the tester
        tester.addAllTest(Utils.toTexts(texts.subList(trainingSetSize, texts.size()), category));
    }
}
