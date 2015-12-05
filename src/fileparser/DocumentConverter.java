package fileparser;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public interface DocumentConverter {

	public void readDocuments();

	public LinkedList<Document> getTrainingSet();

	public LinkedList<Document> getTestSet();

}
