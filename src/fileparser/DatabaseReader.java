package fileparser;

import java.io.File;
import java.util.HashMap;

public interface DatabaseReader {

	public void readJokes(File file);

	public HashMap<Joke, Integer> getJokes();

}
