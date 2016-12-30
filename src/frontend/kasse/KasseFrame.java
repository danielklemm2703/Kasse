package frontend.kasse;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.joda.time.DateTime;

import util.KeyAdapters;
import util.Methods;
import util.Pair;
import util.PopupTriggerListener;
import util.Preis;
import util.Try;
import util.table.MultiselectTable;
import util.table.NonEditColorableTableModel;
import backend.TypedJFrame;
import backend.container.DienstleistungsEintrag;
import backend.container.VerkaufsEintrag;
import backend.framemanagement.FrameManager;
import backend.framemanagement.MouseAdapters;
import database.Ordering;
import database.entities.Dienstleistung;
import database.entities.DienstleistungsInfo;
import database.entities.Friseur;
import database.entities.Gutschein;
import database.entities.Kategorie;
import database.entities.Kunde;
import database.entities.Ort;
import database.entities.Rezeptur;
import database.entities.Transaktion;
import database.entities.Verkauf;
import database.entities.VerkaufsInfo;
import database.enums.FrameType;
import database.util.TableDatas;
import datameer.com.google.common.base.Function;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.Lists;
import datameer.com.google.common.collect.Maps;
import frontend.util.Notification;

public class KasseFrame extends TypedJFrame {

    private static final long serialVersionUID = -1513580225986538916L;

    private JLabel _lblGutscheinInfo = new JLabel("");
    private JLabel _lblGutscheinInfoWert = new JLabel("");
    private JTextField _txtGutscheinCode = new JTextField();

    private JLabel _lblGesamtWert = new JLabel(Preis.of(0L).toString());

    private JComboBox<String> _friseurComboBox;
    private HashMap<Integer, Friseur> _friseurMapping = Maps.newHashMap();

    private JComboBox<String> _kundeComboBox;
    private HashMap<Integer, Kunde> _kundeMapping = Maps.newHashMap();

    private JCheckBox _chckbxLaufkunde;
    private JPopupMenu _verkaufPopUpMenu;
    private MultiselectTable _verkaufsEintragTable;
    private LinkedHashMap<Integer, VerkaufsEintrag> _verkaufEintragMapping = Maps.newLinkedHashMap();

    private JPopupMenu _dienstleistungPopUpMenu;
    private MultiselectTable _dienstleistungsEintragTable;
    private LinkedHashMap<Integer, DienstleistungsEintrag> _dienstleistungEintragMapping = Maps.newLinkedHashMap();

    private JLabel _lblZuZahlenWert = new JLabel(Preis.of(0L).toString());

    private static final AtomicReference<KasseFrame> singletonHolder = new AtomicReference<>();

    public static final KasseFrame instance() {
	if (singletonHolder.get() == null) {
	    singletonHolder.set(new KasseFrame());
	}
	return singletonHolder.get();
    }

    private final ActionListener _laufKundeActionListener = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    if (_chckbxLaufkunde.isSelected()) {
		_kundeComboBox.setEnabled(false);
	    } else {
		_kundeComboBox.setEnabled(true);
	    }
	}
    };

    private final ActionListener _dienstleistungChoser = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    Friseur friseur = _friseurMapping.get(_friseurComboBox.getSelectedIndex());
	    Optional<Kunde> kunde = Optional.absent();
	    if (!_chckbxLaufkunde.isSelected()) {
		kunde = Optional.fromNullable(_kundeMapping.get(_kundeComboBox.getSelectedIndex()));
	    }
	    if (friseur == null) {
		FrameManager.addFrame(new Notification(true, "Ein Friseur muss", "ausgewählt sein"));
	    } else {
		DienstleisungChoserFrame dienstleisungChoserFrame = new DienstleisungChoserFrame(kunde, friseur);
		FrameManager.addFrame(dienstleisungChoserFrame);
	    }
	}
    };

    private ActionListener _deleteChosenDienstleistungen = new ActionListener() {
	@Override
	public void actionPerformed(ActionEvent e) {
	    ArrayList<Integer> selected = Lists.newArrayList(Methods.toIterable(_dienstleistungsEintragTable.getSelectedRows()));
	    Collections.reverse(selected);
	    NonEditColorableTableModel model = (NonEditColorableTableModel) _dienstleistungsEintragTable.getModel();
	    for (Integer elementIndex : selected) {
		_dienstleistungEintragMapping.remove(elementIndex);
		model.removeRow(elementIndex);
	    }
	    updatePayValues();
	}
    };

    private ActionListener _deleteChosenVerkaeufe = new ActionListener() {
	@Override
	public void actionPerformed(ActionEvent e) {
	    ArrayList<Integer> selected = Lists.newArrayList(Methods.toIterable(_verkaufsEintragTable.getSelectedRows()));
	    Collections.reverse(selected);
	    NonEditColorableTableModel model = (NonEditColorableTableModel) _verkaufsEintragTable.getModel();
	    for (Integer elementIndex : selected) {
		_verkaufEintragMapping.remove(elementIndex);
		model.removeRow(elementIndex);
	    }
	    updatePayValues();
	}
    };

    private final ActionListener _verkaufChoser = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    Friseur friseur = _friseurMapping.get(_friseurComboBox.getSelectedIndex());
	    Optional<Kunde> kunde = Optional.absent();
	    if (!_chckbxLaufkunde.isSelected()) {
		kunde = Optional.fromNullable(_kundeMapping.get(_kundeComboBox.getSelectedIndex()));
	    }
	    if (friseur == null) {
		FrameManager.addFrame(new Notification(true, "Ein Friseur muss", "ausgewählt sein"));
	    } else {
		VerkaufChoserFrame verkaufChoserFrame = new VerkaufChoserFrame(kunde, friseur);
		FrameManager.addFrame(verkaufChoserFrame);
	    }
	}
    };

    private KasseFrame() {
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
	lblX.addMouseListener(MouseAdapters.closeFrame(this));
	lblX.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
	lblX.setBounds(571, 12, 13, 16);
	getContentPane().add(lblX);

	_dienstleistungPopUpMenu = new JPopupMenu("Dienstleistungen");
	JMenuItem itemDL1 = new JMenuItem("Hinzufügen");
	itemDL1.addActionListener(_dienstleistungChoser);
	JMenuItem itemDL2 = new JMenuItem("Entfernen");
	itemDL2.addActionListener(_deleteChosenDienstleistungen);
	_dienstleistungPopUpMenu.add(itemDL1);
	_dienstleistungPopUpMenu.add(itemDL2);

	JScrollPane scrollPaneDienstleistungen = new JScrollPane();
	scrollPaneDienstleistungen.addMouseListener(new PopupTriggerListener(_dienstleistungPopUpMenu));
	scrollPaneDienstleistungen.setBounds(25, 130, 559, 179);
	_dienstleistungsEintragTable = new MultiselectTable();
	_dienstleistungsEintragTable.setModel(TableDatas.createEmptyDienstleistungEintragModel());
	_dienstleistungsEintragTable.addMouseListener(new PopupTriggerListener(_dienstleistungPopUpMenu));
	scrollPaneDienstleistungen.setViewportView(_dienstleistungsEintragTable);
	getContentPane().add(scrollPaneDienstleistungen);

	_verkaufPopUpMenu = new JPopupMenu("Verkäufe");
	JMenuItem itemVK1 = new JMenuItem("Hinzufügen");
	itemVK1.addActionListener(_verkaufChoser);
	JMenuItem itemVK2 = new JMenuItem("Entfernen");
	itemVK2.addActionListener(_deleteChosenVerkaeufe);
	_verkaufPopUpMenu.add(itemVK1);
	_verkaufPopUpMenu.add(itemVK2);

	JScrollPane scrollPaneVerkaeufe = new JScrollPane();
	scrollPaneVerkaeufe.addMouseListener(new PopupTriggerListener(_verkaufPopUpMenu));
	scrollPaneVerkaeufe.setBounds(25, 344, 559, 179);
	_verkaufsEintragTable = new MultiselectTable();
	_verkaufsEintragTable.setModel(TableDatas.createEmptyVerkaufEintragModel());
	_verkaufsEintragTable.addMouseListener(new PopupTriggerListener(_verkaufPopUpMenu));
	scrollPaneVerkaeufe.setViewportView(_verkaufsEintragTable);
	getContentPane().add(scrollPaneVerkaeufe);

	JButton btnZurcksetzen = new JButton("Zurücksetzen");
	btnZurcksetzen.addActionListener(resetKasse);
	btnZurcksetzen.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	btnZurcksetzen.setBounds(25, 681, 152, 29);
	getContentPane().add(btnZurcksetzen);

	JButton btnAbschlieen = new JButton("Abschließen");
	btnAbschlieen.addActionListener(save);
	btnAbschlieen.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	btnAbschlieen.setBounds(454, 681, 130, 29);
	getContentPane().add(btnAbschlieen);

	JLabel lblGesamt = new JLabel("Gesamt");
	lblGesamt.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	lblGesamt.setBounds(228, 545, 102, 16);
	getContentPane().add(lblGesamt);

	JLabel lblGutscheinEnlsen = new JLabel("Gutschein Enlösen");
	lblGutscheinEnlsen.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	lblGutscheinEnlsen.setBounds(228, 573, 177, 16);
	getContentPane().add(lblGutscheinEnlsen);
	_txtGutscheinCode.setHorizontalAlignment(SwingConstants.RIGHT);

	_txtGutscheinCode.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	_txtGutscheinCode.setBounds(413, 568, 130, 26);
	_txtGutscheinCode.addKeyListener(KeyAdapters.numbersOnly());
	_txtGutscheinCode.setColumns(10);
	_txtGutscheinCode.getDocument().addDocumentListener(gutscheinCodeListener);
	getContentPane().add(_txtGutscheinCode);

	JLabel lblZuZahlen = new JLabel("Zu Zahlen");
	lblZuZahlen.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	lblZuZahlen.setBounds(228, 629, 101, 16);
	getContentPane().add(lblZuZahlen);

	_lblGutscheinInfo.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	_lblGutscheinInfo.setBounds(228, 601, 162, 16);
	_lblGutscheinInfo.setVisible(false);
	getContentPane().add(_lblGutscheinInfo);

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

	_lblGesamtWert.setHorizontalAlignment(SwingConstants.RIGHT);
	_lblGesamtWert.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	_lblGesamtWert.setBounds(413, 545, 130, 16);
	getContentPane().add(_lblGesamtWert);
	_lblGutscheinInfoWert.setHorizontalAlignment(SwingConstants.RIGHT);

	_lblGutscheinInfoWert.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	_lblGutscheinInfoWert.setBounds(391, 596, 152, 29);
	_lblGutscheinInfoWert.setVisible(false);
	getContentPane().add(_lblGutscheinInfoWert);

	_lblZuZahlenWert.setHorizontalAlignment(SwingConstants.RIGHT);
	_lblZuZahlenWert.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	_lblZuZahlenWert.setBounds(413, 629, 130, 16);
	getContentPane().add(_lblZuZahlenWert);
    }

    private final void updatePayValues() {
	double aggregate = 0L;
	for (DienstleistungsEintrag dienstleistungsEintrag : _dienstleistungEintragMapping.values()) {
	    aggregate += dienstleistungsEintrag.getDienstleistung().getPreis().value();
	}
	for (VerkaufsEintrag verkaufsEintrag : _verkaufEintragMapping.values()) {
	    aggregate += verkaufsEintrag.getVerkauf().getPreis().value();
	}
	_lblGesamtWert.setText(Preis.of(aggregate).toString());

	if (!_txtGutscheinCode.getText().isEmpty()) {
	    int entityId = Integer.parseInt(_txtGutscheinCode.getText());
	    Optional<Gutschein> gutschein = Gutschein.loadById(entityId);
	    if (gutschein.isPresent()) {
		_lblGutscheinInfoWert.setVisible(true);
		_lblGutscheinInfo.setVisible(true);
		if (gutschein.get().getRestWert().value() > 0) {
		    _lblGutscheinInfoWert.setText(gutschein.get().getRestWert().toString());
		    _lblGutscheinInfo.setText("Gutschein Wert");
		    aggregate -= gutschein.get().getRestWert().value();
		    _lblZuZahlenWert.setText(Preis.of(aggregate).toString());
		} else {
		    _lblGutscheinInfoWert.setText(" aufgebraucht.");
		    _lblGutscheinInfo.setText("Gutschein bereits");
		}
	    } else {
		_lblGutscheinInfoWert.setText("nicht gefunden");
		_lblGutscheinInfoWert.setVisible(true);
		_lblGutscheinInfo.setText("Gutschein wurde");
		_lblGutscheinInfo.setVisible(true);
	    }
	} else {
	    _lblGutscheinInfo.setVisible(false);
	    _lblGutscheinInfoWert.setVisible(false);
	}
	if (aggregate < 0) {
	    _lblZuZahlenWert.setText(Preis.of(0L).toString());
	} else {
	    _lblZuZahlenWert.setText(Preis.of(aggregate).toString());
	}
    }

    public void addChosenDienstleistungen(final FluentIterable<Dienstleistung> chosenDienstleistungen) {
	Friseur friseur = _friseurMapping.get(_friseurComboBox.getSelectedIndex());
	if (friseur == null) {
	    Notification error = new Notification(true, "Kein Friseur gewählt.", "Keine Dienstleistungen übertragen.");
	    FrameManager.addFrame(error);
	    return;
	}
	Optional<Kunde> kunde = Optional.absent();
	if (!_chckbxLaufkunde.isSelected()) {
	    kunde = Optional.fromNullable(_kundeMapping.get(_kundeComboBox.getSelectedIndex()));
	}
	NonEditColorableTableModel model = (NonEditColorableTableModel) _dienstleistungsEintragTable.getModel();
	int index = _dienstleistungEintragMapping.size();
	for (Dienstleistung dienstleistung : chosenDienstleistungen) {
	    DienstleistungsEintrag eintrag = new DienstleistungsEintrag(dienstleistung, friseur, kunde);
	    _dienstleistungEintragMapping.put(index, eintrag);
	    model.addRow(new Object[] { dienstleistung.getDienstleistungsName(), kunde.transform(Kunde.toName).or("Laufkunde"), friseur.getFriseurName(),
		    dienstleistung.getPreis().toString() });
	    index++;
	}
	updatePayValues();
    }

    public void addChosenVerkaeufe(final FluentIterable<Verkauf> chosenVerkauf) {
	Friseur friseur = _friseurMapping.get(_friseurComboBox.getSelectedIndex());
	if (friseur == null) {
	    Notification error = new Notification(true, "Kein Friseur gewählt.", "Keine Verkäufe übertragen.");
	    FrameManager.addFrame(error);
	    return;
	}
	Optional<Kunde> kunde = Optional.absent();
	if (!_chckbxLaufkunde.isSelected()) {
	    kunde = Optional.fromNullable(_kundeMapping.get(_kundeComboBox.getSelectedIndex()));
	}
	NonEditColorableTableModel model = (NonEditColorableTableModel) _verkaufsEintragTable.getModel();
	int index = _verkaufEintragMapping.size();
	for (Verkauf verkauf : chosenVerkauf) {
	    VerkaufsEintrag eintrag = new VerkaufsEintrag(verkauf, friseur, kunde);
	    _verkaufEintragMapping.put(index, eintrag);
	    model.addRow(new Object[] { verkauf.getVerkaufsName(), kunde.transform(Kunde.toName).or("Laufkunde"), friseur.getFriseurName(),
		    verkauf.getPreis().toString() });
	    index++;
	}
	updatePayValues();
    }

    private final void resetKasse() {
	_dienstleistungsEintragTable.setModel(TableDatas.createEmptyDienstleistungEintragModel());
	_dienstleistungEintragMapping.clear();

	_verkaufsEintragTable.setModel(TableDatas.createEmptyVerkaufEintragModel());
	_verkaufEintragMapping.clear();

	if (_friseurComboBox.getModel().getSize() > 0) {
	    _friseurComboBox.setSelectedIndex(0);
	}
	if (_kundeComboBox.getModel().getSize() > 0) {
	    _kundeComboBox.setSelectedIndex(0);
	}

	_txtGutscheinCode.setText("");
	_lblGutscheinInfo.setVisible(false);
	_lblGutscheinInfo.setText("");
	_lblGutscheinInfoWert.setVisible(false);
	_lblGutscheinInfoWert.setText("");
	updatePayValues();
    }

    private final ActionListener save = new ActionListener() {
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
	    if (_dienstleistungEintragMapping.isEmpty() && _verkaufEintragMapping.isEmpty()) {
		Notification notification = new Notification(true, "Es wurde nichts", "ausgewählt.");
		FrameManager.addFrame(notification);
		return;
	    }

	    double umsatz = 0L;
	    double gutscheinStartwert = 0L;
	    Optional<Gutschein> gutschein = Optional.absent();

	    FluentIterable<Pair<String, Preis>> boughtGutscheine = FluentIterable.from(ImmutableList.<Pair<String, Preis>> of());
	    FluentIterable<String> rezepturKunden = FluentIterable.from(ImmutableList.<String> of());
	    FluentIterable<DienstleistungsInfo> dlInfos = FluentIterable.from(ImmutableList.<DienstleistungsInfo> of());
	    FluentIterable<VerkaufsInfo> vkInfos = FluentIterable.from(ImmutableList.<VerkaufsInfo> of());
	    FluentIterable<Long> gutscheinKategorien = Kategorie.gutscheinKategorien();

	    for (DienstleistungsEintrag dienstleistungsEintrag : _dienstleistungEintragMapping.values()) {
		umsatz += dienstleistungsEintrag.getDienstleistung().getPreis().value();

		if (dienstleistungsEintrag.getDienstleistung().isRezepturplichtig() && dienstleistungsEintrag.getKunde().isPresent()) {
		    rezepturKunden = rezepturKunden.append(dienstleistungsEintrag.getKunde().transform(Kunde.toName).get());
		}

		DienstleistungsInfo dienstleistungsInfo = buildDienstleistungInfo(dienstleistungsEintrag);
		Try<Long> entityId = dienstleistungsInfo.save();

		if (entityId.isFailure()) {
		    Notification notification = new Notification(true, "Fehler beim Speichern", "der Dienstleistungen.");
		    FrameManager.addFrame(notification);
		    return;
		}

		dlInfos = dlInfos.append(dienstleistungsInfo);
	    }

	    for (VerkaufsEintrag verkaufsEintrag : _verkaufEintragMapping.values()) {
		umsatz += verkaufsEintrag.getVerkauf().getPreis().value();

		long kategorieId = verkaufsEintrag.getVerkauf().getKategorieId();
		if (verkaufsEintrag.getVerkauf().getVerkaufsName().contains("Gutschein") && gutscheinKategorien.contains(kategorieId)) {
		    System.err.println("Ein Gutschein wurde gekauft.");
		    String kundeName = verkaufsEintrag.getKunde().transform(Kunde.toName).or("Laufkunde");
		    Preis gutscheinWert = verkaufsEintrag.getVerkauf().getPreis();
		    boughtGutscheine = boughtGutscheine.append(Pair.of(kundeName, gutscheinWert));
		}

		VerkaufsInfo verkaufsInfo = buildVerkaufsInfo(verkaufsEintrag);
		Try<Long> entityId = verkaufsInfo.save();

		if (entityId.isFailure()) {
		    Notification notification = new Notification(true, "Fehler beim Speichern", "der Verkäufe.");
		    FrameManager.addFrame(notification);
		    return;
		}

		vkInfos = vkInfos.append(verkaufsInfo);
	    }

	    if (!_txtGutscheinCode.getText().isEmpty()) {
		int entityId = Integer.parseInt(_txtGutscheinCode.getText());
		gutschein = Gutschein.loadById(entityId);
		if (gutschein.isPresent()) {
		    gutscheinStartwert = gutschein.get().getRestWert().value();
		    if (gutschein.get().getRestWert().value() > umsatz) {
			double newRestwert = gutschein.get().getRestWert().value() - umsatz;
			umsatz = 0;
			gutschein.get().setRestWert(Preis.of(newRestwert));
		    } else {
			umsatz -= gutschein.get().getRestWert().value();
			gutschein.get().setRestWert(Preis.of(0L));
		    }
		}
	    }

	    // check
	    if (umsatz != Preis.of(_lblZuZahlenWert.getText()).value()) {
		Notification notification = new Notification(true, "Fehler beim Berechnen", "des Gesamtpreises.");
		FrameManager.addFrame(notification);
		return;
	    }

	    if (gutschein.isPresent()) {
		Try<Long> gutscheinEntityId = new Gutschein(gutschein.get().getEntityId().get(), gutschein.get().getTransaktionId(), gutschein.get()
			.getKundeName(), gutschein.get().getRestWert()).save();
		if (gutscheinEntityId.isFailure()) {
		    Notification notification = new Notification(true, "Fehler beim Update", "des Gutscheins.");
		    FrameManager.addFrame(notification);
		    return;
		}
	    }

	    Try<Long> transaktion = new Transaktion(dlInfos, vkInfos, DateTime.now(), Optional.of(Preis.of(gutscheinStartwert)), gutschein, Preis.of(umsatz))
		    .save();
	    if (transaktion.isFailure()) {
		Notification notification = new Notification(true, "Fehler beim Speichern", "der Transaktion.");
		FrameManager.addFrame(notification);
		return;
	    }

	    // erstellt gekaufte Gutscheine
	    FluentIterable<Gutschein> createdGutscheine = FluentIterable.from(ImmutableList.<Gutschein> of());
	    for (Pair<String, Preis> g : boughtGutscheine) {
		Gutschein newGutschein = new Gutschein(transaktion.get(), g._1, g._2);
		Try<Long> savedGutschein = newGutschein.save();
		if (savedGutschein.isFailure()) {
		    Notification notification = new Notification(true, "Fehler beim Speichern", "des Gutscheins.");
		    FrameManager.addFrame(notification);
		    return;
		}
		createdGutscheine = createdGutscheine.append(newGutschein);
	    }

	    // erstellt nötige Rezepturen
	    FluentIterable<Rezeptur> rezepturenToEnter = FluentIterable.from(ImmutableList.<Rezeptur> of());
	    for (String kundeName : rezepturKunden.toSet()) {
		Rezeptur newRezeptur = new Rezeptur(transaktion.get(), kundeName, Optional.<FluentIterable<Long>> absent(), Optional.<Long> absent(), "", false);
		Try<Long> savedRezeptur = newRezeptur.save();
		if (savedRezeptur.isFailure()) {
		    Notification notification = new Notification(true, "Fehler beim Erstellen", "der Rezeptur.");
		    FrameManager.addFrame(notification);
		    return;
		}
		rezepturenToEnter = rezepturenToEnter.append(newRezeptur);
	    }

	    resetKasse();
	    FinishFrame frame = new FinishFrame(createdGutscheine, gutschein, rezepturenToEnter);
	    FrameManager.addFrame(frame);
	}
    };

    private static final DienstleistungsInfo buildDienstleistungInfo(DienstleistungsEintrag dienstleistungsEintrag) {
	String dienstleistungName = dienstleistungsEintrag.getDienstleistung().getDienstleistungsName();
	String kundeName = dienstleistungsEintrag.getKunde().transform(Kunde.toName).or("Laufkunde");
	String friseurName = dienstleistungsEintrag.getFriseur().getFriseurName();
	Preis preis = dienstleistungsEintrag.getDienstleistung().getPreis();
	DienstleistungsInfo dienstleistungsInfo = new DienstleistungsInfo(kundeName, friseurName, dienstleistungName, preis);
	return dienstleistungsInfo;
    }

    private static final VerkaufsInfo buildVerkaufsInfo(VerkaufsEintrag verkaufsEintrag) {
	String verkaufName = verkaufsEintrag.getVerkauf().getVerkaufsName();
	String kundeName = verkaufsEintrag.getKunde().transform(Kunde.toName).or("Laufkunde");
	String friseurName = verkaufsEintrag.getFriseur().getFriseurName();
	Preis preis = verkaufsEintrag.getVerkauf().getPreis();
	VerkaufsInfo verkaufsInfo = new VerkaufsInfo(kundeName, friseurName, verkaufName, preis);
	return verkaufsInfo;
    }

    private final ActionListener resetKasse = new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    resetKasse();
	}
    };

    private final DocumentListener gutscheinCodeListener = new DocumentListener() {
	@Override
	public void removeUpdate(DocumentEvent e) {
	    updatePayValues();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
	    updatePayValues();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	    updatePayValues();
	}
    };
}
