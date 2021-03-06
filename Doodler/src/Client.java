import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class Client extends JFrame implements Runnable, ActionListener {
	private static JPanel anotherBigPanel;
	private static JPanel mainPanel;
	private static JPanel timerPanel;
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
	private JPanel colorButtons = null;
	private JButton colorButton1 = null;
	private JButton colorButton2 = null;
	private JButton colorButton3 = null;
	private JButton colorButton4 = null;
	private JButton colorButton5 = null;
	private JButton colorButton6 = null;
	private JButton colorButton7 = null;
	private JButton colorButton8 = null;
	private JButton clearButton = null;
	private ScoreDisplay scoreBoard = null;
	private JLabel player1 = null;
	private JLabel player2 = null;
	private JLabel player3 = null;
	private JLabel currentPlayer = null;

	private JLabel time = null;
	private boolean ownAnswer = false;
	
	private String currentPlayerName = "";
	private Timer timer = null;
	private GameClient gc = null;
	private WordDisplay answerPanel = null;


	private int count = 80;
	/**
	 * Launch the application.
	 * @param args
	 * @throws IOException 
	 * @wbp.parser.entryPoint
	 */
	public Client(String uname, String serverName, int portno) throws IOException {
		super(uname);
		this.serverName = serverName;
		this.setPreferredSize(new Dimension(1000,600));
		this.setResizable(false);
		this.pack();
		this.container = this.getContentPane();
		this.un = uname;
		System.out.println("Enter to continue");
		
		JPanel space1 = new JPanel();
		space1.setOpaque(false);
		JPanel space2 = new JPanel();
		space2.add(new JLabel(""));
		space2.setOpaque(false);
		JPanel space3 = new JPanel();
		space3.setOpaque(false);
		JPanel space4 = new JPanel();
		space4.setOpaque(false);
		JPanel space5 = new JPanel();
		space5.setOpaque(false);
		
		BufferedImage backgroundImg = ImageIO.read(new File("./rsc/bg-image.jpg"));
		Image scaledBackground = backgroundImg.getScaledInstance(1000,600,Image.SCALE_SMOOTH);
		//this.setLayout(new BorderLayout());
		JLabel label = new JLabel(new ImageIcon(scaledBackground));
		label.setLayout(new BorderLayout());
		this.setContentPane(label);
		
		timerPanel = new JPanel(new FlowLayout());
		timerPanel.setBackground(Color.WHITE);
		BufferedImage timerImg = ImageIO.read(new File("./rsc/timer.png"));
		Image timerImg1 = timerImg.getScaledInstance(60,60,Image.SCALE_SMOOTH);
		JLabel label1 = new JLabel("", new ImageIcon(timerImg1), SwingConstants.LEFT);
		currentPlayer = new JLabel();
		time = new JLabel("80");
		time.setFont(new Font("Serif", Font.PLAIN, 30));
		timerPanel.add(new JLabel(" "));
		timerPanel.add(label1);
		timerPanel.add(time);
		timerPanel.add(new JLabel(" "));
		timerPanel.add(currentPlayer);
		
		mainPanel = new JPanel(new BorderLayout());

		try {
			gc = new GameClient(serverName, un);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
		JPanel scorePanel = new JPanel(new GridLayout(1,0));
		scoreBoard = new ScoreDisplay(gc);
		scorePanel.add(scoreBoard);
		
		this.mainPanel.add(scorePanel, BorderLayout.WEST);
		
	    canvasArea = new JPanel(new BorderLayout());
	    canvasArea.add(gc, BorderLayout.CENTER);
	    buttonArea = new JPanel(new GridLayout(2,3));
	    buttonArea.setBackground(Color.BLACK);
	    
	    answerPanel = new WordDisplay(gc);
	    answerPanel.setBackground(Color.GREEN);
	    canvasArea.add(answerPanel, BorderLayout.NORTH);
	    
	    colorButton1 = new JButton();
	    colorButton1.setBackground(Color.RED);
	    colorButton2 = new JButton();
	    colorButton2.setBackground(Color.BLUE);
	    colorButton3 = new JButton();
	    colorButton3.setBackground(Color.BLACK);
	    colorButton4 = new JButton();
	    colorButton4.setBackground(Color.YELLOW);
	    colorButton5 = new JButton();
	    colorButton5.setBackground(Color.GREEN);
	    colorButton6 = new JButton();
	    colorButton6.setBackground(Color.WHITE);
	    colorButton7 = new JButton();
	    colorButton7.setBackground(Color.ORANGE);
	    colorButton7 = new JButton();
	    colorButton7.setBackground(Color.PINK);
	    clearButton = new JButton("clear");
	    
	    colorButton1.addActionListener(this);
	    colorButton2.addActionListener(this);
	    colorButton3.addActionListener(this);
	    colorButton4.addActionListener(this);
	    colorButton5.addActionListener(this);
	    colorButton6.addActionListener(this);
	    colorButton7.addActionListener(this);
	    clearButton.addActionListener(this);
	    
	    buttonArea.add(colorButton1);
	    buttonArea.add(colorButton2);
	    buttonArea.add(colorButton3);
	    buttonArea.add(colorButton4);
	    buttonArea.add(colorButton5);
	    buttonArea.add(colorButton6);
	    buttonArea.add(colorButton7);
	    buttonArea.add(clearButton);
	    
	    scorePanel.setOpaque(false);
	    mainPanel.setOpaque(false);
	    
	    anotherBigPanel = new JPanel();
	    anotherBigPanel.setLayout(new BoxLayout(anotherBigPanel, BoxLayout.PAGE_AXIS));
	    anotherBigPanel.setOpaque(false);
	    
	    canvasArea.add(buttonArea, BorderLayout.SOUTH);
	    this.mainPanel.add(canvasArea, BorderLayout.CENTER);
	    this.add(space1, BorderLayout.NORTH);
	    anotherBigPanel.add(timerPanel);
	    anotherBigPanel.add(space2);
		anotherBigPanel.add(mainPanel);
		this.add(anotherBigPanel, BorderLayout.CENTER);
		this.add(space3, BorderLayout.SOUTH);
	    this.add(space4, BorderLayout.EAST);
	    this.add(space5, BorderLayout.WEST);
	    
	    timer = new Timer(1000, new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            if (count <= 0) {
	                time.setText("TIME'S UP!");
	                time.setFont(new Font("Serif", Font.PLAIN, 30));
	                ((Timer)e.getSource()).stop();
	                count = 80;
	                resetTimer();
	            } else {
	                time.setText(Integer.toString(count));
	                time.setFont(new Font("Serif", Font.PLAIN, 30));
	                count--;
	            }
	        }
	    });
	    
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
	
	public void resetTimer() {
		timer.stop();
		timer.start();
	}
	
	public void run() {
		while (thread != null) {
			if (gc.endgame == true) {
				timer.stop();
				this.stop();
				time.setText("GAME OVER");
				time.setFont(new Font("Serif", Font.PLAIN, 30));
				gc.newRound = false;
				//gameOver(true);
				try {
					gameOver(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.stop();
				
			}
			if (gc.getNewRound() == true) {
				timer.stop();
				count = 80;
				time.setText("80");
				timer.setInitialDelay(0);
				timer.start();
				gc.newRound = false;
			}
			
			if (currentPlayerName != gc.getCurrentPlayer()) {
				currentPlayerName = gc.getCurrentPlayer();
				currentPlayer.setText(currentPlayerName + "'s TURN");
			}
			if (gc.getGameStart() == true) {
				scoreBoard.setNumPlayers(gc.getNames(),gc.getNumPlayers());
				timer.start();
				gc.setGameStart(false);
			}
			if (client == null) client = new ClientThread(this, socket);

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
		String[] sub = msg.split(": ");
		if (msg.equals("bye")) {
			System.out.println("Bye");
			stop();
		}
		if(sub[1].toLowerCase().equals(gc.getWord().toLowerCase()+"\n") && !this.gc.isTurn && ownAnswer == true) {
			if(!gc.getHasGuessed()){
				ownAnswer = false;
				gc.sendHasGuessed();
			}
			chatArea.append(sub[0]+": CORRECT ANSWER\n");
		}
		else if(sub[1].toLowerCase().equals(gc.getWord().toLowerCase()+"\n") && !this.gc.isTurn && ownAnswer == false){
			chatArea.append(sub[0]+": CORRECT ANSWER\n");
		}
		else {
			//System.out.println(this.chatArea.getText());
			ownAnswer = false;
			chatArea.append(msg);
//			System.out.println(msg);
		}
	}

	public void start() throws IOException {
		//ask for input
		
		out = new DataOutputStream(socket.getOutputStream()); //for server
		if (thread == null) {
			//client = new ClientThread(this, socket);
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
		sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		textArea = new JTextField();
		textArea.setEditable(true);
		
		chatbox = new JPanel(new BorderLayout());
		
		chatbox.add(sp, BorderLayout.CENTER);
		chatbox.add(textArea, BorderLayout.SOUTH);
		
		this.mainPanel.add(chatbox, BorderLayout.EAST);
		
		textArea.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e){
		        if(e.getKeyCode() == KeyEvent.VK_ENTER){
		        	String message = textArea.getText()+"\n";
		        	textArea.setText("");

		        	ownAnswer = true;
		        	send(message);
		        }
		    }

		    public void keyTyped(KeyEvent e) {
		    }

		    public void keyReleased(KeyEvent e) {
		    }
		});
	}
	//@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		if(arg0.getSource().equals(colorButton1)){
			gc.changeColorRed(Color.RED);
		}
		else if(arg0.getSource().equals(colorButton2)){
			gc.changeColorBlue(Color.BLUE);
		}
		else if(arg0.getSource().equals(colorButton3)){
			gc.changeColorBlack(Color.BLACK);
		}else if(arg0.getSource().equals(colorButton4)){
			gc.changeColorYellow(Color.YELLOW);
		}else if(arg0.getSource().equals(colorButton5)){
			gc.changeColorGreen(Color.GREEN);
		}else if(arg0.getSource().equals(colorButton6)){
			gc.changeColorWhite(Color.WHITE);
		}else if(arg0.getSource().equals(colorButton7)){
			gc.changeColorPink(Color.PINK);
		}
		else if(arg0.getSource().equals(clearButton)){
			gc.setClearItself(true);
			gc.clearPane();
		}
	}
	public void gameOver(boolean b) throws IOException {
		if(b) {
			JFrame endFrame= new JFrame();
			endFrame.setPreferredSize(new Dimension(380,300));
			endFrame.setResizable(false);
			endFrame.pack();
			endFrame.setLocationRelativeTo(null);
			
			JPanel panel = new JPanel(new GridLayout(4,0));
			BufferedImage go = ImageIO.read(new File("./rsc/gameover.png"));
			Image gameover = go.getScaledInstance(150,70,Image.SCALE_SMOOTH);
			JLabel label1 = new JLabel("", new ImageIcon(gameover), SwingConstants.CENTER);
			String highScorer = this.scoreBoard.getHighScorer().toUpperCase();
			highScorer = highScorer.substring(0, highScorer.length()-2);
//			System.out.println(highScorer);
			JLabel label2 = new JLabel(highScorer, SwingConstants.CENTER);
			label2.setFont(new Font("SERIF",Font.BOLD,30));
			label2.setForeground(Color.RED);
			JLabel label3 = new JLabel("WINNER:", SwingConstants.CENTER);
			
			
			
			panel.add(label1);
			panel.add(label3);
			panel.add(new JLabel(new ImageIcon(ImageIO.read(new File("./rsc/winner-trophy.gif")))));
			panel.add(label2);
			
			endFrame.add(panel);
			
			endFrame.setVisible(true);
		
		}
	}

}

