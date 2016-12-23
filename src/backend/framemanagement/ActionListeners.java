package backend.framemanagement;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import backend.TypedJFrame;
import database.entities.Kunde;
import datameer.com.google.common.base.Optional;
import frontend.kunde.KundeDataFrame;

public class ActionListeners {

    public static final ActionListener openAndDisposeOthers(final TypedJFrame frame) {
	return new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		FrameManager.openAndDisposeOthers(frame);
	    }
	};
    }

    public static final ActionListener closeFrameOnTop(final TypedJFrame frame) {
	return new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		FrameManager.closeFrameOnTop(frame);
	    }
	};
    }

    public static final ActionListener addFrame(final TypedJFrame frame) {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		FrameManager.addFrame(frame);
	    }
	};
    }

    public static final ActionListener addNewKundeDataFrame() {
	return new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		KundeDataFrame kundeDataFrame = new KundeDataFrame(Optional.<Kunde> absent());
		FrameManager.addFrame(kundeDataFrame);
	    }
	};
    }
}
