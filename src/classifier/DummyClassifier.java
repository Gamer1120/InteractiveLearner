package classifier;

import fileparser.Joke;

import java.util.HashMap;

public class DummyClassifier implements Classifier {

	@Override
	public HashMap<Joke, Integer> classifyJokes(HashMap<Joke, Integer> trainingSet, HashMap<Joke, Integer> classifySet) {
		return null;
	}

	@Override
	public String getBestJoke(HashMap<Joke, Integer> jokes) {
		return null;
	}

}
