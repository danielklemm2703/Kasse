package frontend.kasse;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import util.Pair;
import util.table.MultiselectTable;
import util.table.NonEditColorableTableModel;
import backend.TypedJFrame;
import backend.framemanagement.MouseAdapters;
import database.entities.Dienstleistung;
import database.entities.Friseur;
import database.entities.Kunde;
import database.enums.FrameType;
import database.util.TableDatas;
import datameer.com.google.common.base.Function;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.ImmutableMap;
import datameer.com.google.common.collect.ImmutableSet;
import datameer.com.google.common.collect.ImmutableSet.Builder;

public class DienstleisungChoserFrame extends TypedJFrame {

    private static final long serialVersionUID = 6060625047333936877L;
    private MultiselectTable _dienstleistungsTable;
    ImmutableMap<Integer, Optional<Dienstleistung>> _dienstleistungMapping;

    public DienstleisungChoserFrame(final Optional<Kunde> kunde, final Friseur friseur) {
	_type = FrameType.DIENSTLEISTUNG_CHOSER;
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	setResizable(false);
	setTitle("Dienstleistung Hinzufügen");
	getContentPane().setLayout(null);

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) Math.round(screenSize.getWidth());
	int height = (int) Math.round(screenSize.getHeight());

	int startingWidth = ((width / 2) - (440 / 2));
	int startingHeigth = ((height / 2) - (353 / 2));

	setBounds(startingWidth, startingHeigth, 440, 353);
	getContentPane().setLayout(null);

	JLabel lblDienstleistungenHinzufgen = new JLabel("Dienstleistungen Hinzufügen");
	lblDienstleistungenHinzufgen.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
	lblDienstleistungenHinzufgen.setBounds(6, 6, 283, 25);
	getContentPane().add(lblDienstleistungenHinzufgen);

	JLabel lblX = new JLabel("X");
	lblX.addMouseListener(MouseAdapters.closeFrame(this));
	lblX.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
	lblX.setBounds(421, 10, 13, 16);
	lblX.addMouseListener(MouseAdapters.closeFrame(this));
	getContentPane().add(lblX);

	JLabel lblFuer = new JLabel("Für:");
	lblFuer.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
	lblFuer.setBounds(16, 36, 34, 16);
	getContentPane().add(lblFuer);

	JLabel lblVon = new JLabel("Von:");
	lblVon.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
	lblVon.setBounds(16, 56, 34, 16);
	getContentPane().add(lblVon);

	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setBounds(16, 82, 405, 227);
	_dienstleistungsTable = new MultiselectTable();
	Pair<NonEditColorableTableModel, ImmutableMap<Integer, Optional<Dienstleistung>>> dienstleistungData = TableDatas.loadDienstleistungChoserData();
	_dienstleistungMapping = dienstleistungData._2;
	_dienstleistungsTable.setModel(dienstleistungData._1);
	_dienstleistungsTable.getSelectionModel().addListSelectionListener(handleMultiselect(_dienstleistungMapping));
	scrollPane.setViewportView(_dienstleistungsTable);
	getContentPane().add(scrollPane);

	JButton btnHinzufgen = new JButton("Hinzufügen");
	btnHinzufgen.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
	    }
	});
	btnHinzufgen.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	btnHinzufgen.setBounds(285, 318, 136, 29);
	getContentPane().add(btnHinzufgen);

	String kundeText = kunde.transform(new Function<Kunde, String>() {
	    @Override
	    public String apply(Kunde input) {
		return input.getNachname() + ", " + input.getVorname();
	    }
	}).or("Laufkunde");
	JLabel lblFuerWert = new JLabel(kundeText);
	lblFuerWert.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
	lblFuerWert.setBounds(55, 37, 234, 16);
	getContentPane().add(lblFuerWert);

	JLabel lblVonWert = new JLabel(friseur.getFriseurName());
	lblVonWert.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
	lblVonWert.setBounds(55, 57, 234, 16);
	getContentPane().add(lblVonWert);
	setUndecorated(true);
    }

    private final ListSelectionListener handleMultiselect(final ImmutableMap<Integer, Optional<Dienstleistung>> map) {
	return new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e) {
		Builder<Integer> aggregate = ImmutableSet.<Integer> builder();
		for (int selected : _dienstleistungsTable.getSelectedRows()) {
		    aggregate.add(selected);
		}
		for (Integer selectedIndex : aggregate.build()) {
		    if (!map.get(selectedIndex).isPresent()) {
			_dienstleistungsTable.getSelectionModel().removeSelectionInterval(selectedIndex, selectedIndex);
		    }
		}
	    }
	};
    }
}
