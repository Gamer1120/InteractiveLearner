package fileparser;

import java.io.File;
import java.util.HashMap;

public interface DocumentConverter {

	public void readDocuments();

	public HashMap<Document, Integer> getDocuments();

}
