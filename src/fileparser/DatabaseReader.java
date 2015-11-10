package fileparser;

import java.io.File;
import java.util.HashMap;

public class DatabaseReader {

	private HashMap<Joke, Integer> jokes;

	public DatabaseReader() {
		this.jokes = new HashMap<>();
	}

	public void readJokes(File file) {
		//TODO
	}

	public HashMap<Joke, Integer> getJokes() {
		return jokes;
	}

}
