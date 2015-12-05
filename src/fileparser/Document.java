package fileparser;

public class Document {

	private String[] text;
	private String classification;

	public Document(String[] text, String classification){
		this.text = text;
		this.classification = classification;
	}

	public String[] getText(){
		return text;
	}

	public String getClassification(){
		return classification;
	}

}
