package backend.framemanagement;

import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;

import backend.TypedJFrame;

public class WindowListeners {
    public static final WindowStateListener onMinimizeOrMaximizeMainFrame = new WindowStateListener() {
	    public void windowStateChanged(WindowEvent event) {
		// minimized
		if ((event.getNewState() & Frame.ICONIFIED) == Frame.ICONIFIED) {
		    System.err.println("minimized Main Frame");
		    FrameManager.minimizeAll();
		}
		// maximized
		else if ((event.getNewState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH) {
		    System.err.println("maximized Main Frame");
		    // do nothing, when main frame gets focus, it will build all
		    // active frames again
		}
	    }
	};

    public static final WindowFocusListener handleFramePrioritiesForMainFrame = new WindowFocusListener() {
	@Override
	public void windowLostFocus(WindowEvent e) {
	}

	@Override
	public void windowGainedFocus(WindowEvent e) {
	    FrameManager.adjustFocusForMainFrame();
	}
    };

    public static final WindowFocusListener handleFramePriorities(final TypedJFrame frame) {
	return new WindowFocusListener() {
	    @Override
	    public void windowLostFocus(WindowEvent e) {
	    }

	    @Override
	    public void windowGainedFocus(WindowEvent e) {
		FrameManager.adjustFocus(frame);
	    }
	};
    }
}
