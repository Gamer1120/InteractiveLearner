package fileparser;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Michael on 5-12-2015.
 */
public class BlogConverter implements DocumentConverter {

    private LinkedList<Document> trainingSet;
    private LinkedList<Document> testSet;

    public static void main(String[] args) {
        new BlogConverter().readDocuments();
    }

    @Override
    public void readDocuments() {
        long startTime = System.nanoTime();
        this.trainingSet = new LinkedList<>();
        this.testSet = new LinkedList<>();
        trainingSet.addAll(FileUtils.readFolder("db/blogs/training/female", "female"));
        trainingSet.addAll(FileUtils.readFolder("db/blogs/training/male", "male"));
        testSet.addAll(FileUtils.readFolder("db/blogs/test", null));
        long endTime = System.nanoTime();
        double duration = Math.round((endTime - startTime) / 1000000000 * 100.0) / 100.0;
        System.out.println("Successfully read " + trainingSet.size() + " training blogs and " + testSet.size() + " test blogs in " + duration + " seconds.");

        System.out.println("Examples: ");
        System.out.println("Training: " + Arrays.toString(trainingSet.get(0).getText()));
        System.out.println("Test: " + Arrays.toString(testSet.get(0).getText()));

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
