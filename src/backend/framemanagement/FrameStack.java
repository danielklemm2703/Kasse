package backend.framemanagement;

import java.util.LinkedList;

import backend.TypedJFrame;

public class FrameStack {

    private static LinkedList<TypedJFrame> _frameStack = new LinkedList<TypedJFrame>();

    static void openAndDisposeOthers(TypedJFrame frame) {
	// if (_frameStack.size() == 0 ||
	// _frameStack.getLast()._type.equals(FrameType.NOTIFICATION)) {
	// // do nothing, because an error or notification has focus
	// } else {
	for (TypedJFrame jframe : _frameStack) {
	    jframe.dispose();
	}
	_frameStack.clear();
	System.err.println("Cleared all Frames in Stack");
	addFrame(frame);
	// }
    }

    // public static void closeNotification(TypedJFrame frame) {
    // if (_frameStack.size() > 0 &&
    // _frameStack.getLast()._type.equals(FrameType.NOTIFICATION)) {
    // _frameStack.getLast().dispose();
    // }
    // }

    static void addFrame(TypedJFrame frame) {
	_frameStack.add(frame);
	frame.setVisible(true);
	System.err.println("add " + frame._type.toString() + " to FrameStack.");
    }

    static void closeFrameOnTop(TypedJFrame frame) {
	if (_frameStack.getLast().equals(frame)) {
	    System.err.println(frame._type.toString() + " was opened on top");
	    _frameStack.removeLast().dispose();
	    System.err.println("Closed " + frame._type.toString() + "-Frame.");
	    // if (!_frameStack.getFirst().isVisible()) {
	    // _frameStack.getFirst().setVisible(true);
	    // System.err.println("Set underlying " +
	    // _frameStack.getFirst()._type.toString() + "-Frame to visible.");
	    // }
	} else {
	    System.err.println("Tried to find " + frame._type.toString() + "-Frame to close it, but was not opened on top.");
	}
    }

}
