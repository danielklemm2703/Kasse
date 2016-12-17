package frontend.kunde;

import static util.Functions.toPresent;
import static util.Functions.toRezeptur;
import static util.Predicates.eingetragen;
import static util.Predicates.present;
import static util.Predicates.withRezept;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import backend.TypedJFrame;
import backend.framemanagement.ActionListeners;
import backend.framemanagement.FrameManager;
import backend.framemanagement.MouseAdapters;
import database.Ordering;
import database.entities.Kunde;
import database.entities.Ort;
import database.entities.Rezeptur;
import database.entities.Transaktion;
import database.enums.FrameType;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableList;
import datameer.com.google.common.collect.Maps;
import frontend.RezepturenFrame;
import frontend.util.Notification;

public class KundenFrame extends TypedJFrame {

    private static final long serialVersionUID = 4794193793492079259L;
    private JTable _table;
    private HashMap<Integer, Kunde> _kundenMapping = Maps.newHashMap();

    /**
     * Create the frame.
     */
    public KundenFrame() {
	_type = FrameType.KUNDE;
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setResizable(false);
	setTitle("Kunden");
	setUndecorated(true);

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) Math.round(screenSize.getWidth());
	int height = (int) Math.round(screenSize.getHeight());

	int startingWidth = ((width / 2) - (602 / 2));
	int startingHeigth = ((height / 2) - (542 / 2));

	setBounds(startingWidth, startingHeigth, 602, 536);

	getContentPane().setLayout(null);

	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setBounds(0, 62, 602, 444);
	getContentPane().add(scrollPane);
	_table = new JTable(createEmptyKundenModel());
	_table.setShowHorizontalLines(true);
	_table.setShowVerticalLines(true);
	_table.setGridColor(Color.BLACK);
	scrollPane.setViewportView(_table);
	loadKundeData("A");

	JLabel lblKunden = new JLabel("Kunden");
	lblKunden.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	lblKunden.setBounds(6, 6, 104, 25);
	getContentPane().add(lblKunden);

	JMenuBar menuBar = createMenuBar();
	menuBar.setBounds(0, 40, 602, 22);
	getContentPane().add(menuBar);

	JLabel lblX = new JLabel("X");
	lblX.addMouseListener(MouseAdapters.closeFrameMouseAdapter(this));
	lblX.setHorizontalAlignment(SwingConstants.CENTER);
	lblX.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	lblX.setBounds(570, 10, 26, 16);
	getContentPane().add(lblX);

	JButton neuerKundeBtn = new JButton("Neu Anlegen");
	KundeDataFrame kundeDataFrame = new KundeDataFrame(Optional.<Kunde> absent());
	neuerKundeBtn.addActionListener(ActionListeners.addFrameListener(kundeDataFrame));
	neuerKundeBtn.setBounds(10, 507, 117, 29);
	getContentPane().add(neuerKundeBtn);

	JButton btnNewButton = new JButton("Rezepturen Anzeigen");
	btnNewButton.addActionListener(kundeAnzeigen());
	btnNewButton.setBounds(154, 507, 168, 29);
	getContentPane().add(btnNewButton);

	JButton btnDatenndern = new JButton("Daten Ändern");
	btnDatenndern.addActionListener(kundeUpdate());
	btnDatenndern.setBounds(347, 507, 117, 29);
	getContentPane().add(btnDatenndern);

	JButton btnLschen = new JButton("Löschen");
	btnLschen.addActionListener(kundeDelete());
	btnLschen.setBounds(485, 507, 117, 29);
	getContentPane().add(btnLschen);
    }

    private final ActionListener kundeDelete() {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		int selectedRow = _table.getSelectedRow();
		if (selectedRow == -1) {
		    FrameManager.addFrame(new Notification(true, "Es wurde kein", "Kunde ausgewählt!"));
		    return;
		}
		Kunde kunde = _kundenMapping.get(selectedRow);
		if (kunde == null) {
		    FrameManager.addFrame(new Notification(true, "Beim Laden des Kunden", "trat ein Fehler auf"));
		    return;
		}
		KundeDeleteFrame kundeDeleteFrame = new KundeDeleteFrame(kunde);
		FrameManager.addFrame(kundeDeleteFrame);
	    }
	};
    }

    private final ActionListener kundeAnzeigen() {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		int selectedRow = _table.getSelectedRow();
		if (selectedRow == -1) {
		    FrameManager.addFrame(new Notification(true, "Es wurde kein", "Kunde ausgewählt!"));
		    return;
		}
		Kunde kunde = _kundenMapping.get(selectedRow);
		if (kunde == null) {
		    FrameManager.addFrame(new Notification(true, "Beim Laden des Kunden", "trat ein Fehler auf"));
		    return;
		}
		Optional<Ort> ort = Ort.loadById(kunde.getOrtId());
		if (!ort.isPresent()) {
		    FrameManager.addFrame(new Notification(true, "Beim Laden des Kunden", "trat ein Fehler auf"));
		    return;
		}
		FluentIterable<Rezeptur> rezepturen = FluentIterable
			.from(Transaktion.loadByParameter("KUNDE_ID", "" + kunde.getEntityId().get(), new Ordering("DATUM", "DESC"))).filter(withRezept)
			.transform(toRezeptur).filter(present).transform(toPresent).filter(eingetragen);
		if (rezepturen.isEmpty()) {
		    FrameManager.addFrame(new Notification(true, "Kunde hat noch", "keine Rezepturen"));
		    return;
		}
		RezepturenFrame kundeDataFrame = new RezepturenFrame(ort.get(), kunde, rezepturen);
		FrameManager.addFrame(kundeDataFrame);
	    }
	};
    }

    private static final DefaultTableModel createEmptyKundenModel() {
	DefaultTableModel model = new DefaultTableModel();
	model.addColumn("Nachname");
	model.addColumn("Vorname");
	model.addColumn("Ort");
	model.addColumn("Telefon");
	return model;
    }

    private final JMenuBar createMenuBar() {
	JMenuBar menuBar = new JMenuBar();
	ImmutableList<String> letters = ImmutableList.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
		"U", "V", "W", "X", "Y", "Z");
	for (String letter : letters) {
	    JMenu a = new JMenu(letter);
	    a.addMouseListener(loadKundenOnKlick(letter));
	    menuBar.add(a);
	}
	return menuBar;
    }

    private final void loadKundeData(final String letter) {
	Iterable<Kunde> kunden = Kunde.loadByParameterStartsWith("NACHNAME", letter, new Ordering("NACHNAME", "ASC"));
	DefaultTableModel model = createEmptyKundenModel();
	_kundenMapping = new HashMap<Integer, Kunde>();
	int index = 0;
	for (Kunde kunde : kunden) {
	    model.addRow(new Object[] { kunde.getNachname(), kunde.getVorname(), Ort.loadById(kunde.getOrtId()).get().getOrtName(), kunde.getTelefon() });
	    _kundenMapping.put(index++, kunde);
	}
	_table.setModel(model);
    }

    private final MouseAdapter loadKundenOnKlick(final String letter) {
	return new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		loadKundeData(letter);
	    }
	};
    }

    private final ActionListener kundeUpdate() {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		int selectedRow = _table.getSelectedRow();
		if (selectedRow == -1) {
		    FrameManager.addFrame(new Notification(true, "Es wurde kein", "Kunde ausgewählt!"));
		    return;
		}
		Kunde kunde = _kundenMapping.get(selectedRow);
		if (kunde == null) {
		    FrameManager.addFrame(new Notification(true, "Beim Laden des Kunden", "trat ein Fehler auf"));
		    return;
		}
		KundeDataFrame kundeDataFrame = new KundeDataFrame(Optional.of(kunde));
		FrameManager.addFrame(kundeDataFrame);
	    }
	};
    }
}
