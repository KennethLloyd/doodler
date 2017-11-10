import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class Start {
	private static Text port;
	private static Text ip;
	private static Text uname;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell();
		shell.setSize(262, 228);
		shell.setText("SWT Application");
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				Client client = null;
				String serverName = /*ip.getText();*/"127.0.0.1";
				int portno = 80;//Integer.parseInt(port.getText());
				String name = uname.getText();//"lois";
				
				try {
					//
					client = new Client(serverName,portno,name);
					UI window = new UI(name, client);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				
			}
		});
		btnNewButton.setBounds(78, 141, 75, 25);
		btnNewButton.setText("Connect");
		
		port = new Text(shell, SWT.BORDER);
		port.setBounds(78, 58, 76, 21);
		
		ip = new Text(shell, SWT.BORDER);
		ip.setBounds(78, 22, 76, 21);
		
		Label ipLbl = new Label(shell, SWT.NONE);
		ipLbl.setBounds(10, 22, 55, 15);
		ipLbl.setText("IP Address");
		
		Label portLbl = new Label(shell, SWT.NONE);
		portLbl.setBounds(10, 58, 55, 15);
		portLbl.setText("Port ");
		
		uname = new Text(shell, SWT.BORDER);
		uname.setBounds(78, 94, 76, 21);
		
		Label unameLbl = new Label(shell, SWT.NONE);
		unameLbl.setBounds(10, 94, 55, 15);
		unameLbl.setText("Username");

		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
