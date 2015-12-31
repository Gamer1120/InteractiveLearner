package testing;

import classifier.MultinomialNaiveBayesClassifier;
import utils.Utils;


public class BlogTester {

    /**
     * Tests the classifier using the blog training and test sets.
     */
    public static void main(String[] args) {
        // Create a tester
        Tester tester = new Tester(new MultinomialNaiveBayesClassifier());
        // Add the female category training set to the training set of the classifier
        tester.addAllTraining(Utils.toDocuments(Utils.readFiles("db/blogs/F/train"), "female"));
        // Add the male category training set to the training set of the classifier
        tester.addAllTraining(Utils.toDocuments(Utils.readFiles("db/blogs/M/train"), "male"));
        // Add the female category test set to the test set of the tester
        tester.addAllTest(Utils.toTexts(Utils.readFiles("db/blogs/F/test"), "female"));
        // Add the male category test set to the test set of the tester
        tester.addAllTest(Utils.toTexts(Utils.readFiles("db/blogs/M/test"), "male"));
        // Execute the test
        tester.test();
    }
}
