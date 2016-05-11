package javaZI;

import java.awt.Color;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Model {

	private int selected = -1;
	List<Circle> circles = new ArrayList<>();
	List<ModelObserver> observers = new ArrayList<>();

	// dodaje novi krug u model:
	void dodajKrug(int cx, int cy, int r, Color obrub, Color ispuna) {
		circles.add(new Circle(cx, cy, r, obrub, ispuna));
		for (ModelObserver o : observers) {
			o.execute(this);
		}
	}

	public void dodajKrug(Circle c) {
		circles.add(c);
		for (ModelObserver o : observers) {
			o.execute(this);
		}
	}

	// vraća krug koji model pamti pod navedenim rednim brojem:
	public Circle dohvati(int index) {
		return circles.get(index);
	}

	// vraća broj krugova u modelu:
	int brojKrugova() {
		return circles.size();
	}

	// briše iz modela zadani krug:
	public void ukloniKrug(int index) {
		if (index >= 0 && index < circles.size()) {
			circles.remove(index);
			for (ModelObserver o : observers) {
				o.execute(this);
			}
		}

	}

	// postavi selektirani krug (samo jedan može biti selektiran); -1 postavlja
	// da ništa nije selektirano:
	public void postaviSelektirani(int index) {
		if (index >= 0 && index < circles.size()) {
			if (selected >= 0) {
				Circle c = circles.get(selected);
				c.setBackground(new Color(255 - c.getBackground().getRed(), 255 - c
						.getBackground().getBlue(), 255 - c.getBackground()
						.getGreen()));
				c.setForeground(new Color(255 - c.getForeground().getRed(), 255 - c
						.getForeground().getBlue(), 255 - c.getForeground()
						.getGreen()));
			}
			selected = index;
			Circle c = circles.get(index);
			c.setBackground(new Color(255 - c.getBackground().getRed(), 255 - c
					.getBackground().getBlue(), 255 - c.getBackground()
					.getGreen()));
			c.setForeground(new Color(255 - c.getForeground().getRed(), 255 - c
					.getForeground().getBlue(), 255 - c.getForeground()
					.getGreen()));
			
			for (ModelObserver o : observers) {
				o.execute(this);
			}
		}
	}

	public void ukloniSelekciju() {
		if (selected >= 0) {
			Circle c = circles.get(selected);
			c.setBackground(new Color(255 - c.getBackground().getRed(), 255 - c
					.getBackground().getBlue(), 255 - c.getBackground()
					.getGreen()));
			c.setForeground(new Color(255 - c.getForeground().getRed(), 255 - c
					.getForeground().getBlue(), 255 - c.getForeground()
					.getGreen()));
		}
		selected = -1;
		for (ModelObserver o : observers) {
			o.execute(this);
		}
	}

	// vraća indeks selektiranog kruga ili -1 ako ništa nije selektirano:
	int dohvatiIndeksSelektiranogKruga() {
		return selected;
	}

	// dohvaća redni broj kruga najbližeg(1); vidi pojašnjenje ispod predanoj
	// točki:
	public int najblizi(int tx, int ty) {
		double minDistance = 5;
		int closest = -1;
		for (int i = 0; i < circles.size(); i++) {
			Circle circle = circles.get(i);
			double distance = Math
					.abs(Math.sqrt(Math.pow(tx - circle.getX(), 2)
							+ Math.pow(ty - circle.getY(), 2))
							- circle.getRadius());

			if (distance < minDistance) {
				closest = i;
			}
		}
		return closest;
	}

	public void popuniIzDatoteke(Path path) throws IOException {
		try {
			List<String> lines = Files.readAllLines(path,
					StandardCharsets.UTF_8);

			for (String s : lines) {
				s = s.trim();
				if (s.isEmpty()) {
					continue;
				}
				String[] lineSeparated = s.split("\\s+");
				int x = Integer.parseInt(lineSeparated[0]);
				int y = Integer.parseInt(lineSeparated[1]);
				int r = Integer.parseInt(lineSeparated[2]);
				String foreground = lineSeparated[3];
				String background = lineSeparated[4];

				circles.add(new Circle(x, y, r, foreground, background));
			}
		} catch (Exception e) {
		}
	}

	public int getSelected() {
		return selected;
	}

	public List<Circle> getCircles() {
		return circles;
	}

	public void notifyAllObservers() {
		for (ModelObserver o : observers) {
			o.execute(this);
		}
	}

	public void dodajObservera(ModelObserver o) {
		observers.add(o);
	}

}
