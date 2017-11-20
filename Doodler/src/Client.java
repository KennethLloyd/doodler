import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client extends JFrame implements Runnable {
	private static JTextField textArea;
	private String un;
	private JTextArea chatArea;
	private JPanel canvas;
	private ChatServer server;
	private String serverName;
	
	private Socket socket = null;
	private Thread thread = null;
	private String in = null;
	private DataOutputStream out = null;
	private ClientThread client = null;
	private Container container = null;
	private JPanel chatbox = null;
	private JPanel canvasArea = null;
	private JPanel buttonArea = null;
	private JButton colorButton1 = null;
	private JButton colorButton2 = null;
	private JButton colorButton3 = null;
	private JPanel scoreBoard = null;
	private JLabel player1 = null;
	private JLabel player2 = null;
	private JLabel player3 = null;
	private GameClient gc = null;
	/**
	 * Launch the application.
	 * @param args
	 * @wbp.parser.entryPoint
	 */
	public Client(String uname, String serverName, int portno) {
		super("Doodler");
		this.serverName = serverName;
		this.setPreferredSize(new Dimension(1000,600));
		this.setResizable(false);
		this.pack();
		this.setLayout(new BorderLayout());
		this.container = this.getContentPane();
		this.un = uname;
		System.out.println("Connecting...");
		System.out.println("Enter to continue");
		
		scoreBoard = new JPanel(new GridLayout(0,1));
		
		scoreBoard.setBackground(Color.GRAY);
		scoreBoard.setPreferredSize(new Dimension(200, 100));
		player1 = new JLabel("Player 1: 100");
		player2 = new JLabel("Player 2: 100");
		player3 = new JLabel("Player 3: 100");
		scoreBoard.add(player1);
		scoreBoard.add(player2);
		scoreBoard.add(player3);
		this.container.add(scoreBoard, BorderLayout.WEST);
		
		try {
			gc = new GameClient(serverName, un);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    gc.setBackground(Color.white);
	    canvasArea = new JPanel(new BorderLayout());
	    canvasArea.add(gc, BorderLayout.CENTER);
	    buttonArea = new JPanel(new FlowLayout());
	    colorButton1 = new JButton("red");
	    colorButton2 = new JButton("blue");
	    colorButton3 = new JButton("black");
	    buttonArea.add(colorButton1);
	    buttonArea.add(colorButton2);
	    buttonArea.add(colorButton3);
	    canvasArea.add(buttonArea, BorderLayout.SOUTH);
	    this.container.add(canvasArea, BorderLayout.CENTER);
		
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
			gc.getThread().start();
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
		chatArea = new JTextArea(20, 20);
		Border border = BorderFactory.createLineBorder(Color.darkGray);
		chatArea.setBorder(BorderFactory.createCompoundBorder(border, 
	            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		JScrollPane sp = new JScrollPane(chatArea);
		chatArea.setEditable(false);
		
		textArea = new JTextField();
		textArea.setEditable(true);
		
		chatbox = new JPanel(new BorderLayout());
		
		chatbox.add(chatArea, BorderLayout.CENTER);
		chatbox.add(textArea, BorderLayout.SOUTH);
		
		this.container.add(chatbox, BorderLayout.EAST);
		
		textArea.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e){
		        if(e.getKeyCode() == KeyEvent.VK_ENTER){
		        	String message = textArea.getText()+"\n";
		        	textArea.setText("");
		        	send(message);
		        	
		        }
		    }

		    public void keyTyped(KeyEvent e) {
		    }

		    public void keyReleased(KeyEvent e) {
		    }
		});
	}

}
