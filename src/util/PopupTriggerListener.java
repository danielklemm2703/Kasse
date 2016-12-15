package util;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

public class PopupTriggerListener extends MouseAdapter {
    private JPopupMenu _popUpMenu;

    public PopupTriggerListener(final JPopupMenu popUpMenu) {
	super();
	_popUpMenu = popUpMenu;
    }

    public void mousePressed(MouseEvent ev) {
	if (ev.isPopupTrigger()) {
	    _popUpMenu.show(ev.getComponent(), ev.getX(), ev.getY());
	}
    }

    public void mouseReleased(MouseEvent ev) {
	if (ev.isPopupTrigger()) {
	    _popUpMenu.show(ev.getComponent(), ev.getX(), ev.getY());
	}
    }
}