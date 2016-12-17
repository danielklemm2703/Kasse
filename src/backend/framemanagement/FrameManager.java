package backend.framemanagement;

import java.util.LinkedList;

import backend.TypedJFrame;
import database.enums.FrameType;

public class FrameManager {
    private static LinkedList<TypedJFrame> _frameStack = new LinkedList<TypedJFrame>();

    private static final void focusFrames() {
	// do not open requested frame, because a not answered notification
	// is opened. Try to request focus for the other open frames.
	for (TypedJFrame jframe : _frameStack) {
	    jframe.setVisible(true);
	    jframe.requestFocus();
	    System.err.println("Set focus to underlaying or top " + jframe._type.toString());
	}
    }

    private static final boolean notificationOnTop(TypedJFrame frame) {
	if (_frameStack.size() != 0 && _frameStack.getLast()._type.equals(FrameType.NOTIFICATION)) {
	    return true;
	}
	return false;
    }

    public static final void openAndDisposeOthers(final TypedJFrame frame) {
	System.err.println("Try to focus " + frame._type.toString());
	if (notificationOnTop(frame)) {
	    focusFrames();
	    return;
	}
	for (TypedJFrame jframe : _frameStack) {
	    jframe.dispose();
	    System.err.println("Disposed " + jframe._type.toString());
	}
	_frameStack.clear();
	System.err.println("Cleared all Frames in Stack");
	addFrame(frame);
    }

    public static final void addFrame(TypedJFrame frame) {
	System.err.println("Try to focus " + frame._type.toString());
	if (notificationOnTop(frame)) {
	    focusFrames();
	    return;
	}
	_frameStack.add(frame);
	frame.setVisible(true);
	frame.requestFocus();
	System.err.println("add " + frame._type.toString() + " to FrameStack.");
    }

    public static final void closeFrameOnTop(TypedJFrame frame) {
	if (_frameStack.getLast().equals(frame)) {
	    System.err.println(frame._type.toString() + " was opened on top");
	    _frameStack.removeLast().dispose();
	    System.err.println("Closed " + frame._type.toString() + "-Frame.");
	} else {
	    System.err.println("Tried to find " + frame._type.toString() + "-Frame to close it, but was not opened on top.");
	}
    }

    public static final void adjustFocus(TypedJFrame frame) {
	if (_frameStack.size() != 0) {
	    if (_frameStack.getLast().equals(frame)) {
		return;
	    }
	    _frameStack.getLast().setVisible(true);
	    _frameStack.getLast().requestFocus();
	}
    }

    public static final void minimizeAll() {
	for (TypedJFrame jframe : _frameStack) {
	    jframe.setVisible(false);
	    System.err.println("Set visible false to  " + jframe._type.toString());
	}
    }

    public static final void adjustFocusForMainFrame() {
	for (TypedJFrame jframe : _frameStack) {
	    jframe.setVisible(true);
	    System.err.println("Set visible true to  " + jframe._type.toString());
	}
    }
}
