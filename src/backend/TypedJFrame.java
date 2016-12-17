package backend;

import javax.swing.JFrame;

import backend.framemanagement.WindowListeners;
import database.enums.FrameType;

public abstract class TypedJFrame extends JFrame {

    private static final long serialVersionUID = -1007671354528640249L;
    public FrameType _type;

    protected TypedJFrame() {
	this.addWindowFocusListener(WindowListeners.handleFramePriorities(this));
    }
}
