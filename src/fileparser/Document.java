package fileparser;

public class Document {

	private String joke;
	private int classification;

	public Document(String joke, int classification){
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
