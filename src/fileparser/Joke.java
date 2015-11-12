package fileparser;

public class Joke {

	private String joke;
	private int classification;

	public Joke(String joke, int classification){
		this.joke = joke;
		this.classification = classification;
	}

	public String getJoke(){
		return joke;
	}

	public int getClassification(){
		return classification;
	}

}
