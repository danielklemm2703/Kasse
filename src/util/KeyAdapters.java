package util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyAdapters {
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
}
