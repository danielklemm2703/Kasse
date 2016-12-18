package util.tableModels;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ColorableTableCellRenderer extends DefaultTableCellRenderer{

    private static final long serialVersionUID = 4068608912102137801L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	NonEditColorableTableModel model = (NonEditColorableTableModel) table.getModel();
	Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	if (isSelected) {
	    c.setBackground(table.getSelectionBackground());
	} else {
	    c.setBackground(model.getRowColour(row));
	}
	return c;
    }
}
