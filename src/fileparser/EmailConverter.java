package fileparser;

import java.util.Arrays;
import java.util.LinkedList;

public class EmailConverter implements DocumentConverter {


    private LinkedList<Document> trainingSet;
    private LinkedList<Document> testSet;

    public static void main(String[] args) {
        new EmailConverter().readDocuments();
    }

    @Override
    public void readDocuments() {
        long startTime = System.nanoTime();
        this.trainingSet = new LinkedList<>();
        this.testSet = new LinkedList<>();
        trainingSet.addAll(FileUtils.readFolder("db/emails/training/ham", "ham"));
        trainingSet.addAll(FileUtils.readFolder("db/emails/training/spam", "spam"));
        testSet.addAll(FileUtils.readFolder("db/emails/test", null));
        long endTime = System.nanoTime();
        double duration = Math.round((endTime - startTime) / 1000000000 * 100.0) / 100.0;
        System.out.println("Successfully read " + trainingSet.size() + " training emails and " + testSet.size() + " test emails in " + duration + " seconds.");
        /*
        System.out.println("Examples: ");
        System.out.println("Training: " + Arrays.toString(trainingSet.get(0).getText()));
        System.out.println("Test: " + Arrays.toString(testSet.get(0).getText()));
        */
    }

    @Override
    public LinkedList<Document> getTrainingSet() {
        return trainingSet;
    }

    @Override
    public LinkedList<Document> getTestSet() {
        return testSet;
    }
}
