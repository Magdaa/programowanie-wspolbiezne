package pl.lodz.wspolbiezne.lab07;

import java.io.Serializable;
import java.util.List;

public class MacierzeDto implements Serializable {
	
	private static final long serialVersionUID = 1363948434542689151L;
	private List<Zbi�r> rows;
	private List<Zbi�r> columns;

	public List<Zbi�r> getRows() {
		return rows;
	}

	public void setRows(List<Zbi�r> values) {
		this.rows = values;
	}
	
	public Zbi�r getRow(int index) {
		return getRows().get(index);
	}
	
	public List<Zbi�r> getColumns() {
		return columns;
	}
	
	public Zbi�r getColumn(int index) {
		return getColumns().get(index);
	}

	public void setColumns(List<Zbi�r> columns) {
		this.columns = columns;
	}
}

class Zbi�r implements Serializable {
	private static final long serialVersionUID = 1363948434542689151L;
	private List<Double> values;
	private int index;
	public List<Double> getValues() {
		return values;
	}
	
	public Double getValue(int index) {
		return getValues().get(index);
	}
	
	public void setValues(List<Double> values) {
		this.values = values;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}