import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Server {
	public static void main(String[] args) throws IOException {
		BufferedImage backgroundImg = ImageIO.read(new File("./rsc/bg-image1.jpg"));
		Image scaledBackground = backgroundImg.getScaledInstance(380,300,Image.SCALE_SMOOTH);
		BufferedImage logo = ImageIO.read(new File("./rsc/logoandtext.png"));
		Image logoPic = logo.getScaledInstance(180,160,Image.SCALE_SMOOTH);
		
		final JFrame frame = new JFrame("Start");
		frame.setPreferredSize(new Dimension(380,300));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		JLabel label = new JLabel(new ImageIcon(scaledBackground));
		JLabel label1 = new JLabel("", new ImageIcon(logoPic), JLabel.CENTER);
		Container container = frame.getContentPane();
		label.setLayout(new BorderLayout());
		frame.setContentPane(label);
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout());
		panel1.setOpaque(false);
		
		JLabel numOfPlayersLbl = new JLabel("Enter number of players: ");
		final JTextField numOfPlayers = new JTextField(10);
		
		numOfPlayers.setBounds(88, 58, 76, 21);
		
		numOfPlayersLbl.setBounds(10, 58, 55, 15);
		
		JButton btnNewButton = new JButton("Submit");
		btnNewButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {	
				try {
					frame.dispose();
					int numPlayers = Integer.parseInt(numOfPlayers.getText());
					ChatServer cs = new ChatServer();
					GameServer gs = new GameServer(numPlayers);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
			
		});
		btnNewButton.setBounds(78, 141, 75, 25);
		
		panel.setOpaque(false);
		frame.add(label1, BorderLayout.NORTH);
		panel.add(numOfPlayersLbl);
		panel.add(numOfPlayers);
		panel.add(btnNewButton);
		panel1.add(panel);
		frame.add(panel1, BorderLayout.CENTER);
		frame.pack();
		frame.validate();
		frame.setVisible(true);
	}
}
