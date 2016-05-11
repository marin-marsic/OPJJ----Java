package javaZI;

import javax.swing.JLabel;

public class BrojKrugovaLabela extends JLabel implements ModelObserver {

	private Model model;

	public BrojKrugovaLabela(Model model) {
		this.setText("0");
		this.model = model;
	}

	@Override
	public void execute(Model model) {
		this.model = model;
		this.setText(String.valueOf(model.getCircles().size()));
	}

}
