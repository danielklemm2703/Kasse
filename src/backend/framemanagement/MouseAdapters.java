package backend.framemanagement;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import backend.TypedJFrame;

public class MouseAdapters {
    public static final MouseAdapter closeFrame(final TypedJFrame frame) {
	return new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		FrameManager.closeFrameOnTop(frame);
	    }
	};
    }
}
