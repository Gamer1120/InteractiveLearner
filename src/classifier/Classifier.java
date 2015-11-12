package classifier;

import fileparser.Joke;

import java.util.HashMap;

public interface Classifier {

	/**
	 * Returns the classifications of ALL jokes, both the training and the set to be classified.
	 * @param trainingSet The set that the classification has been given off.
	 * @param classifySet The set to be classified.
	 * @return The classification of ALL jokes.
	 */
	public HashMap<Joke, Integer> classifyJokes(HashMap<Joke, Integer> trainingSet, HashMap<Joke, Integer> classifySet);

	/**
	 * Returns the best joke from a set of given jokes.
	 * @param jokes A HashMap with jokes and their classification.
	 * @return The best joke in the jokes map.
	 */
	public String getBestJoke(HashMap<Joke, Integer> jokes);

}
