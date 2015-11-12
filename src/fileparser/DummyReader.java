package fileparser;

import java.io.File;
import java.util.HashMap;

public class DummyReader implements DatabaseReader {
	@Override
	public void readJokes(File file) {

	}

	@Override
	public HashMap<Joke, Integer> getJokes() {
		return null;
	}
}
