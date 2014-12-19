package pl.lodz.wspolbiezne.lab07;

import java.io.Serializable;
import java.util.List;

public class MacierzeDto implements Serializable {

	private static final long serialVersionUID = 1363948434542689151L;
	private Zbi�r[] rows;
	private Zbi�r[] columns;

	public Zbi�r[] getRows() {
		return rows;
	}

	public void setRows(Zbi�r[] values) {
		this.rows = values;
	}

	public void setRows(List<Zbi�r> rows) {
		Zbi�r[] target = new Zbi�r[rows.size()];
		for (int i = 0; i < target.length; i++) {
			target[i] = rows.get(i);
		}
		this.rows = target;
	}

	public Zbi�r getRow(int index) {
		return rows[index];
	}

	public Zbi�r[] getColumns() {
		return columns;
	}

	public Zbi�r getColumn(int index) {
		return columns[index];
	}

	public void setColumns(List<Zbi�r> columns) {
		Zbi�r[] target = new Zbi�r[columns.size()];
		for (int i = 0; i < target.length; i++) {
			target[i] = columns.get(i);
		}
		this.columns = target;
	}

}

class Zbi�r implements Serializable {
	private static final long serialVersionUID = 1363948434542689151L;
	private double[] values;
	private int index;

	public double[] getValues() {
		return values;
	}

	public Double getValue(int index) {
		return values[index];
	}

	public void setValues(List<Double> values) {
		this.values = values.stream().mapToDouble(Double::doubleValue)
				.toArray();
	}

	public void setValues(double[] values) {
		this.values = values;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}