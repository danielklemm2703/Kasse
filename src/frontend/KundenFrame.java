package frontend;

import static backend.FrameManager.closeOnLeave;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import backend.TypedJFrame;
import backend.enums.FrameType;

public class KundenFrame extends TypedJFrame {

    private static final long serialVersionUID = 4794193793492079259L;
    private JPanel _contentPane;

    /**
     * Create the frame.
     */
    public KundenFrame() {
	_type = FrameType.EIGENVERBRAUCH;
	addWindowFocusListener(closeOnLeave(this));
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setResizable(false);
	setTitle("Kunden");
	setUndecorated(true);

	setBounds(100, 100, 450, 300);
	_contentPane = new JPanel();
	_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	_contentPane.setLayout(new BorderLayout(0, 0));
	setContentPane(_contentPane);
    }

}
