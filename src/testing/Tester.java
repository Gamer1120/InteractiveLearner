package testing;

import classifier.Classifier;
import classifier.Document;

import java.util.ArrayList;
import java.util.Collection;

public class Tester {
    // The classifier
    private Classifier classifier;
    // The test set
    private Collection<Document> testDocuments;

    /**
     * Tests the specified classifier.
     *
     * @param classifier - the specified classifier
     */
    public Tester(Classifier classifier) {
        testDocuments = new ArrayList<>();
        this.classifier = classifier;
    }

    /**
     * Adds a document to the training set of the classifier
     *
     * @param document - the document to be added
     */
    public void addTraining(Document document) {
        classifier.add(document);
    }

    /**
     * Adds a document to the test set.
     *
     * @param document - the document to be added
     */
    public void addTest(Document document) {
        testDocuments.add(document);
    }

    /**
     * Adds all documents to the training set of the classifier
     *
     * @param documents - the documents to be added
     */
    public void addAllTraining(Collection<Document> documents) {
        classifier.addAll(documents);
    }

    /**
     * Adds all documents to the test set.
     *
     * @param documents - the documents to be added
     */
    public void addAllTest(Collection<Document> documents) {
        testDocuments.addAll(documents);
    }

    /**
     * Tests the classifier using the test set.
     */
    public void test() {
        // The amount of correct classifications
        int correct = 0;
        // The amount of incorrect classifications
        int incorrect = 0;
        // Test for each document is the classification equals the classification calculated by the classifier
        for (Document document : testDocuments) {
            if (document.classification.equals(classifier.classify(document.text))) {
                // Add one to correct if the classification equals the classification calculated by the classifier
                correct++;
            } else {
                // Add one to incorrect if the classification doesn't equal the classification calculated by the classifier
                incorrect++;
            }
        }
        // Output the amount of correct and incorrect documents to the console
        System.out.println("correct = " + correct + ", incorrect = " + incorrect);
    }
}
