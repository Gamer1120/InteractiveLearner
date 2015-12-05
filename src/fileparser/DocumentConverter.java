package fileparser;

import classifier.TrainingSet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public interface DocumentConverter {

	public void readDocuments();

	public TrainingSet getTrainingSet();

	public ArrayList<Document> getTestSet();

}
