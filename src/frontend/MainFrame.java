package frontend;

import static backend.framemanagement.FrameManager.checkOtherOpenFrames;
import static backend.framemanagement.FrameManager.onMinimize;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;

import util.Images;
import util.Try;
import backend.framemanagement.ActionListeners;
import frontend.util.ImagePanel;

public class MainFrame {

    private JFrame _frame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    MainFrame window = new MainFrame();
		    window._frame.setVisible(true);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    /**
     * Create the application.
     */
    public MainFrame() {
	initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
	_frame = new JFrame();
	_frame.setIconImage(Toolkit.getDefaultToolkit().getImage("/Users/dklemm/Documents/workspace/Kasse/kassaicon.ico"));
	_frame.setTitle("Kasse");
	_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	_frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
	_frame.getContentPane().setLayout(null);
	_frame.addWindowFocusListener(checkOtherOpenFrames());
	_frame.addWindowStateListener(onMinimize());

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) Math.round(screenSize.getWidth());
	int height = (int) Math.round(screenSize.getHeight());

	Try<BufferedImage> image = Images.loadImage("1.png");
	if (image.isSuccess()) {
	    _frame.setContentPane(new ImagePanel(Images.resize(image.get(), width, height)));
	} else {
	    System.err.println("could not load image '1.png', reason");
	    System.err.println(image.failure().getMessage());
	}

	int totalWidth = 266;
	int starting = ((width / 2) - (totalWidth / 2)) / 2;

	JButton kasseBtn = new JButton("Kasse");
	kasseBtn.addActionListener(ActionListeners.openKasseFrameListener);
	kasseBtn.setBackground(Color.GRAY);
	kasseBtn.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	kasseBtn.setBounds(22, starting, 205, 45);
	_frame.getContentPane().add(kasseBtn);

	JButton eigenVerbrauchBtn = new JButton("Eigenverbrauch");
	eigenVerbrauchBtn.addActionListener(ActionListeners.openEigenverbrauchFrameListener);
	eigenVerbrauchBtn.setBackground(Color.GRAY);
	eigenVerbrauchBtn.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	eigenVerbrauchBtn.setBounds(22, starting + 80, 205, 45);
	_frame.getContentPane().add(eigenVerbrauchBtn);

	JButton kundenBtn = new JButton("Kunden");
	kundenBtn.addActionListener(ActionListeners.openKundeFrameListener);
	kundenBtn.setBackground(Color.GRAY);
	kundenBtn.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	kundenBtn.setBounds(22, starting + 160, 205, 45);
	_frame.getContentPane().add(kundenBtn);

	JButton adminBereichBtn = new JButton("Admin Bereich");
	adminBereichBtn.addActionListener(ActionListeners.openAdminFrameListener);
	adminBereichBtn.setBackground(Color.GRAY);
	adminBereichBtn.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	adminBereichBtn.setBounds(22, starting + 240, 205, 45);
	_frame.getContentPane().add(adminBereichBtn);
    }
}
