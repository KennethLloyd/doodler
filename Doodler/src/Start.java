/*import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;*/

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Start implements MouseListener {
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		/*Display display = Display.getDefault();
		Shell shell = new Shell();
		
		shell.setSize(262, 228);
		shell.setText("SWT Application");*/
		JTextField port = new JTextField(10);
		JTextField ip = new JTextField(10);
		final JTextField uname = new JTextField(10);
		
		JFrame frame = new JFrame("Start");
		frame.setPreferredSize(new Dimension(262,228));
		frame.setResizable(false);
		frame.pack();
		frame.setLayout(new FlowLayout());
		Container container = frame.getContentPane();
		
		JButton btnNewButton = new JButton("Connect");
		btnNewButton.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				Client client = null;
				String serverName = /*ip.getText();*/"127.0.0.1";
				int portno = 12345;//Integer.parseInt(port.getText());
				String name = uname.getText();//"lois";
				
				try {
					//client = new Client1(serverName,portno,name);
					client = new Client(name, serverName,portno);
					//client.setUI(window);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				
			}
			
		});
		btnNewButton.setBounds(78, 141, 75, 25);
		
		port.setBounds(78, 58, 76, 21);
		ip.setBounds(78, 22, 76, 21);
		uname.setBounds(78, 94, 76, 21);
		
		JLabel ipLbl = new JLabel("IP Address: ");
		ipLbl.setBounds(10, 22, 55, 15);
		
		JLabel portLbl = new JLabel("Port: ");
		portLbl.setBounds(10, 58, 55, 15);
		
		JLabel unameLbl = new JLabel("Username: ");
		unameLbl.setBounds(10, 94, 55, 15);

		container.add(ipLbl);
		container.add(ip);
		container.add(portLbl);
		container.add(port);
		container.add(unameLbl);
		container.add(uname);
		container.add(btnNewButton);
		
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
