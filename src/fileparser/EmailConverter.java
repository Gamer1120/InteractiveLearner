package fileparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class EmailConverter implements DocumentConverter {

    // There are 2412 ham emails and 481 spam emails

    // These variables are used to indicate how many emails should be used
    // as training. The others will be used to test. These values cannot
    // be higher than the amount of email of that kind that there are.
    // Default: 1206 and 241.
    private static final int HAMTRAININGAMOUNT = 1206;
    private static final int SPAMTRAININGAMOUNT = 241;

    private ArrayList<Document> trainingSet;
    private ArrayList<Document> testSet;

    public static void main(String[] args) {
        new EmailConverter().readDocuments();
    }

    @Override
    public void readDocuments() {
        long startTime = System.nanoTime();
        this.trainingSet = new ArrayList<>();
        this.testSet = new ArrayList<>();
        trainingSet.addAll(FileUtils.readFolder("db/emails/ham", "ham", 0, HAMTRAININGAMOUNT));
        trainingSet.addAll(FileUtils.readFolder("db/emails/spam", "spam", 0, SPAMTRAININGAMOUNT));
        testSet.addAll(FileUtils.readFolder("db/emails/ham", "ham", HAMTRAININGAMOUNT));
        testSet.addAll(FileUtils.readFolder("db/emails/spam", "spam", SPAMTRAININGAMOUNT));
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
    public ArrayList<Document> getTrainingSet() {
        return trainingSet;
    }

    @Override
    public ArrayList<Document> getTestSet() {
        return testSet;
    }
}
