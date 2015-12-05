package fileparser;

import java.io.File;
import java.util.HashMap;

public interface DocumentConverter {

	public void readJokes(File file);

	public HashMap<Document, Integer> getJokes();

}
