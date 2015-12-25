package testing;

import classifier.Document;
import classifier.FeatureSelection;
import classifier.MultinomialNaiveBayesClassifier;
import fileparser.FileUtils;

import java.util.List;

public class EmailTester {
    // The percentage of documents to be used for training
    private static final double TRAINING_PERCENTAGE = 0.10d;

    /**
     * Tests the classifier using the email set.
     */
    public static void main(String[] args) {
        // Create a tester for a multinomial naive bayes classifier
        Tester tester = new Tester(new MultinomialNaiveBayesClassifier(new FeatureSelection(false)));
        // Add the ham class documents
        add(tester, FileUtils.readDocuments("db/emails/ham", "ham"));
        // Add the spam class documents
        add(tester, FileUtils.readDocuments("db/emails/spam", "spam"));
        // Execute the test
        tester.test();
    }

    /**
     * Adds the training percentage of documents to the training set and all the other documents to the test set.
     *
     * @param tester    - the tester to add the documents to.
     * @param documents - the documents to be added.
     */
    private static void add(Tester tester, List<Document> documents) {
        // Calculate the amount of documents for training using the training percentage
        int trainingSetSize = (int) (TRAINING_PERCENTAGE * documents.size());
        // Add the training documents to the training set of the classifier
        documents.stream().limit(trainingSetSize).forEach(tester::addTraining);
        // Add the test documents to the test set of the tester
        documents.stream().skip(trainingSetSize).forEach(tester::addTest);
    }
}
