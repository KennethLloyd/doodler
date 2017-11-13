import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends JFrame implements Runnable {
	private static JTextField textArea;
	private String un;
	private JTextArea chatArea;
	private Server server;
	
	private Socket socket = null;
	private Thread thread = null;
	private String in = null;
	private DataOutputStream out = null;
	private ClientThread client = null;
	private Container container = null;
	/**
	 * Launch the application.
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public Client(String uname, String serverName, int portno) {
		super("Doodler");
		this.setSize(500,500);
		this.setLayout(new FlowLayout());
		this.container = this.getContentPane();
		this.un = uname;
		System.out.println("Connecting...");
		System.out.println("Enter to continue");
		try {
			socket = new Socket(serverName, portno);
			out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(un);
			int id = socket.getPort();
			System.out.println("Client id: " + id);
			start(); //start once connected
		}catch(Exception e) {}
		open();
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
		
	}
	/**
	 * @wbp.parser.entryPoint
	 */
	
	public void run() {
		while (thread != null) {
			try {
				if(!in.isEmpty()){
					out.writeUTF(in); //send the input to server to be distributed
					out.flush();
					in = "";
					Thread.sleep(1000);
				}
				
				start(); //start another receiver
			}catch(Exception e) {}
		}
	}
	
	public void handle(String msg) { //prints the message for itself (from ClientThread)
		if (msg.equals("bye")) {
			System.out.println("Bye");
			stop();
		}
		else {
			//System.out.println(this.chatArea.getText());
			chatArea.append(msg);
			System.out.println(msg);
		}
	}

	public void start() throws IOException {
		//ask for input
		
		out = new DataOutputStream(socket.getOutputStream()); //for server
		if (thread == null) {
			client = new ClientThread(this, socket);
			thread = new Thread(this);
			thread.start();
		}
	}
	public void send(String message){
		in = message;
	}
	
	
	public void stop() {
		if (thread != null) {
			//thread.stop();
			thread = null;
		}
		try {
			if (socket != null) socket.close();
			if (out != null) out.close();
		}catch(Exception e) {}
	}
	
	public void open() {
		chatArea = new JTextArea(5,20);
		JScrollPane sp = new JScrollPane(chatArea);
		chatArea.setEditable(false);
		
		textArea = new JTextField(20);
		textArea.setEditable(true);
		
		this.container.add(chatArea);
		this.container.add(textArea);
		
		textArea.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e){
		        if(e.getKeyCode() == KeyEvent.VK_ENTER){
		        	String message = textArea.getText()+"\n";
		        	//chatArea.append(un + ": " + message);
		        	textArea.setText("");
		        	send(message);
		        	
		        }
		    }

		    public void keyTyped(KeyEvent e) {
		    }

		    public void keyReleased(KeyEvent e) {
		    }
		});
		
		/*textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode==SWT.CR){
					if(!textArea.getText().isEmpty()){
						String message = textArea.getText()+"\n";
						chatArea.append(un+": "+ message);
						textArea.setText("");
						send(message);
					}	
				}
			}
		});
		textArea.setBounds(316, 199, 108, 21);
		
		Canvas canvas = new Canvas(shell, SWT.NONE);
		canvas.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_SELECTION_TEXT));
		canvas.setBounds(86, 21, 224, 178);
		
		List list = new List(shell, SWT.BORDER);
		list.setBounds(9, 21, 71, 102);
		
		ToolBar toolBar = new ToolBar(shell, SWT.FLAT | SWT.RIGHT);
		toolBar.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		toolBar.setBounds(86, 199, 224, 23);
		
		/*Button btnBlack = new Button(shell, SWT.NONE);
		btnBlack.setBounds(86, 197, 75, 25);
		btnBlack.setText("Black");
		
		Button btnRed = new Button(shell, SWT.NONE);
		btnRed.setBounds(157, 197, 82, 25);
		btnRed.setText("Red");
		
		Button btnRed_1 = new Button(shell, SWT.NONE);
		btnRed_1.setBounds(235, 197, 75, 25);
		btnRed_1.setText("Blue");
		
		chatArea = new Text(shell, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
		chatArea.setBounds(316, 21, 108, 178);
		
		}*/
	}

}
