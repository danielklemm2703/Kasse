package backend.framemanagement;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

public class WindowListeners {
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
}
