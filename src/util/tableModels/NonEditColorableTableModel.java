package util.tableModels;

import java.awt.Color;
import java.util.LinkedList;

public class NonEditColorableTableModel extends NonEditableColumnTableModel {
    private static final long serialVersionUID = -4609098657002208479L;
    private LinkedList<Color> _rowColours = new LinkedList<Color>();

    public void setRowColour(int row, Color c) {
	_rowColours.set(row, c);
    }

    @Override
    public void addRow(final Object[] rowData) {
	super.addRow(rowData);
	_rowColours.add(Color.white);
    }

    public void addRow(final Object[] rowData, final Color color) {
	super.addRow(rowData);
	_rowColours.add(color);
    }

    public Color getRowColour(int row) {
	return _rowColours.get(row);
    }
}
