package frontend;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import backend.TypedJFrame;
import backend.enums.FrameType;

public class KasseFrame extends TypedJFrame {

    private static final long serialVersionUID = -1513580225986538916L;
    private JTextField textField;

    public KasseFrame() {
	_type = FrameType.KASSE;
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	setResizable(false);
	setTitle("Kasse");
	getContentPane().setLayout(null);

	// Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	// int width = (int) Math.round(screenSize.getWidth());
	// int height = (int) Math.round(screenSize.getHeight());
	//
	// int startingWidth = ((width / 2) - (317 / 2));
	// int startingHeigth = ((height / 2) - (172 / 2));
	//
	// setBounds(startingWidth, startingHeigth, 317, 172);
	// getContentPane().setLayout(null);

	JLabel lblKasse = new JLabel("Kasse");
	lblKasse.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	lblKasse.setBounds(228, 6, 70, 29);
	getContentPane().add(lblKasse);

	JLabel lblX = new JLabel("X");
	lblX.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
	lblX.setBounds(571, 12, 13, 16);
	getContentPane().add(lblX);

	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setBounds(25, 80, 559, 179);
	getContentPane().add(scrollPane);

	JScrollPane scrollPane_1 = new JScrollPane();
	scrollPane_1.setBounds(25, 315, 559, 179);
	getContentPane().add(scrollPane_1);

	JButton btnZurcksetzen = new JButton("Zurücksetzen");
	btnZurcksetzen.setBounds(213, 681, 117, 29);
	getContentPane().add(btnZurcksetzen);

	JButton btnAbschlieen = new JButton("Abschließen");
	btnAbschlieen.setBounds(424, 681, 117, 29);
	getContentPane().add(btnAbschlieen);

	JLabel lblGesamt = new JLabel("Gesamt");
	lblGesamt.setBounds(269, 519, 61, 16);
	getContentPane().add(lblGesamt);

	JLabel lblGutscheinEnlsen = new JLabel("Gutschein Enlösen");
	lblGutscheinEnlsen.setBounds(228, 547, 126, 16);
	getContentPane().add(lblGutscheinEnlsen);

	textField = new JTextField();
	textField.setBounds(386, 542, 130, 26);
	getContentPane().add(textField);
	textField.setColumns(10);

	JLabel lblZuZahlen = new JLabel("Zu Zahlen");
	lblZuZahlen.setBounds(253, 612, 101, 16);
	getContentPane().add(lblZuZahlen);

	JLabel lblGutscheinInfo = new JLabel("Gutschein Info");
	lblGutscheinInfo.setBounds(237, 584, 117, 16);
	getContentPane().add(lblGutscheinInfo);

	JScrollPane scrollPane_2 = new JScrollPane();
	scrollPane_2.setBounds(35, 531, 152, 103);
	getContentPane().add(scrollPane_2);

	JLabel lblKundenInfo = new JLabel("Kunden Info");
	lblKundenInfo.setBounds(35, 506, 82, 16);
	getContentPane().add(lblKundenInfo);

	JLabel lblDienstleistungen = new JLabel("Dienstleistungen");
	lblDienstleistungen.setBounds(25, 52, 126, 16);
	getContentPane().add(lblDienstleistungen);

	JLabel lblVerkufe = new JLabel("Verkäufe");
	lblVerkufe.setBounds(25, 287, 61, 16);
	getContentPane().add(lblVerkufe);
	setUndecorated(true);
    }
}
