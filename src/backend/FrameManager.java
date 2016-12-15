package backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

import database.entities.Kunde;
import database.entities.Ort;
import database.entities.Rezeptur;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;
import frontend.EigenverbrauchFrame;
import frontend.MainFrame;
import frontend.RezepturenFrame;
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
		if (!MainFrame._notificationKeeper._notification.isPresent()) {
		    KundenFrame kundenFrame = new KundenFrame();
		    kundenFrame.setVisible(true);
		    MainFrame._frameKeeper._openFrame = Optional.<TypedJFrame> of(kundenFrame);
		}
	    }
	};
    }

    public static final ActionListener openEigenverbrauch() {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (!MainFrame._notificationKeeper._notification.isPresent()) {
		    EigenverbrauchFrame eigenverbrauchFrame = new EigenverbrauchFrame();
		    eigenverbrauchFrame.setVisible(true);
		    MainFrame._frameKeeper._openFrame = Optional.<TypedJFrame> of(eigenverbrauchFrame);
		}
	    }
	};
    }

    public static final ActionListener openKasse(final JFrame frame) {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (!MainFrame._notificationKeeper._notification.isPresent()) {
		    KasseFrame kasseFrame = new KasseFrame();
		    kasseFrame.setVisible(true);
		    MainFrame._frameKeeper._openFrame = Optional.<TypedJFrame> of(kasseFrame);
		}
	    }
	};
    }

    public static final ActionListener closeNotificationByButton(final JFrame frame) {
	return new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		frame.dispose();
		MainFrame._notificationKeeper._notification = Optional.absent();
	    }
	};
    }

    public static final WindowFocusListener holdFocus(final TypedJFrame frame) {
	return new WindowFocusListener() {
	    public void windowGainedFocus(WindowEvent e) {
		System.err.println(frame._type.toString() + " window gained focus");
	    }

	    public void windowLostFocus(WindowEvent e) {
		System.err.println(frame._type.toString() + " window lost focus");
		frame.toFront();
		frame.requestFocus();
	    }
	};
    }

    public static final void showNotification(final boolean error, final String message1, final String message2) {
	Notification notification = new Notification(error, message1, message2);
	notification.setVisible(true);
	MainFrame._notificationKeeper._notification = Optional.<TypedJFrame> of(notification);
    }

    public static final MouseAdapter closeFrame(final JFrame frame) {
	return new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		frame.dispose();
	    }
	};
    }

    public static final MouseAdapter closeFrameAndOpenKundenFrame(final JFrame frame) {
	return new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		if (MainFrame._notificationKeeper._notification.isPresent()) {
		    MainFrame._notificationKeeper._notification = Optional.<TypedJFrame> absent();
		}
		KundenFrame kundenFrame = new KundenFrame();
		kundenFrame.setVisible(true);
		MainFrame._frameKeeper._openFrame = Optional.<TypedJFrame> of(kundenFrame);
		frame.dispose();
	    }
	};
    }

    public static final WindowFocusListener checkOtherOpenFrames() {
	return new WindowFocusListener() {
	    public void windowGainedFocus(WindowEvent e) {
		System.err.println("MainFrame window gained focus");
		if (MainFrame._notificationKeeper._notification.isPresent()) {
		    System.err.println("Notification window request focus");
		    MainFrame._notificationKeeper._notification.get().requestFocus();
		}
		if (MainFrame._frameKeeper._openFrame.isPresent()) {
		    System.err.println("Currently opened window closed");
		    MainFrame._frameKeeper._openFrame.get().dispose();
		    MainFrame._frameKeeper._openFrame = Optional.<TypedJFrame> absent();
		}
	    }

	    public void windowLostFocus(WindowEvent e) {
		System.err.println("MainFrame window lost focus");
	    }
	};
    }

    public static final void showKundeDeleteCheck(Kunde kunde) {
	KundeDeleteFrame kundeDeleteFrame = new KundeDeleteFrame(kunde);
	kundeDeleteFrame.setVisible(true);
	MainFrame._notificationKeeper._notification = Optional.<TypedJFrame> of(kundeDeleteFrame);
    }

    public static final void showKundeDataFrame(final Optional<Kunde> kunde, final KundenFrame kundenFrame) {
	if (!MainFrame._notificationKeeper._notification.isPresent()) {
	    if (MainFrame._frameKeeper._openFrame.isPresent()) {
		MainFrame._frameKeeper._openFrame = Optional.absent();
	    }
	    KundeDataFrame kundeDataFrame = new KundeDataFrame(kunde);
	    kundeDataFrame.setVisible(true);
	    kundenFrame.dispose();
	    MainFrame._frameKeeper._openFrame = Optional.<TypedJFrame> of(kundeDataFrame);
	}
    }

    public static final void showRezepturenFrame(Ort ort, Kunde kunde, FluentIterable<Rezeptur> rezepturen, KundenFrame kundenFrame) {
	if (!MainFrame._notificationKeeper._notification.isPresent()) {
	    if (MainFrame._frameKeeper._openFrame.isPresent()) {
		MainFrame._frameKeeper._openFrame = Optional.absent();
	    }
	    RezepturenFrame kundeDataFrame = new RezepturenFrame(ort, kunde, rezepturen);
	    kundeDataFrame.setVisible(true);
	    kundenFrame.dispose();
	    MainFrame._frameKeeper._openFrame = Optional.<TypedJFrame> of(kundeDataFrame);
	}
    }
}
