package frontend;

import static backend.FrameManager.closeFrameAndOpenKundenFrame;
import static backend.FrameManager.showNotification;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.joda.time.DateTime;

import backend.TypedJFrame;
import backend.enums.FrameType;
import database.entities.Farbe;
import database.entities.Kunde;
import database.entities.Ort;
import database.entities.Praeparat;
import database.entities.Rezeptur;
import database.entities.Transaktion;
import database.entities.Wickel;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;

public class RezepturenFrame extends TypedJFrame {

    private static final long serialVersionUID = 2931432891689129660L;
    private JPanel _contentPane;
    private JTable _table;
    private JLabel _pageText;
    private JButton _btnZurueck;
    private JButton _btnVor;
    private JLabel _datumText;
    private JLabel _ergebnisText;
    private JLabel _wickelstaerkeText;
    private JLabel _wickelTypText;
    private JLabel _wickelfarbeText;
    private JLabel _einwirkzeitText;
    private JLabel _praeparatText;
    private JLabel _wärmeText;
    private int _currentPage;
    private FluentIterable<Rezeptur> _rezepturen;

    /**
     * Create the frame.
     */
    public RezepturenFrame(final Ort ort, final Kunde kunde, final FluentIterable<Rezeptur> rezepturen) {
	_rezepturen = rezepturen;
	_currentPage = 0;
	_type = FrameType.REZEPTUREN;
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setResizable(false);
	setTitle("Kunden");
	setUndecorated(true);

	// setBounds(100, 100, 606, 418);

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) Math.round(screenSize.getWidth());
	int height = (int) Math.round(screenSize.getHeight());

	int startingWidth = ((width / 2) - (606 / 2));
	int startingHeigth = ((height / 2) - (418 / 2));

	setBounds(startingWidth, startingHeigth, 606, 418);

	_contentPane = new JPanel();
	_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(_contentPane);
	_contentPane.setLayout(null);

	JLabel lblRezepturen = new JLabel("Rezepturen");
	lblRezepturen.setBounds(5, 5, 125, 25);
	lblRezepturen.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	_contentPane.add(lblRezepturen);

	JLabel label = new JLabel("X");
	label.addMouseListener(closeFrameAndOpenKundenFrame(this));
	label.setHorizontalAlignment(SwingConstants.CENTER);
	label.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	label.setBounds(560, 8, 26, 18);
	_contentPane.add(label);

	JLabel lblName = new JLabel("Name:");
	lblName.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	lblName.setBounds(5, 35, 61, 16);
	_contentPane.add(lblName);

	JLabel lblOrt = new JLabel("Ort:");
	lblOrt.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	lblOrt.setBounds(5, 54, 61, 16);
	_contentPane.add(lblOrt);

	JLabel lblDatum = new JLabel("Datum:");
	lblDatum.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	lblDatum.setBounds(5, 73, 61, 16);
	_contentPane.add(lblDatum);

	_pageText = new JLabel("");
	_pageText.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	_pageText.setBounds(125, 7, 87, 21);
	_contentPane.add(_pageText);

	JLabel lblFarbe = new JLabel("Farbe:");
	lblFarbe.setFont(new Font("Lucida Grande", Font.BOLD, 16));
	lblFarbe.setBounds(5, 110, 66, 16);
	_contentPane.add(lblFarbe);

	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setBounds(15, 138, 571, 91);
	getContentPane().add(scrollPane);
	_table = new JTable(createEmptyFarbenModel());
	_table.setShowHorizontalLines(true);
	_table.setShowVerticalLines(true);
	_table.setGridColor(Color.BLACK);
	scrollPane.setViewportView(_table);

	JLabel lblWickel = new JLabel("Wickel:");
	lblWickel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
	lblWickel.setBounds(5, 241, 61, 16);
	_contentPane.add(lblWickel);

	JLabel lblEinwirkzeit = new JLabel("Einwirkzeit:");
	lblEinwirkzeit.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	lblEinwirkzeit.setBounds(268, 258, 103, 25);
	_contentPane.add(lblEinwirkzeit);

	JLabel lblNewLabel = new JLabel("Wickeltyp:");
	lblNewLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	lblNewLabel.setBounds(15, 258, 87, 25);
	_contentPane.add(lblNewLabel);

	JLabel lblWickelstrke = new JLabel("Wickelstärke:");
	lblWickelstrke.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	lblWickelstrke.setBounds(15, 280, 103, 25);
	_contentPane.add(lblWickelstrke);

	JLabel lblWickelfarbe = new JLabel("Wickelfarbe:");
	lblWickelfarbe.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	lblWickelfarbe.setBounds(15, 302, 103, 25);
	_contentPane.add(lblWickelfarbe);

	JLabel lblPrparat = new JLabel("Präparat:");
	lblPrparat.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	lblPrparat.setBounds(268, 280, 87, 25);
	_contentPane.add(lblPrparat);

	JLabel lblWrme = new JLabel("Wärme:");
	lblWrme.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	lblWrme.setBounds(268, 302, 61, 25);
	_contentPane.add(lblWrme);

	JLabel lblEergebnis = new JLabel("Ergebnis:");
	lblEergebnis.setFont(new Font("Lucida Grande", Font.BOLD, 16));
	lblEergebnis.setBounds(5, 334, 87, 25);
	_contentPane.add(lblEergebnis);

	_ergebnisText = new JLabel("war jut");
	_ergebnisText.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	_ergebnisText.setBounds(15, 357, 553, 25);
	_contentPane.add(_ergebnisText);

	JLabel nameText = new JLabel(kunde.getVorname() + " " + kunde.getNachname());
	nameText.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	nameText.setBounds(69, 31, 484, 25);
	_contentPane.add(nameText);

	JLabel ortText = new JLabel(ort.getOrtName());
	ortText.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	ortText.setBounds(69, 50, 484, 25);
	_contentPane.add(ortText);

	_datumText = new JLabel("");
	_datumText.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	_datumText.setBounds(69, 69, 484, 25);
	_contentPane.add(_datumText);

	_wickelstaerkeText = new JLabel("");
	_wickelstaerkeText.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	_wickelstaerkeText.setBounds(125, 280, 131, 25);
	_contentPane.add(_wickelstaerkeText);

	_wickelTypText = new JLabel("");
	_wickelTypText.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	_wickelTypText.setBounds(125, 258, 131, 25);
	_contentPane.add(_wickelTypText);

	_wickelfarbeText = new JLabel("");
	_wickelfarbeText.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	_wickelfarbeText.setBounds(125, 302, 131, 25);
	_contentPane.add(_wickelfarbeText);

	_einwirkzeitText = new JLabel("");
	_einwirkzeitText.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	_einwirkzeitText.setBounds(367, 258, 131, 25);
	_contentPane.add(_einwirkzeitText);

	_praeparatText = new JLabel("");
	_praeparatText.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	_praeparatText.setBounds(367, 280, 131, 25);
	_contentPane.add(_praeparatText);

	_wärmeText = new JLabel("");
	_wärmeText.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
	_wärmeText.setBounds(367, 302, 131, 25);
	_contentPane.add(_wärmeText);

	_btnVor = new JButton("Neuere");
	_btnVor.addActionListener(changeRezepte(true));
	_btnVor.setFont(new Font("Lucida Grande", Font.BOLD, 16));
	_btnVor.setBounds(15, 384, 117, 29);
	_contentPane.add(_btnVor);

	_btnZurueck = new JButton("Ältere");
	_btnZurueck.addActionListener(changeRezepte(false));
	_btnZurueck.setFont(new Font("Lucida Grande", Font.BOLD, 16));
	_btnZurueck.setBounds(469, 384, 117, 29);
	_contentPane.add(_btnZurueck);

	fillPage(rezepturen.get(_currentPage), _currentPage, _rezepturen.size());
    }

    private ActionListener changeRezepte(final boolean newer) {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		if (newer) {
		    _currentPage--;
		} else {
		    _currentPage++;
		}
		fillPage(_rezepturen.get(_currentPage), _currentPage, _rezepturen.size());
	    }
	};
    }

    private final void fillPage(Rezeptur rezeptur, int index, int size) {
	_pageText.setText("(" + (index + 1) + "/" + size + ")");
	_btnVor.setEnabled(true);
	_btnZurueck.setEnabled(true);
	if (index == 0) {
	    // first page, disable left button
	    _btnVor.setEnabled(false);
	}
	if (index == (size - 1)) {
	    // last page, disable right button
	    _btnZurueck.setEnabled(false);
	}
	Optional<Transaktion> transaktion = Transaktion.loadById(rezeptur.getTransaktionId());
	if (!transaktion.isPresent()) {
	    showNotification(true, "Rezeptur konnte nicht", "geladen werden");
	}
	DateTime datum = transaktion.get().getDatum();
	_datumText.setText(datum.toString("dd. MMMM yyyy"));

	fillWickel(rezeptur.getWickelId(), _wickelstaerkeText, _wickelTypText, _wickelfarbeText, _einwirkzeitText, _praeparatText, _wärmeText);

	fillFarbe(rezeptur.getFarbIds(), _table);
	_ergebnisText.setText(rezeptur.getErgebnis());

    }

    private static final void fillFarbe(Optional<FluentIterable<Long>> farbIds, JTable table) {
	DefaultTableModel model = createEmptyFarbenModel();
	if (!farbIds.isPresent()) {
	    table.setModel(model);
	    return;
	}
	if (farbIds.get().size() == 0) {
	    table.setModel(model);
	    return;
	}
	if (!farbIds.get().first().isPresent()) {
	    table.setModel(model);
	    return;
	}
	if (farbIds.get().first().get() == -1) {
	    table.setModel(model);
	    return;
	}
	for (Long farbId : farbIds.get()) {
	    Optional<Farbe> farbe = Farbe.loadById(farbId);
	    if (farbe.isPresent()) {
		Optional<Praeparat> praeparat = Praeparat.loadById(farbe.get().getPraeparatId());
		model.addRow(new Object[] { praeparat.isPresent() ? praeparat.get().getName() : "nicht gefunden", farbe.get().getFaerbeTechnik().name(),
			farbe.get().getFarbe(), farbe.get().getOxyd() });
	    }
	}
	table.setModel(model);
    }

    private static final void fillWickel(Optional<Long> wickelId, JLabel wickelstaerkeText, JLabel wickelTypText, JLabel wickelfarbeText,
	    JLabel einwirkzeitText, JLabel praeparatText, JLabel wärmeText) {
	if (!wickelId.isPresent()) {
	    fillempty(wickelstaerkeText, wickelTypText, wickelfarbeText, einwirkzeitText, praeparatText, wärmeText);
	    return;
	}
	Optional<Wickel> wickel = Wickel.loadById(wickelId.get());
	if (!wickel.isPresent()) {
	    fillempty(wickelstaerkeText, wickelTypText, wickelfarbeText, einwirkzeitText, praeparatText, wärmeText);
	    return;
	}
	wickelTypText.setText(wickel.get().getWickelTyp().name());
	wickelstaerkeText.setText(wickel.get().getWickelstaerke().name());
	wickelfarbeText.setText(wickel.get().getWickelFarbe());
	einwirkzeitText.setText(wickel.get().getEinwirkZeit() + "min");
	wärmeText.setText(wickel.get().isWaerme() ? "Ja" : "Nein");
	Optional<Praeparat> praeparat = Praeparat.loadById(wickel.get().getPraeparatId());
	if (praeparat.isPresent()) {
	    praeparatText.setText(praeparat.get().getName());
	} else {
	    praeparatText.setText("nicht gefunden");
	}
    }

    private static final void fillempty(JLabel wickelstaerkeText, JLabel wickelTypText, JLabel wickelfarbeText, JLabel einwirkzeitText, JLabel praeparatText,
	    JLabel wärmeText) {
	wickelstaerkeText.setText("");
	wickelTypText.setText("");
	wickelfarbeText.setText("");
	einwirkzeitText.setText("");
	praeparatText.setText("");
	wärmeText.setText("");
    }

    private static final DefaultTableModel createEmptyFarbenModel() {
	DefaultTableModel model = new DefaultTableModel();
	model.addColumn("Präparat");
	model.addColumn("Färbetechnik");
	model.addColumn("Farbe");
	model.addColumn("Oxyd");
	return model;
    }
}
