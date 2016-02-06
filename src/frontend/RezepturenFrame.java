package frontend;
import static backend.FrameManager.closeFrameAndOpenKundenFrame;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import backend.TypedJFrame;
import backend.enums.FrameType;
import database.entities.Rezeptur;
import datameer.com.google.common.collect.FluentIterable;

public class RezepturenFrame extends TypedJFrame {

    private static final long serialVersionUID = 2931432891689129660L;
    private JPanel _contentPane;
    private JTable _table;

    /**
     * Create the frame.
     */
    public RezepturenFrame(final FluentIterable<Rezeptur> rezepturen) {
	_type = FrameType.REZEPTUREN;
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setResizable(false);
	setTitle("Kunden");
	setUndecorated(true);

	setBounds(100, 100, 606, 413);
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
	label.setBounds(418, 12, 26, 16);
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

	JLabel label_1 = new JLabel("(1/5)");
	label_1.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	label_1.setBounds(125, 7, 61, 21);
	_contentPane.add(label_1);

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
