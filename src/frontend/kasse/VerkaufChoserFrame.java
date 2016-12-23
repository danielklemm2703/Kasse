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

import util.Methods;
import util.Pair;
import util.table.MultiselectTable;
import util.table.NonEditColorableTableModel;
import backend.TypedJFrame;
import backend.framemanagement.FrameManager;
import backend.framemanagement.MouseAdapters;
import database.entities.Friseur;
import database.entities.Kunde;
import database.entities.Verkauf;
import database.enums.FrameType;
import database.util.TableDatas;
import datameer.com.google.common.base.Function;
import datameer.com.google.common.base.Optional;
import datameer.com.google.common.collect.FluentIterable;
import datameer.com.google.common.collect.ImmutableMap;

public class VerkaufChoserFrame extends TypedJFrame {

    private static final long serialVersionUID = 6060625047333936877L;
    private MultiselectTable _verkaufsTable;
    ImmutableMap<Integer, Optional<Verkauf>> _verkaufMapping;

    public VerkaufChoserFrame(final Optional<Kunde> kunde, final Friseur friseur) {
	_type = FrameType.VERKAUF_CHOSER;
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	setResizable(false);
	setTitle("Verkauf Hinzufügen");
	getContentPane().setLayout(null);

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) Math.round(screenSize.getWidth());
	int height = (int) Math.round(screenSize.getHeight());

	int startingWidth = ((width / 2) - (440 / 2));
	int startingHeigth = ((height / 2) - (353 / 2));

	setBounds(startingWidth, startingHeigth, 440, 353);
	getContentPane().setLayout(null);

	JLabel lblVerkauefeHinzufgen = new JLabel("Verkauf Hinzufügen");
	lblVerkauefeHinzufgen.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
	lblVerkauefeHinzufgen.setBounds(6, 6, 283, 25);
	getContentPane().add(lblVerkauefeHinzufgen);

	JLabel lblX = new JLabel("X");
	lblX.addMouseListener(MouseAdapters.closeFrame(this));
	lblX.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
	lblX.setBounds(421, 10, 13, 16);
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
	_verkaufsTable = new MultiselectTable();
	Pair<NonEditColorableTableModel, ImmutableMap<Integer, Optional<Verkauf>>> verkaufData = TableDatas.loadVerkaufChoserData();
	_verkaufMapping = verkaufData._2;
	_verkaufsTable.setModel(verkaufData._1);
	_verkaufsTable.getSelectionModel().addListSelectionListener(handleMultiselect(_verkaufMapping));
	scrollPane.setViewportView(_verkaufsTable);
	getContentPane().add(scrollPane);

	JButton btnHinzufgen = new JButton("Hinzufügen");
	btnHinzufgen.addActionListener(addChosenVerkaufToKasse(this));
	btnHinzufgen.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	btnHinzufgen.setBounds(285, 318, 136, 29);
	getContentPane().add(btnHinzufgen);

	String kundeText = kunde.transform(Kunde.toName).or("Laufkunde");
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

    private ActionListener addChosenVerkaufToKasse(final TypedJFrame frame){
	return new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		FluentIterable<Verkauf> chosenVerkaeufe = FluentIterable.from(Methods.toSet(_verkaufsTable.getSelectedRows())).transform(
			new Function<Integer, Verkauf>() {
			    @Override
			    public Verkauf apply(Integer input) {
				return _verkaufMapping.get(input).get();
			    }
			});
		KasseFrame.instance().addChosenVerkaeufe(chosenVerkaeufe);
		FrameManager.closeFrameOnTop(frame);
	    }
	};
    }

    private final ListSelectionListener handleMultiselect(final ImmutableMap<Integer, Optional<Verkauf>> map) {
	return new ListSelectionListener() {
	    @Override
	    public void valueChanged(ListSelectionEvent e) {
		for (Integer selectedIndex : Methods.toSet(_verkaufsTable.getSelectedRows())) {
		    if (!map.get(selectedIndex).isPresent()) {
			_verkaufsTable.getSelectionModel().removeSelectionInterval(selectedIndex, selectedIndex);
		    }
		}
	    }
	};
    }
}
