package backend;

import javax.swing.JFrame;

import backend.enums.FrameType;

public abstract class TypedJFrame extends JFrame {

    private static final long serialVersionUID = -1007671354528640249L;
    protected FrameType _type;
}
