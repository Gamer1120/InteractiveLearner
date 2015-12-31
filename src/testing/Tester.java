package testing;

import classifier.Classifier;
import model.Document;
import model.Text;
import utils.MutableInt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Tester {
    // The classifier
    private final Classifier classifier;
    // A map of categories and texts
    private final Collection<Text> testSet;

    /**
     * Tests the specified classifier.
     *
     * @param classifier - the specified classifier
     */
    public Tester(Classifier classifier) {
        this.classifier = classifier;
        testSet = new ArrayList<>();
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
     * Adds all documents to the training set of the classifier
     *
     * @param documents - the documents to be added
     */
    public void addAllTraining(Collection<Document> documents) {
        classifier.addAll(documents);
    }

    /**
     * Adds a text to the test set.
     *
     * @param text - the text to be added
     */
    public void addTest(Text text) {
        testSet.add(text);
    }

    /**
     * Adds all texts to the test set.
     *
     * @param texts - the texts to be added
     */
    public void addAllTest(Collection<Text> texts) {
        texts.forEach(this::addTest);
    }

    /**
     * Tests the classifier using the test set.
     */
    public void test() {
        // Train the classifier
        classifier.train();
        // The total amount of correct classifications
        MutableInt correct = new MutableInt();
        // The total amount of incorrect classifications
        MutableInt incorrect = new MutableInt();
        // Map of classification and how many correctly and incorrectly classified documents for the classification
        // scores[0] = correct and scores[1] = incorrect
        Map<String, int[]> scores = new HashMap<>();
        // Test for each document is the category equals the category calculated by the classifier
        testSet.forEach(text -> {
            // Get the current score for the category or add a new score if it doesn't exists
            int[] score = scores.get(text.getCategory());
            if (score == null) {
                score = new int[2];
                scores.put(text.getCategory(), score);
            }
            // Test if the category equals the category calculated by the classifier
            // and update the score accordingly
            if (text.getCategory().equals(classifier.classify(text.getText()))) {
                correct.add(1);
                score[0]++;
            } else {
                incorrect.add(1);
                score[1]++;
            }
        });
        // Output the amount of correct and incorrect documents for each class to the console
        scores.forEach((category, score) -> System.out.println(category + ": correct = " + score[0] + ", incorrect = " + score[1]));
        // Output the total amount of correct and incorrect documents
        System.out.println("Total: correct = " + correct + ", incorrect = " + incorrect);
    }
}
