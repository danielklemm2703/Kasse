package backend.framemanagement;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;

import backend.TypedJFrame;

public class FrameManager {
    // TODO Planning List:
    // first code formatting dm
    // 1. design kasse frame
    // 2. design how to enter and create rezepturen
    // 3. design kollegen umsatz fenster stuff
    // 4. design admin area
    // BUG: Opened error -> minimize main window -Y maximize it, nothing is
    // clickable and error is gone


    public static final void closeFrameOnTop(final TypedJFrame frame) {
	FrameStack.closeFrameOnTop(frame);
    }

    public static final void openAndDisposeOthers(final TypedJFrame frame) {
	FrameStack.openAndDisposeOthers(frame);
    }

    public static final void addFrame(final TypedJFrame frame) {
	FrameStack.addFrame(frame);
    }

    public static final WindowStateListener onMinimize() {
	// TODO try to make all other frames invisible and restore them on
	// maximize and gain focus events
	return new WindowStateListener() {
	    public void windowStateChanged(WindowEvent event) {
		if ((event.getNewState() & Frame.ICONIFIED) == Frame.ICONIFIED) {
		    System.err.println("minimized main Frame");
			System.err.println("Notification window closed");
			System.err.println("Open window closed");
		}
	    }
	};
    }

    public static final WindowFocusListener checkOtherOpenFrames() {
	// TODO check if other frames are open and have priority
	return new WindowFocusListener() {
	    public void windowGainedFocus(WindowEvent e) {
		System.err.println("MainFrame window gained focus");
		    System.err.println("Notification window request focus");
		    System.err.println("Currently opened window closed");
	    }

	    public void windowLostFocus(WindowEvent e) {
		System.err.println("MainFrame window lost focus");
	    }
	};
    }
}
