package frontend.kasse;

import static backend.framemanagement.FrameManager.closeFrameMouseAdapter;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

import backend.TypedJFrame;
import backend.enums.FrameType;
import database.entities.Friseur;
import database.entities.Kunde;
import datameer.com.google.common.base.Optional;

public class DienstleisungChoserFrame extends TypedJFrame {

    private static final long serialVersionUID = 6060625047333936877L;

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
	lblX.addMouseListener(closeFrameMouseAdapter(this));
	lblX.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
	lblX.setBounds(421, 10, 13, 16);
	getContentPane().add(lblX);
	setUndecorated(true);
    }
}
