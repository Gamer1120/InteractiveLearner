package testing;

import classifier.MultinomialNaiveBayesClassifier;
import fileparser.ClassFolder;
import fileparser.FileUtils;


public class BlogTester {

    /**
     * Tests the classifier using the blog training and test sets.
     */
    public static void main(String[] args) {
        // Create a tester for a multinomial naive bayes classifier
        Tester tester = new Tester(new MultinomialNaiveBayesClassifier());
        // Add the female class training set to the training set of the classifier
        tester.addAllTraining(FileUtils.readDocuments(new ClassFolder("db/blogs/F/train", "female")));
        // Add the male class training set to the training set of the classifier
        tester.addAllTraining(FileUtils.readDocuments(new ClassFolder("db/blogs/M/train", "male")));
        // Add the female class test set to the test set of the tester
        tester.addAllTest(FileUtils.readDocuments(new ClassFolder("db/blogs/F/test", "female")));
        // Add the male class test set to the test set of the tester
        tester.addAllTest(FileUtils.readDocuments(new ClassFolder("db/blogs/M/test", "male")));
        // Execute the test
        tester.test();
    }
}
