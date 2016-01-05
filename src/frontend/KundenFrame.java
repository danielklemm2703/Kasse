package frontend;

import static backend.FrameManager.closeFrame;
import static backend.FrameManager.closeOnLeave;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import backend.TypedJFrame;
import backend.enums.FrameType;
import database.Ordering;
import database.entities.Kunde;
import database.entities.Ort;
import datameer.com.google.common.collect.ImmutableList;

public class KundenFrame extends TypedJFrame {

    private static final long serialVersionUID = 4794193793492079259L;
    private JTable _table;

    /**
     * Create the frame.
     */
    public KundenFrame() {
	_type = FrameType.KUNDE;
	addWindowFocusListener(closeOnLeave(this));
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setResizable(false);
	setTitle("Kunden");
	setUndecorated(true);

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) Math.round(screenSize.getWidth());
	int height = (int) Math.round(screenSize.getHeight());

	int startingWidth = ((width / 2) - (602 / 2));
	int startingHeigth = ((height / 2) - (542 / 2));

	setBounds(startingWidth, startingHeigth, 602, 542);

	getContentPane().setLayout(null);

	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setBounds(0, 62, 602, 480);
	getContentPane().add(scrollPane);
	_table = new JTable(createEmptyKundenModel());
	_table.setShowHorizontalLines(true);
	_table.setShowVerticalLines(true);
	_table.setGridColor(Color.BLACK);
	_table.addMouseListener(kundeChooser());
	scrollPane.setViewportView(_table);
	loadKundeData("A", _table);

	JLabel lblKunden = new JLabel("Kunden");
	lblKunden.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	lblKunden.setBounds(6, 6, 104, 25);
	getContentPane().add(lblKunden);

	JMenuBar menuBar = createMenuBar(_table);
	menuBar.setBounds(0, 40, 602, 22);
	getContentPane().add(menuBar);

	JLabel lblX = new JLabel("X");
	lblX.addMouseListener(closeFrame(this));
	lblX.setHorizontalAlignment(SwingConstants.CENTER);
	lblX.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	lblX.setBounds(570, 10, 26, 16);
	getContentPane().add(lblX);

    }

    private static final DefaultTableModel createEmptyKundenModel() {
	DefaultTableModel model = new DefaultTableModel();
	model.addColumn("Nachname");
	model.addColumn("Vorname");
	model.addColumn("Ort");
	model.addColumn("Telefon");
	return model;
    }

    private MouseAdapter kundeChooser() {
	return new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
	    }
	};
    }

    private static final JMenuBar createMenuBar(final JTable table) {
	JMenuBar menuBar = new JMenuBar();
	ImmutableList<String> letters = ImmutableList.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
		"U", "V", "W", "X", "Y", "Z");
	for (String letter : letters) {
	    JMenu a = new JMenu(letter);
	    a.addMouseListener(loadKundenOnKlick(letter, table));
	    menuBar.add(a);
	}
	return menuBar;
    }

    private static final void loadKundeData(final String letter, final JTable table) {
	Iterable<Kunde> kunden = Kunde.loadByParameterStartsWith("NACHNAME", letter, new Ordering("NACHNAME", "ASC"));
	DefaultTableModel model = createEmptyKundenModel();
	for (Kunde kunde : kunden) {
	    model.addRow(new Object[] { kunde.getNachname(), kunde.getVorname(), Ort.loadById(kunde.getOrtId()).get().getOrtName(), kunde.getTelefon() });
	}
	table.setModel(model);
    }

    private static final MouseAdapter loadKundenOnKlick(final String letter, final JTable table) {
	return new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		loadKundeData(letter, table);
	    }
	};
    }
}
