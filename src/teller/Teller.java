package teller;

import fileparser.Joke;

public interface Teller {

	public void tellJoke(Joke joke);

	public void processFeedback(String feedback);

}
