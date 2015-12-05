package testing;

import classifier.Classifier;
import fileparser.BlogConverter;
import fileparser.Document;

public class BlogTester {

    public BlogTester() {
        BlogConverter blogConverter = new BlogConverter();
        Classifier classifier = new Classifier();
        blogConverter.readDocuments();
        classifier.setTrainingSet(blogConverter.getTrainingSet());
        int correct = 0;
        int incorrect = 0;
        for (Document document : blogConverter.getTestSet()) {
            if (document.getClassification().equals(classifier.classify(document.getText()))) {
                correct++;
            } else {
                incorrect++;
            }
        }
        System.out.println("correct = " + correct + ", incorrect = " + incorrect);
    }

    public static void main(String[] args) {
        new BlogTester();
    }
}
