package frontend.kasse;
import static backend.FrameManager.closeFrameMouseAdapter;
import static backend.FrameManager.showKasseDienstleisungChoserFrame;
import static backend.FrameManager.showNotification;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import util.PopupTriggerListener;
import backend.TypedJFrame;
import backend.enums.FrameType;
import database.Ordering;
import database.entities.Friseur;
import database.entities.Kunde;
import database.entities.Ort;
import datameer.com.google.common.base.Function;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.Maps;

public class KasseFrame extends TypedJFrame {

    private static final long serialVersionUID = -1513580225986538916L;
    private JTextField txtGutscheinCode;
    private JComboBox<String> _friseurComboBox;
    private HashMap<Integer, Friseur> _friseurMapping = Maps.newHashMap();
    private JComboBox<String> _kundeComboBox;
    private HashMap<Integer, Kunde> _kundeMapping = Maps.newHashMap();
    private JCheckBox _chckbxLaufkunde;
    private JPopupMenu _dienstleistungPopUpMenu;
    private JPopupMenu _verkaufPopUpMenu;

    private final ActionListener _laufKundeActionListener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    if (_chckbxLaufkunde.isSelected()) {
		_kundeComboBox.setEnabled(false);
	    } else {
		_kundeComboBox.setEnabled(true);
	    }
	}
    };

    private final ActionListener _diensleistungChoser = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    Friseur friseur = _friseurMapping.get(_friseurComboBox.getSelectedIndex());
	    Optional<Kunde> kunde = Optional.absent();
	    if (!_chckbxLaufkunde.isSelected()) {
		kunde = Optional.fromNullable(_kundeMapping.get(_kundeComboBox.getSelectedIndex()));
	    }
	    if (friseur == null) {
		showNotification(true, "Ein Friseur muss", "ausgewählt sein");
	    } else {
		showKasseDienstleisungChoserFrame(kunde, friseur);
	    }
	}
    };

    private final ActionListener _verkaufChoser = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    System.out.println("Menu item Test1");
	}
    };

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
	lblX.addMouseListener(closeFrameMouseAdapter(this));
	lblX.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
	lblX.setBounds(571, 12, 13, 16);
	getContentPane().add(lblX);

	_dienstleistungPopUpMenu = new JPopupMenu("Dienstleistungen");
	JMenuItem itemDL1 = new JMenuItem("Hinzufügen");
	itemDL1.addActionListener(_diensleistungChoser);
	JMenuItem itemDL2 = new JMenuItem("Entfernen");
	// TODO delete action listener
	// itemDL2.addActionListener();
	_dienstleistungPopUpMenu.add(itemDL1);
	_dienstleistungPopUpMenu.add(itemDL2);

	JScrollPane scrollPaneDienstleistungen = new JScrollPane();
	scrollPaneDienstleistungen.addMouseListener(new PopupTriggerListener(_dienstleistungPopUpMenu));
	scrollPaneDienstleistungen.setBounds(25, 130, 559, 179);
	getContentPane().add(scrollPaneDienstleistungen);

	_verkaufPopUpMenu = new JPopupMenu("Verkäufe");
	JMenuItem itemVK1 = new JMenuItem("Hinzufügen");
	itemVK1.addActionListener(_verkaufChoser);
	JMenuItem itemVK2 = new JMenuItem("Entfernen");
	// TODO delete action listener
	// itemVK2.addActionListener();
	_verkaufPopUpMenu.add(itemVK1);
	_verkaufPopUpMenu.add(itemVK2);

	JScrollPane scrollPaneVerkaeufe = new JScrollPane();
	scrollPaneVerkaeufe.addMouseListener(new PopupTriggerListener(_verkaufPopUpMenu));
	scrollPaneVerkaeufe.setBounds(25, 344, 559, 179);
	getContentPane().add(scrollPaneVerkaeufe);

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
	Optional<Ort> ort;
	for (Kunde kunde : kunden) {
	    _kundeMapping.put(index++, kunde);
	    ort = Ort.loadById(kunde.getOrtId());
	    String ortString = ort.transform(new Function<Ort, String>() {
		@Override
		public String apply(Ort input) {
		    return "(" + input.getOrtName() + ")";
		}
	    }).or("");
	    _kundeComboBox.addItem(kunde.getNachname() + ", " + kunde.getVorname() + " " + ortString);
	}
	getContentPane().add(_kundeComboBox);

	_chckbxLaufkunde = new JCheckBox("Laufkunde");
	_chckbxLaufkunde.addActionListener(_laufKundeActionListener);
	_chckbxLaufkunde.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
	_chckbxLaufkunde.setBounds(413, 73, 128, 23);
	getContentPane().add(_chckbxLaufkunde);

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
    }
}
