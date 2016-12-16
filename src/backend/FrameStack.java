package backend;

import java.util.LinkedList;

import backend.enums.FrameType;

public class FrameStack {

    private static LinkedList<TypedJFrame> _frameStack = new LinkedList<TypedJFrame>();
    
    public static void openAndDisposeOthers(TypedJFrame frame) {
	if (_frameStack.size() == 0 || _frameStack.getLast()._type.equals(FrameType.NOTIFICATION)) {
	    // do nothing, because an error or notification has focus
	} else {
	    _frameStack.clear();
	    _frameStack.add(frame);
	    frame.setVisible(true);
	}
    }

}
