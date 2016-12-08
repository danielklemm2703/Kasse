package frontend;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import backend.TypedJFrame;
import backend.enums.FrameType;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

public class KasseFrame extends TypedJFrame {

    private static final long serialVersionUID = -1513580225986538916L;

    public KasseFrame() {
	_type = FrameType.KASSE;
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	setResizable(false);
	setTitle("Kasse");

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) Math.round(screenSize.getWidth());
	int height = (int) Math.round(screenSize.getHeight());

	int startingWidth = ((width / 2) - (317 / 2));
	int startingHeigth = ((height / 2) - (172 / 2));

	setBounds(startingWidth, startingHeigth, 317, 172);
	getContentPane().setLayout(null);

	JLabel label = new JLabel("Eigenverbrauch");
	label.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	label.setBounds(17, 6, 186, 29);
	getContentPane().add(label);

	JLabel label_1 = new JLabel("X");
	label_1.setHorizontalAlignment(SwingConstants.CENTER);
	label_1.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	label_1.setBounds(285, 12, 26, 16);
	getContentPane().add(label_1);
	setUndecorated(true);
    }
}
