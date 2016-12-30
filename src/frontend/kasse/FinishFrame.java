package frontend.kasse;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextPane;

import backend.TypedJFrame;
import backend.framemanagement.ActionListeners;
import database.entities.Gutschein;
import database.entities.Rezeptur;
import database.enums.FrameType;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;

public class FinishFrame extends TypedJFrame {

    private static final long serialVersionUID = 6060625047333936877L;

    public FinishFrame(FluentIterable<Gutschein> createdGutscheine, Optional<Gutschein> usedGutschein, FluentIterable<Rezeptur> rezepturenToEnter) {
	// TODO last move was here
	// R I P
	_type = FrameType.KASSE_FINISH;
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	setResizable(false);
	setTitle("Erfolgreich Abgeschlossen");
	getContentPane().setLayout(null);

	FluentIterable<String> texts = FluentIterable.from(ImmutableList.<String> of());

	if (usedGutschein.isPresent()) {
	    if (usedGutschein.get().getRestWert().value() > 0) {
		String restwert = String.format("Eingelöster Gutschein '%s' hat noch %s restwert.", usedGutschein.get().getTransaktionId(), usedGutschein.get()
			.getRestWert().toString());
		texts = texts.append(restwert);
	    } else {
		String aufgebraucht = String.format("Eingelöster Gutschein '%s' wurde komplett aufgebraucht.", usedGutschein.get().getTransaktionId());
		texts = texts.append(aufgebraucht);
	    }
	}
	if (!createdGutscheine.isEmpty()) {

	}
	if (rezepturenToEnter.isEmpty()) {

	}

	String text1 = texts.get(0);
	// TODO show in separate frame:
	// if gutschein was bought + wert & id
	// if gutschein was used and its restwert +id
	// if rezepturen need to be entered

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) Math.round(screenSize.getWidth());
	int height = (int) Math.round(screenSize.getHeight());

	int startingWidth = ((width / 2) - (440 / 2));
	int startingHeigth = ((height / 2) - (353 / 2));

	setBounds(startingWidth, startingHeigth, 440, 353);
	getContentPane().setLayout(null);

	JLabel lblErfolgreichAbgeschlossen = new JLabel("Erfolgreich Abgeschlossen");
	lblErfolgreichAbgeschlossen.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
	lblErfolgreichAbgeschlossen.setBounds(86, 6, 261, 25);
	getContentPane().add(lblErfolgreichAbgeschlossen);

	JButton btnOk = new JButton("OK");
	btnOk.addActionListener(ActionListeners.closeFrameOnTop(this));
	btnOk.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	btnOk.setBounds(154, 318, 117, 29);
	getContentPane().add(btnOk);

	JTextPane txtNotifications = new JTextPane();
	txtNotifications.setText(text1 + "\n LOL");
	txtNotifications.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	txtNotifications.setBounds(33, 43, 379, 159);
	getContentPane().add(txtNotifications);

	setUndecorated(true);
    }
}
