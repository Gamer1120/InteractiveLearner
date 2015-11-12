package teller;

import fileparser.Joke;

public class BasicTeller implements Teller{

	@Override
	public void tellJoke(Joke joke) {
		System.out.println(joke.getJoke());
	}

	@Override
	public void processFeedback(String feedback) {

	}
}
