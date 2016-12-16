package backend;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;

import database.entities.Friseur;
import database.entities.Kunde;
import database.entities.Ort;
import database.entities.Rezeptur;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;
import frontend.EigenverbrauchFrame;
import frontend.RezepturenFrame;
import frontend.kasse.DienstleisungChoserFrame;
import frontend.kasse.KasseFrame;
import frontend.kunde.KundeDataFrame;
import frontend.kunde.KundeDeleteFrame;
import frontend.kunde.KundenFrame;
import frontend.util.Notification;

public class FrameManager {
    // TODO Planning List:
    // first code formatting dm
    // 1. design kasse frame
    // 2. design how to enter and create rezepturen
    // 3. design kollegen umsatz fenster stuff
    // 4. design admin area
    // BUG: Opened error -> minimize main window -Y maximize it, nothing is
    // clickable and error is gone

    public static final ActionListener openAdmin(final JFrame frame) {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		// TODO implement admin area
	    }
	};
    }

    public static final ActionListener openKunden() {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		KundenFrame kundenFrame = new KundenFrame();
		FrameStack.openAndDisposeOthers(kundenFrame);
	    }
	};
    }

    public static final ActionListener openEigenverbrauch() {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		EigenverbrauchFrame eigenverbrauchFrame = new EigenverbrauchFrame();
		FrameStack.openAndDisposeOthers(eigenverbrauchFrame);
	    }
	};
    }

    public static final ActionListener openKasse(final JFrame frame) {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		KasseFrame kasseFrame = new KasseFrame();
		FrameStack.openAndDisposeOthers(kasseFrame);
	    }
	};
    }

    public static final ActionListener closeNotificationByButton(final TypedJFrame frame) {
	return new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		FrameStack.closeFrameOnTop(frame);
	    }
	};
    }

    // public static final WindowFocusListener holdFocus(final TypedJFrame
    // frame) {
    // return new WindowFocusListener() {
    // public void windowGainedFocus(WindowEvent e) {
    // System.err.println(frame._type.toString() + " window gained focus");
    // }
    //
    // public void windowLostFocus(WindowEvent e) {
    // System.err.println(frame._type.toString() + " window lost focus");
    // frame.toFront();
    // frame.requestFocus();
    // }
    // };
    // }

    public static final void showNotification(final boolean error, final String message1, final String message2) {
	Notification notification = new Notification(error, message1, message2);
	FrameStack.addFrame(notification);
    }

    public static final MouseAdapter closeFrameMouseAdapter(final TypedJFrame frame) {
	return new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		FrameStack.closeFrameOnTop(frame);
	    }
	};
    }

    public static final void closeFrame(final TypedJFrame frame) {
	FrameStack.closeFrameOnTop(frame);
    }

    public static final MouseAdapter closeFrameAndOpenKundenFrame(final TypedJFrame frame) {
	return new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		FrameStack.closeFrameOnTop(frame);
	    }
	};
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

    public static final void showKundeDeleteCheck(Kunde kunde) {
	KundeDeleteFrame kundeDeleteFrame = new KundeDeleteFrame(kunde);
	FrameStack.addFrame(kundeDeleteFrame);
    }

    public static final void showKundeDataFrame(final Optional<Kunde> kunde, final KundenFrame kundenFrame) {
	KundeDataFrame kundeDataFrame = new KundeDataFrame(kunde);
	FrameStack.addFrame(kundeDataFrame);
    }

    public static final void showKasseDienstleisungChoserFrame(final Optional<Kunde> kunde, final Friseur friseur) {
	DienstleisungChoserFrame dienstleisungChoserFrame = new DienstleisungChoserFrame(kunde, friseur);
	FrameStack.addFrame(dienstleisungChoserFrame);
    }

    public static final void showRezepturenFrame(Ort ort, Kunde kunde, FluentIterable<Rezeptur> rezepturen) {
	RezepturenFrame kundeDataFrame = new RezepturenFrame(ort, kunde, rezepturen);
	FrameStack.addFrame(kundeDataFrame);
    }
}
