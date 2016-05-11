package javaZI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class SwingMain extends JFrame {

	private static final long serialVersionUID = 1L;
	static Model model = new Model();
	static JTextField input = new JTextField();

	public SwingMain() {
		setLocation(20, 50);
		setSize(300, 200);
		setTitle("Zadatak 2");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		try {
			model.popuniIzDatoteke(Paths.get("krugovi.txt"));
		} catch (IOException e) {
			System.exit(1);
		}

		initGUI();

	}

	private void initGUI() {
		setLayout(new BorderLayout());
		JPanel labele = new JPanel();
		labele.setLayout(new GridLayout(1, 2));
		BrojKrugovaLabela brojac = new BrojKrugovaLabela(model);
		SelektiraniKrugLabela selektirani = new SelektiraniKrugLabela(model);
		labele.add(brojac);
		labele.add(selektirani);
		add(labele, BorderLayout.NORTH);

		DrawingPanel drawingPanel = new DrawingPanel(model);
		add(drawingPanel, BorderLayout.CENTER);

		JPanel unos = new JPanel();
		unos.setLayout(new GridLayout(1, 2));
		JButton button = new JButton("Izvrši");

		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				obradiUnos();
			}
		});
		unos.add(input);
		unos.add(button);
		add(unos, BorderLayout.SOUTH);

		model.dodajObservera(brojac);
		model.dodajObservera(drawingPanel);
		model.dodajObservera(selektirani);
		model.notifyAllObservers();
	}

	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			JFrame frame = new SwingMain();
			frame.setVisible(true);
		});
	}

	public static void obradiUnos() {
		String text = input.getText().trim();
		if (!text.isEmpty()) {
			String[] textSeparated = text.split("\\s+");
			String naredba = textSeparated[0];

			if (naredba.toUpperCase().equals("OBRISI")) {
				if (textSeparated.length > 1) {
					model.ukloniKrug(Integer.parseInt(textSeparated[1]));
				} else {
					model.ukloniKrug(model.getSelected());
					model.ukloniSelekciju();
				}
			}

			else if (naredba.toUpperCase().equals("DODAJ")) {
				int x = Integer.parseInt(textSeparated[1]);
				int y = Integer.parseInt(textSeparated[2]);
				int r = Integer.parseInt(textSeparated[3]);
				String foreground = textSeparated[4];
				String background = textSeparated[5];
				model.dodajKrug(new Circle(x, y, r, foreground, background));
			}

			else if (naredba.toUpperCase().equals("selektiraj".toUpperCase())
					&& textSeparated.length > 1) {
				model.postaviSelektirani(Integer.parseInt(textSeparated[1]));
			}

			else if (naredba.toUpperCase().equals("deselektiraj".toUpperCase())) {
				model.ukloniSelekciju();
			}
			
			else {
				JOptionPane.showMessageDialog(input.getParent(),
						"Naredba nije korektna.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}

	

}
