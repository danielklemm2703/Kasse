package frontend;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import backend.TypedJFrame;
import backend.enums.FrameType;
import database.Ordering;
import database.entities.Friseur;
import database.entities.Kunde;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.Maps;

public class KasseFrame extends TypedJFrame {

    private static final long serialVersionUID = -1513580225986538916L;
    private JTextField txtGutscheinCode;
    private JComboBox<String> _friseurComboBox;
    private HashMap<Integer, Friseur> _friseurMapping = Maps.newHashMap();
    private JComboBox<String> _kundeComboBox;
    private HashMap<Integer, Kunde> _kundeMapping = Maps.newHashMap();

    public KasseFrame() {
	_type = FrameType.KASSE;
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	setResizable(false);
	setTitle("Kasse");
	getContentPane().setLayout(null);

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) Math.round(screenSize.getWidth());
	int height = (int) Math.round(screenSize.getHeight());

	int startingWidth = ((width / 2) - (612 / 2));
	int startingHeigth = ((height / 2) - (716 / 2));

	setBounds(startingWidth, startingHeigth, 612, 716);
	getContentPane().setLayout(null);
	setUndecorated(true);

	JLabel lblKasse = new JLabel("Kasse");
	lblKasse.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	lblKasse.setBounds(228, 6, 70, 29);
	getContentPane().add(lblKasse);

	JLabel lblX = new JLabel("X");
	lblX.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
	lblX.setBounds(571, 12, 13, 16);
	getContentPane().add(lblX);

	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setBounds(25, 130, 559, 179);
	getContentPane().add(scrollPane);

	JScrollPane scrollPane_1 = new JScrollPane();
	scrollPane_1.setBounds(25, 344, 559, 179);
	getContentPane().add(scrollPane_1);

	JButton btnZurcksetzen = new JButton("Zurücksetzen");
	btnZurcksetzen.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	btnZurcksetzen.setBounds(25, 681, 152, 29);
	getContentPane().add(btnZurcksetzen);

	JButton btnAbschlieen = new JButton("Abschließen");
	btnAbschlieen.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	btnAbschlieen.setBounds(454, 681, 130, 29);
	getContentPane().add(btnAbschlieen);

	JLabel lblGesamt = new JLabel("Gesamt");
	lblGesamt.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	lblGesamt.setBounds(228, 568, 102, 16);
	getContentPane().add(lblGesamt);

	JLabel lblGutscheinEnlsen = new JLabel("Gutschein Enlösen");
	lblGutscheinEnlsen.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	lblGutscheinEnlsen.setBounds(228, 596, 177, 16);
	getContentPane().add(lblGutscheinEnlsen);

	txtGutscheinCode = new JTextField();
	txtGutscheinCode.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	txtGutscheinCode.setBounds(411, 593, 130, 26);
	getContentPane().add(txtGutscheinCode);
	txtGutscheinCode.setColumns(10);

	JLabel lblZuZahlen = new JLabel("Zu Zahlen");
	lblZuZahlen.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	lblZuZahlen.setBounds(228, 652, 101, 16);
	getContentPane().add(lblZuZahlen);

	JLabel lblGutscheinInfo = new JLabel("Gutschein Info");
	lblGutscheinInfo.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	lblGutscheinInfo.setBounds(228, 624, 144, 16);
	getContentPane().add(lblGutscheinInfo);

	JScrollPane scrollPane_2 = new JScrollPane();
	scrollPane_2.setBounds(25, 566, 152, 103);
	getContentPane().add(scrollPane_2);

	JLabel lblKundenInfo = new JLabel("Kunden Info");
	lblKundenInfo.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
	lblKundenInfo.setBounds(25, 546, 101, 16);
	getContentPane().add(lblKundenInfo);

	JLabel lblDienstleistungen = new JLabel("Dienstleistungen");
	lblDienstleistungen.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	lblDienstleistungen.setBounds(25, 102, 158, 26);
	getContentPane().add(lblDienstleistungen);

	JLabel lblVerkufe = new JLabel("Verkäufe");
	lblVerkufe.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	lblVerkufe.setBounds(27, 321, 89, 16);
	getContentPane().add(lblVerkufe);

	JLabel lblFriseur = new JLabel("Friseur");
	lblFriseur.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	lblFriseur.setBounds(25, 44, 61, 16);
	getContentPane().add(lblFriseur);

	JLabel lblKunde = new JLabel("Kunde");
	lblKunde.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	lblKunde.setBounds(25, 76, 61, 16);
	getContentPane().add(lblKunde);

	_friseurComboBox = new JComboBox<String>();
	_friseurComboBox.setBounds(177, 42, 213, 27);

	Ordering ordering = new Ordering("FRISEUR_NAME", "ASC");
	FluentIterable<Friseur> frisuere = FluentIterable.from(Friseur.loadByParameter("'1'", "1", ordering));
	int index = 0;
	for (Friseur friseur : frisuere) {
	    _friseurMapping.put(index++, friseur);
	    _friseurComboBox.addItem(friseur.toString());
	}
	getContentPane().add(_friseurComboBox);

	_kundeComboBox = new JComboBox<String>();
	_kundeComboBox.setBounds(177, 74, 213, 27);

	Ordering kundeOrdering = new Ordering("NACHNAME", "ASC");
	FluentIterable<Kunde> kunden = FluentIterable.from(Kunde.loadByParameter("'1'", "1", kundeOrdering));
	index = 0;
	for (Kunde kunde : kunden) {
	    _kundeMapping.put(index++, kunde);
	    _kundeComboBox.addItem(kunde.getNachname() + ", " + kunde.getVorname());
	}
	getContentPane().add(_kundeComboBox);

	JCheckBox chckbxLaufkunde = new JCheckBox("Laufkunde");
	chckbxLaufkunde.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
	chckbxLaufkunde.setBounds(413, 73, 128, 23);
	getContentPane().add(chckbxLaufkunde);

	JLabel lblGesamtWert = new JLabel("");
	lblGesamtWert.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	lblGesamtWert.setBounds(411, 570, 130, 16);
	getContentPane().add(lblGesamtWert);

	JLabel lblGutscheinInfoWert = new JLabel("");
	lblGutscheinInfoWert.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	lblGutscheinInfoWert.setBounds(411, 626, 130, 16);
	getContentPane().add(lblGutscheinInfoWert);

	JLabel lblZuZahlenWert = new JLabel("");
	lblZuZahlenWert.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	lblZuZahlenWert.setBounds(411, 654, 130, 16);
	getContentPane().add(lblZuZahlenWert);
	setUndecorated(true);
    }
}
