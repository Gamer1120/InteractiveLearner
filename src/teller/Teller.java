package teller;

import fileparser.Joke;

/**
 * Created by Michael on 10-11-2015.
 */
public interface Teller {

	public void tellJoke(Joke joke);

	public void processFeedback(Joke joke, String feedback);

}
