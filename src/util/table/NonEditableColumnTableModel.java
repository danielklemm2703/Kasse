package util.table;

import javax.swing.table.DefaultTableModel;

public class NonEditableColumnTableModel extends DefaultTableModel {

    private static final long serialVersionUID = -1103655641542106495L;

    @Override
    public boolean isCellEditable(int row, int column) {
	return false;
    }
}
