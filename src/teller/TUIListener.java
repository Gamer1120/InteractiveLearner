package teller;

import java.util.Scanner;

public class TUIListener extends Thread implements FeedbackListener {

	private Teller teller;

	public TUIListener(Teller teller) {

	}

	@Override
	public void run() {
		Scanner scan = new Scanner(System.in);
		boolean loop = true;
		while (loop) {
			String feedback = scan.nextLine();
			if (!feedback.equals("")) {
				processFeedback(feedback);
				loop = false;
			}
		}
	}


	@Override
	public void processFeedback(String feedback) {
		teller.processFeedback(feedback);
	}
}
