package testing;

import classifier.Document;
import classifier.MultinomialNaiveBayesClassifier;
import fileparser.ClassFolder;
import fileparser.FileUtils;

import java.util.List;

public class EmailTester {
    // The percentage of documents to be used for training
    private static final double TRAINING_PERCENTAGE = 0.10;

    /**
     * Tests the classifier using the email set.
     */
    public static void main(String[] args) {
        // Create a tester for a multinomial naive bayes classifier
        Tester tester = new Tester(new MultinomialNaiveBayesClassifier());
        // Add the ham class documents
        add(tester, FileUtils.readDocuments(new ClassFolder("db/emails/ham", "ham")));
        // Add the spam class documents
        add(tester, FileUtils.readDocuments(new ClassFolder("db/emails/spam", "spam")));
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
        tester.addAllTraining(documents.subList(0, trainingSetSize));
        // Add the test documents to the test set of the tester
        tester.addAllTest(documents.subList(trainingSetSize, documents.size()));
    }
}
