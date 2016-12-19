package util.table;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

public class MultiselectTable extends JTable{

    private static final long serialVersionUID = 8235447128239188390L;

    public MultiselectTable() {
	setShowHorizontalLines(true);
	setShowVerticalLines(true);
	setGridColor(Color.BLACK);
	setDefaultRenderer(Object.class, new ColorableTableCellRenderer());
	setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	setSelectionModel(new MultiselectListSelectionModel());
    }
}
