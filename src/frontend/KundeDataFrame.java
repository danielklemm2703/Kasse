package frontend;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import backend.TypedJFrame;
import backend.enums.FrameType;
import database.entities.Kunde;
import datameer.com.google.common.base.Optional;

public class KundeDataFrame extends TypedJFrame {

    private static final long serialVersionUID = 5968986682054442654L;
    private JPanel _contentPane;

    /**
     * Create the frame.
     */
    public KundeDataFrame(final Optional<Kunde> kunde) {
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	_type = FrameType.KUNDENDATEN;
	setBounds(100, 100, 450, 300);
	_contentPane = new JPanel();
	_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	_contentPane.setLayout(new BorderLayout(0, 0));
	setContentPane(_contentPane);
	setUndecorated(true);
    }

}
