package frontend;

import static backend.FrameManager.closeFrame;
import static backend.FrameManager.numbersOnly;
import static backend.FrameManager.showNotification;
import static backend.FrameManager.closeOnLeave;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.joda.time.DateTime;

import util.Preis;
import util.Try;
import backend.TypedJFrame;
import backend.enums.FrameType;
import database.entities.Eigenverbrauch;
import database.entities.Friseur;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.Maps;

public class EigenverbrauchFrame extends TypedJFrame {

    private static final long serialVersionUID = 7393495256404816114L;
    private JTextField _textField;
    private JComboBox<String> _friseurComboBox;
    private HashMap<Integer, Friseur> _friseurMapping = Maps.newHashMap();

    /**
     * Create the frame.
     */
    public EigenverbrauchFrame() {
	_type = FrameType.EIGENVERBRAUCH;
	addWindowFocusListener(closeOnLeave(this));
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	setResizable(false);
	setTitle("Eigenverbrauch");

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) Math.round(screenSize.getWidth());
	int height = (int) Math.round(screenSize.getHeight());

	int startingWidth = ((width / 2) - (317 / 2));
	int startingHeigth = ((height / 2) - (172 / 2));

	setBounds(startingWidth, startingHeigth, 317, 172);
	getContentPane().setLayout(null);
	setUndecorated(true);

	JButton speichernBtn = new JButton("Speichern");
	speichernBtn.addActionListener(saveEigenverbrauch());
	speichernBtn.setBounds(157, 126, 117, 29);
	getContentPane().add(speichernBtn);

	_friseurComboBox = new JComboBox<String>();
	_friseurComboBox.setBounds(157, 47, 117, 27);

	FluentIterable<Friseur> frisuere = FluentIterable.from(Friseur.loadByParameter("'1'", "1"));
	int index = 0;
	for (Friseur friseur : frisuere) {
	    _friseurMapping.put(index++, friseur);
	    _friseurComboBox.addItem(friseur.toString());
	}

	getContentPane().add(_friseurComboBox);

	JLabel eigenverbrauchLbl = new JLabel("Eigenverbrauch");
	eigenverbrauchLbl.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	eigenverbrauchLbl.setBounds(6, 6, 186, 29);
	getContentPane().add(eigenverbrauchLbl);

	JLabel friseurLbl = new JLabel("Friseur");
	friseurLbl.setFont(new Font("Lucida Grande", Font.BOLD, 18));
	friseurLbl.setBounds(6, 47, 117, 16);
	getContentPane().add(friseurLbl);

	JLabel lblX = new JLabel("X");
	lblX.addMouseListener(closeFrame(this));
	lblX.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	lblX.setHorizontalAlignment(SwingConstants.CENTER);
	lblX.setBounds(274, 12, 26, 16);
	getContentPane().add(lblX);

	JLabel lblPreis = new JLabel("Preis");
	lblPreis.setFont(new Font("Lucida Grande", Font.BOLD, 18));
	lblPreis.setBounds(6, 87, 61, 16);
	getContentPane().add(lblPreis);

	_textField = new JTextField();
	_textField.setBounds(157, 83, 117, 28);
	getContentPane().add(_textField);
	_textField.addKeyListener(numbersOnly());

	JLabel lblEur = new JLabel("EUR");
	lblEur.setBounds(274, 89, 31, 16);
	getContentPane().add(lblEur);
    }

    private ActionListener saveEigenverbrauch() {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent event) {
		boolean error = false;
		String message1 = "";
		String message2 = "";
		try {
		    Friseur friseur = _friseurMapping.get(_friseurComboBox.getSelectedIndex());
		    Preis preis = Preis.of(_textField.getText());
		    Try<Long> save = new Eigenverbrauch(friseur.getEntityId().get(), preis, DateTime.now()).save();
		    if (save.isSuccess()) {
			message1 = "Eigenverbrauch wurde";
			message2 = "erfolgreich gespeichert.";
		    }
		    if (save.isFailure()) {
			error=true;
			message1 = "Fehler beim speichern";
			message2 = "des Eigenverbrauchs.";
			System.err.println(save.failure().getMessage());
		    }
		} catch (Exception e) {
		    error = true;
		    message1 = "Eigenverbrauch konnte";
		    message2 = "nicht gespeichert werden.";
		    e.printStackTrace();
		}
		showNotification(error, message1, message2);
	    }
	};
    }
}
