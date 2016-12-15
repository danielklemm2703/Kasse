package frontend.kunde;

import static backend.FrameManager.closeFrameAndOpenKundenFrame;

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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import util.Try;
import backend.FrameManager;
import backend.TypedJFrame;
import backend.enums.FrameType;
import database.Ordering;
import database.entities.Kunde;
import database.entities.Ort;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.Maps;

public class KundeDataFrame extends TypedJFrame {

    private static final long serialVersionUID = 5968986682054442654L;
    private JPanel _contentPane;
    private JTextField _textField;
    private JTextField _textField_1;
    private JTextField _textField_3;
    private HashMap<Integer, Ort> _ortMapping = Maps.newHashMap();
    private JComboBox<String> _comboBox;

    /**
     * Create the frame.
     */
    public KundeDataFrame(final Optional<Kunde> kunde) {
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	_type = FrameType.KUNDENDATEN;

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) Math.round(screenSize.getWidth());
	int height = (int) Math.round(screenSize.getHeight());

	int startingWidth = ((width / 2) - (337 / 2));
	int startingHeigth = ((height / 2) - (228 / 2));

	setBounds(startingWidth, startingHeigth, 337, 228);
	_contentPane = new JPanel();
	_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(_contentPane);
	_contentPane.setLayout(null);

	JLabel lblKundenDaten = new JLabel("Kunden Daten");
	lblKundenDaten.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	lblKundenDaten.setBounds(6, 6, 151, 25);
	_contentPane.add(lblKundenDaten);

	JLabel lblVorname = new JLabel("Vorname: ");
	lblVorname.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	lblVorname.setBounds(16, 60, 93, 16);
	_contentPane.add(lblVorname);

	JLabel lblNachname = new JLabel("Nachname:");
	lblNachname.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	lblNachname.setBounds(16, 90, 107, 16);
	_contentPane.add(lblNachname);

	JLabel lblOrt = new JLabel("Ort:");
	lblOrt.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	lblOrt.setBounds(16, 120, 83, 16);
	_contentPane.add(lblOrt);

	JLabel lblTelefonnummer = new JLabel("Telefonnummer:");
	lblTelefonnummer.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	lblTelefonnummer.setBounds(16, 150, 141, 16);
	_contentPane.add(lblTelefonnummer);

	JButton btnNewButton = new JButton("Speichern");
	btnNewButton.addActionListener(saveKunde(kunde));
	btnNewButton.setBounds(92, 191, 117, 29);
	_contentPane.add(btnNewButton);

	JLabel lblX = new JLabel("X");
	lblX.addMouseListener(closeFrameAndOpenKundenFrame(this));
	lblX.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	lblX.setHorizontalAlignment(SwingConstants.CENTER);
	lblX.setBounds(300, 10, 29, 16);
	_contentPane.add(lblX);

	_textField = new JTextField();
	_textField.setBounds(165, 54, 134, 28);
	if (kunde.isPresent()) {
	    _textField.setText(kunde.get().getVorname());
	}
	_contentPane.add(_textField);
	_textField.setColumns(10);

	_textField_1 = new JTextField();
	_textField_1.setBounds(165, 85, 134, 28);
	if (kunde.isPresent()) {
	    _textField_1.setText(kunde.get().getNachname());
	}
	_contentPane.add(_textField_1);
	_textField_1.setColumns(10);

	_textField_3 = new JTextField();
	_textField_3.setBounds(165, 145, 134, 28);
	if (kunde.isPresent()) {
	    _textField_3.setText(kunde.get().getTelefon());
	}
	_contentPane.add(_textField_3);
	_textField_3.setColumns(10);

	_comboBox = new JComboBox<String>();
	_comboBox.setBounds(165, 117, 134, 27);
	Iterable<Ort> loadByParameter = Ort.loadByParameter("'1'", "1", new Ordering("ORT_NAME", "ASC"));
	int index = 0;
	int selectedIndex = 0;
	for (Ort ort : loadByParameter) {
	    if (kunde.isPresent() && kunde.get().getOrtId() == ort.getEntityId().get()) {
		selectedIndex = index;
	    }
	    _ortMapping.put(index++, ort);
	    _comboBox.addItem(ort.getOrtName());
	}
	_comboBox.setSelectedIndex(selectedIndex);
	_contentPane.add(_comboBox);
	setUndecorated(true);
    }

    private ActionListener saveKunde(final Optional<Kunde> kunde) {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (_textField.getText().isEmpty() || _textField_1.getText().isEmpty() || _textField_3.getText().isEmpty()) {
		    FrameManager.showNotification(true, "Es müssen alle", "Felder gefüllt sein!");
		    return;
		}
		if (_comboBox.getSelectedIndex() == -1) {
		    FrameManager.showNotification(true, "Es muss ein Ort", "gewählt werden!");
		    return;
		}
		String vorname = _textField.getText();
		String nachname = _textField_1.getText();
		String telefon = _textField_3.getText();
		long ortId = _ortMapping.get(_comboBox.getSelectedIndex()).getEntityId().get();
		Try<Long> save;
		if (!kunde.isPresent()) {
		    save = new Kunde(vorname, nachname, ortId, telefon).save();
		} else {
		    kunde.get().setNachname(nachname);
		    kunde.get().setVorname(vorname);
		    kunde.get().setOrtId(ortId);
		    kunde.get().setTelefon(telefon);
		    save = kunde.get().save();
		}

		if (save.isSuccess()) {
		    FrameManager.showNotification(false, "Kunde wurde", "erfolgreich gespeichert");
		} else {
		    FrameManager.showNotification(true, "Kunde konnte nicht", "gespeichert werden");
		}
	    }
	};
    }
}
