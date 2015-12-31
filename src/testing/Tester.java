package testing;

import classifier.Classifier;
import classifier.TempDocument;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Tester {
    // The classifier
    private final Classifier classifier;
    // The test set
    private final Collection<TempDocument> testDocuments;

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
    public void addTraining(TempDocument document) {
        classifier.add(document);
    }

    /**
     * Adds a document to the test set.
     *
     * @param document - the document to be added
     */
    public void addTest(TempDocument document) {
        testDocuments.add(document);
    }

    /**
     * Adds all documents to the training set of the classifier
     *
     * @param documents - the documents to be added
     */
    public void addAllTraining(Collection<TempDocument> documents) {
        classifier.addAll(documents);
    }

    /**
     * Adds all documents to the test set.
     *
     * @param documents - the documents to be added
     */
    public void addAllTest(Collection<TempDocument> documents) {
        testDocuments.addAll(documents);
    }

    /**
     * Tests the classifier using the test set.
     */
    public void test() {
        // The total amount of correct classifications
        int correct = 0;
        // The total amount of incorrect classifications
        int incorrect = 0;
        // Map of classification and how many correctly and incorrectly classified documents for the classification
        // scores[0] = correct and scores[1] = incorrect
        Map<String, int[]> scores = new HashMap<>();
        // Test for each document is the classification equals the classification calculated by the classifier
        for (TempDocument document : testDocuments) {
            // Get the current score for the classification or add a new score if it doesn't exists
            int[] score = scores.get(document.getClassification());
            if (score == null) {
                score = new int[2];
                scores.put(document.getClassification(), score);
            }
            // Test if the classification equals the classification calculated by the classifier
            // and update the score accordingly
            if (document.getClassification().equals(classifier.classify(document.getText()))) {
                correct++;
                score[0]++;
            } else {
                incorrect++;
                score[1]++;
            }
        }
        // Output the amount of correct and incorrect documents for each class to the console
        scores.forEach((className, score) -> System.out.println(className + ": correct = " + score[0] + ", incorrect = " + score[1]));
        // Output the total amount of correct and incorrect documents
        System.out.println("Total: correct = " + correct + ", incorrect = " + incorrect);
    }
}
