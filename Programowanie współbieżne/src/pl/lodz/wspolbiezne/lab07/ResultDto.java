package pl.lodz.wspolbiezne.lab07;

import java.io.Serializable;
import java.util.List;

public class ResultDto implements Serializable {

	private static final long serialVersionUID = 1363948434542689151L;

	private Element[] elements;

	public Element[] getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		Element[] target = new Element[elements.size()];
		for (int i = 0; i < target.length; i++) {
			target[i] = elements.get(i);
		}
		this.elements = target;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Element element : elements) {
			sb.append(element.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

	public Element getElement(int i) {
		return elements[i];
	}
}

class Element implements Serializable {
	private static final long serialVersionUID = 1363948434542689151L;
	private int wiersz;

	public Element(int j, int i, int k) {
		this.kolumna = j;
		this.wiersz = i;
		this.warto�� = k;
	}

	public Element() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		return "[" + getKolumna() + "][" + getWiersz() + "]=" + getWarto��();
	}

	public int getWiersz() {
		return wiersz;
	}

	public void setWiersz(int wiersz) {
		this.wiersz = wiersz;
	}

	public int getKolumna() {
		return kolumna;
	}

	public void setKolumna(int kolumna) {
		this.kolumna = kolumna;
	}

	public double getWarto��() {
		return warto��;
	}

	public void setWarto��(double warto��) {
		this.warto�� = warto��;
	}

	private int kolumna;
	private double warto��;
}
