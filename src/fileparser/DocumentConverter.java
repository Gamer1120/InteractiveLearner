package fileparser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public interface DocumentConverter {

	public void readDocuments();

	public ArrayList<Document> getTrainingSet();

	public ArrayList<Document> getTestSet();

}
