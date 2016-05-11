package javaZI;

import javax.swing.JLabel;

public class SelektiraniKrugLabela extends JLabel implements ModelObserver {

	private Model model;

	public SelektiraniKrugLabela(Model model) {
		this.setText("-");
		this.model = model;
	}

	@Override
	public void execute(Model model) {
		this.model = model;
		int selected = model.getSelected();
		if (selected >= 0){
			this.setText(String.valueOf(selected));
		}
		else{
			this.setText("-");	
		}
		
	}

}