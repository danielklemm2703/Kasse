package util.table;

import javax.swing.DefaultListSelectionModel;

public class MultiselectListSelectionModel extends DefaultListSelectionModel {

    private static final long serialVersionUID = 108890488980790255L;

    @Override
    public void setSelectionInterval(int index0, int index1) {
	if (super.isSelectedIndex(index0)) {
	    super.removeSelectionInterval(index0, index1);
	} else {
	    super.addSelectionInterval(index0, index1);
	}
    }
}
