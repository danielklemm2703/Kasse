package frontend.util;

import static backend.FrameManager.closeNotificationByButton;
import static backend.FrameManager.holdFocus;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import util.Images;
import util.Try;
import backend.TypedJFrame;
import backend.enums.FrameType;

public class Notification extends TypedJFrame {

    private static final long serialVersionUID = 1301186311827197190L;
    private JPanel _contentPane;

    /**
     * Create the frame.
     */
    public Notification(final boolean error, final String message1, final String message2) {
	setTitle("Notification");
	_type = FrameType.NOTIFICATION;
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	addWindowFocusListener(holdFocus(this));

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) Math.round(screenSize.getWidth());
	int height = (int) Math.round(screenSize.getHeight());

	int startingWidth = ((width / 2) - (282 / 2));
	int startingHeigth = ((height / 2) - (134 / 2));

	setBounds(startingWidth, startingHeigth, 282, 134);
	_contentPane = new JPanel();
	_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(_contentPane);
	_contentPane.setLayout(null);

	String title = "Erfolg";
	if(error){
	    title = "Fehler";
	}
	JLabel lblNewLabel = new JLabel(title );
	lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	lblNewLabel.setBounds(6, 6, 170, 29);
	_contentPane.add(lblNewLabel);

	JLabel lblNewLabel_1 = new JLabel(message1);
	lblNewLabel_1.setFont(new Font("Lucida Grande", Font.BOLD, 14));
	lblNewLabel_1.setBounds(75, 36, 191, 27);
	_contentPane.add(lblNewLabel_1);

	JButton btnOk = new JButton("OK");
	btnOk.addActionListener(closeNotificationByButton(this));
	btnOk.setBounds(85, 101, 117, 29);
	_contentPane.add(btnOk);

	JLabel lblZweiterText = new JLabel(message2);
	lblZweiterText.setFont(new Font("Lucida Grande", Font.BOLD, 14));
	lblZweiterText.setBounds(75, 62, 192, 27);
	_contentPane.add(lblZweiterText);

	String pic = "check.png";
	if (error) {
	    pic = "ausrufezeichen.png";
	}
	ImageIcon image = new ImageIcon();
	Try<BufferedImage> loadImage = Images.loadImage(pic);
	if (loadImage.isSuccess()) {
	    image = new ImageIcon(Images.resize(loadImage.get(), 51, 51));
	}
	JLabel lblNewLabel_2 = new JLabel(image);
	lblNewLabel_2.setBounds(16, 36, 51, 51);
	_contentPane.add(lblNewLabel_2);
	setUndecorated(true);
    }
}
