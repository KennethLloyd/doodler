import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Start implements MouseListener {
	/**
	 * Launch the application.
	 * @param args
	 * @throws IOException 
	 */
	static Image scaledBackground;
	public static void main(String[] args) throws IOException {
		JTextField port = new JTextField(10);
		final JTextField ip = new JTextField(10);
		final JTextField uname = new JTextField(10);
		JPanel space= new JPanel();
		
		BufferedImage backgroundImg = ImageIO.read(new File("./rsc/bg-image1.jpg"));
		scaledBackground = backgroundImg.getScaledInstance(300,380,Image.SCALE_SMOOTH);
		BufferedImage logo = ImageIO.read(new File("./rsc/logoandtext.png"));
		Image logoPic = logo.getScaledInstance(180,160,Image.SCALE_SMOOTH);
		
		final JFrame frame = new JFrame("Client");
		frame.setPreferredSize(new Dimension(300,380));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		JLabel label = new JLabel(new ImageIcon(scaledBackground));
		JLabel label1 = new JLabel("", new ImageIcon(logoPic), JLabel.CENTER);
		Container container = frame.getContentPane();
		label.setLayout(new BoxLayout(label, BoxLayout.PAGE_AXIS));
		frame.setContentPane(label);
		JPanel panel = new JPanel();
		JPanel bigPanel = new JPanel(new FlowLayout());
		panel.setLayout(new GridLayout(0,1));
		JButton btnNewButton = new JButton("Connect");
		BufferedImage cBtn = ImageIO.read(new File("./rsc/connectButton.png"));
		Image btn = cBtn.getScaledInstance(35,30,Image.SCALE_SMOOTH);
		Icon i = new ImageIcon(btn);
		//btnNewButton.setIcon(i);
		JButton btnTutorial = new JButton("Tutorial");
		btnTutorial.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				Tutorial t = new Tutorial();
				
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Client client = null;
				String serverName = ip.getText();//*/"127.0.0.1";
				int portno = 1234;//Integer.parseInt(port.getText());
				String name = uname.getText();//*/"lois";
				
				try {
					frame.dispose();
					client = new Client(name, serverName,portno);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}
			
		});
		btnNewButton.setBounds(78, 141, 75, 25);
		
		port.setBounds(88, 58, 76, 21);
		ip.setBounds(88, 22, 76, 21);
		uname.setBounds(88, 94, 76, 21);
		
		JLabel ipLbl = new JLabel("IP Address: ");
		ipLbl.setBounds(50, 22, 55, 15);
		//ipLbl.setForeground(Color.WHITE);
		
		JLabel portLbl = new JLabel("Port: ");
		portLbl.setBounds(10, 58, 55, 15);
		
		
		JLabel unameLbl = new JLabel("Username: ");
		unameLbl.setBounds(50, 94, 55, 15);
		//unameLbl.setForeground(Color.WHITE);

		panel.add(ipLbl);
		panel.add(ip);
		
		panel.add(unameLbl);
		panel.add(uname);
		panel.add(btnNewButton);
		panel.add(btnTutorial);
		panel.setOpaque(false);
		bigPanel.setOpaque(false);
		space.setOpaque(false);
		
		frame.add(space);
		frame.add(label1);
		bigPanel.add(panel);
		frame.add(bigPanel);
		
		frame.pack();
		frame.validate();
		frame.setVisible(true);
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
