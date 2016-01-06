package backend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

import database.entities.Kunde;
import datameer.com.google.common.base.Optional;
import frontend.EigenverbrauchFrame;
import frontend.KundeDataFrame;
import frontend.KundeDeleteFrame;
import frontend.KundenFrame;
import frontend.MainFrame;
import frontend.Notification;

public class FrameManager {
    public static final ActionListener openAdmin(final JFrame frame) {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		// TODO
	    }
	};
    }

    public static final ActionListener openKunden(final JFrame frame) {
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

    public static final ActionListener openEigenverbrauch(final JFrame frame) {
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
		// TODO
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

    public static final KeyAdapter numbersOnly() {
	return new KeyAdapter() {
	    private boolean dot = false;

	    public void keyTyped(KeyEvent e) {
		char caracter = e.getKeyChar();
		if (dot) {
		    if (((caracter < '0') || (caracter > '9')) && (caracter != '\b')) {
			e.consume();
		    }
		} else if (((caracter < '0') || (caracter > '9')) && (caracter != '\b') && (caracter != '.') && (caracter != ',')) {
		    e.consume();
		}
		if (caracter == '.' || caracter == ',') {
		    dot = true;
		}
	    }
	};
    }

    public static final MouseAdapter closeFrame(final JFrame frame) {
	return new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
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
		    System.err.println("Open window closed");
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
}
