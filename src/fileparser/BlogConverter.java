package fileparser;

import classifier.TrainingSet;

import java.util.ArrayList;
import java.util.Arrays;

public class BlogConverter implements DocumentConverter {

    // There are 325 female blogs and 325 male blogs

    // These variables are used to indicate how many blogs should be used
    // as training. The others will be used to test. These values cannot
    // be higher than the amount of blogs of that kind that there are.
    // Default: 162 and 162.
    private static final int MALETRAININGAMOUNT = 162;
    private static final int FEMALETRAININGAMOUNT = 162;

    private TrainingSet trainingSet;
    private ArrayList<Document> testSet;

    public static void main(String[] args) {
        new BlogConverter().readDocuments();
    }

    @Override
    public void readDocuments() {
        long startTime = System.nanoTime();
        this.trainingSet = new TrainingSet();
        this.testSet = new ArrayList<>();
        trainingSet.addAll(FileUtils.readFolder("db/blogs/F", "female", 0, FEMALETRAININGAMOUNT));
        trainingSet.addAll(FileUtils.readFolder("db/blogs/M", "male", 0, MALETRAININGAMOUNT));
        testSet.addAll(FileUtils.readFolder("db/blogs/F", "female", FEMALETRAININGAMOUNT));
        testSet.addAll(FileUtils.readFolder("db/blogs/M", "male", MALETRAININGAMOUNT));
        long endTime = System.nanoTime();
        double duration = Math.round((endTime - startTime) / 1000000000 * 100.0) / 100.0;
        System.out.println("Successfully read " + trainingSet.getDocumentCount() + " training blogs and " + testSet.size() + " test blogs in " + duration + " seconds.");
        /*
        System.out.println("Examples: ");
        System.out.println("Training: " + Arrays.toString(trainingSet.get(0).getText()));
        System.out.println("Test: " + Arrays.toString(testSet.get(0).getText()));
        */
    }

    @Override
    public TrainingSet getTrainingSet() {
        return trainingSet;
    }

    @Override
    public ArrayList<Document> getTestSet() {
        return testSet;
    }

}
