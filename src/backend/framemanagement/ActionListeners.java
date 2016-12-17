package backend.framemanagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import backend.TypedJFrame;
import frontend.EigenverbrauchFrame;
import frontend.kasse.KasseFrame;
import frontend.kunde.KundenFrame;

public class ActionListeners {

    public static final ActionListener openKundeFrameListener = new ActionListener() {
	@Override
	public void actionPerformed(ActionEvent e) {
	    FrameManager.openAndDisposeOthers(new KundenFrame());
	}
    };

    public static final ActionListener openKasseFrameListener = new ActionListener() {
	@Override
	public void actionPerformed(ActionEvent e) {
	    FrameManager.openAndDisposeOthers(new KasseFrame());
	}
    };

    public static final ActionListener openAdminFrameListener = new ActionListener() {
	@Override
	public void actionPerformed(ActionEvent e) {
	    // TODO implement admin area
	}
    };

    public static final ActionListener openEigenverbrauchFrameListener = new ActionListener() {
	@Override
	public void actionPerformed(ActionEvent e) {
	    FrameManager.openAndDisposeOthers(new EigenverbrauchFrame());
	}
    };

    public static final ActionListener closeFrameOnTop(final TypedJFrame frame) {
	return new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		FrameManager.closeFrameOnTop(frame);
	    }
	};
    }

}
