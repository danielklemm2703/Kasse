package frontend.kunde;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import util.Try;
import util.Unit;
import backend.TypedJFrame;
import backend.framemanagement.FrameManager;
import database.entities.Kunde;
import database.enums.FrameType;
import frontend.util.Notification;

public class KundeDeleteFrame extends TypedJFrame {

    private static final long serialVersionUID = 6762461508614533804L;
    private JPanel _contentPane;

    /**
     * Create the frame.
     * 
     * @param kunde
     */
    public KundeDeleteFrame(final Kunde kunde) {
	_type = FrameType.CHECK;
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setUndecorated(true);

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int width = (int) Math.round(screenSize.getWidth());
	int height = (int) Math.round(screenSize.getHeight());

	int startingWidth = ((width / 2) - (273 / 2));
	int startingHeigth = ((height / 2) - (165 / 2));

	setBounds(startingWidth, startingHeigth, 273, 165);
	_contentPane = new JPanel();
	_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	setContentPane(_contentPane);
	_contentPane.setLayout(null);

	JLabel lblKundeLschen = new JLabel("Kunde Löschen");
	lblKundeLschen.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	lblKundeLschen.setBounds(6, 6, 165, 25);
	_contentPane.add(lblKundeLschen);

	JLabel lblSollDerKunde = new JLabel("Soll der Kunde:");
	lblSollDerKunde.setFont(new Font("Lucida Grande", Font.BOLD, 14));
	lblSollDerKunde.setBounds(6, 43, 125, 16);
	_contentPane.add(lblSollDerKunde);

	JLabel lblNewLabel = new JLabel(kunde.getNachname() + ", " + kunde.getVorname());
	lblNewLabel.setBounds(6, 71, 180, 16);
	_contentPane.add(lblNewLabel);

	JLabel lblWirklichGelschtWerden = new JLabel("wirklich gelöscht werden?");
	lblWirklichGelschtWerden.setFont(new Font("Lucida Grande", Font.BOLD, 14));
	lblWirklichGelschtWerden.setBounds(6, 99, 192, 16);
	_contentPane.add(lblWirklichGelschtWerden);

	JButton btnJa = new JButton("Ja");
	btnJa.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	btnJa.addActionListener(closeKundeDeleteFrame(true, this, kunde));
	btnJa.setBounds(6, 127, 117, 29);
	_contentPane.add(btnJa);

	JButton btnNein = new JButton("Nein");
	btnNein.setFont(new Font("Lucida Grande", Font.PLAIN, 18));
	btnNein.addActionListener(closeKundeDeleteFrame(false, this, kunde));
	btnNein.setBounds(145, 127, 117, 29);
	_contentPane.add(btnNein);
    }

    private static final ActionListener closeKundeDeleteFrame(final boolean deleteKunde, final KundeDeleteFrame kundeDeleteFrame, final Kunde kunde) {
	return new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		FrameManager.closeFrameOnTop(kundeDeleteFrame);
		if (deleteKunde) {
		    Try<Unit> delete = kunde.delete();
		    if (delete.isSuccess()) {
			FrameManager.addFrame(new Notification(false, "Kunde wurde erfolgreich", "gelöscht."));
		    } else {
			FrameManager.addFrame(new Notification(true, "Kunde konnte nicht", "gelöscht werden"));
		    }
		}
	    }
	};
    }
}
