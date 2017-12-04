import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;

public class GameClient extends JPanel implements Runnable, Constants {
	/**
	 * Main window
	 */
	
	/**
	 * Player position, speed etc.
	 */
	int x=10,y=10,xspeed=2,yspeed=2,prevX,prevY;
	
	/**
	 * Game timer, handler receives data from server to update game state
	 */
	Thread t=new Thread(this);
	
	/**
	 * Nice name!
	 */
	String name="Joseph";
	String currentPlayer;
	
	/**
	 * Player name of others
	 */
	String pname;
	
	/**
	 * Server to connect to
	 */
	String server="localhost";
	/**
	 * Flag to indicate whether this player has connected or not
	 */
	boolean connected = false;
	boolean gameStart = false;
	
	/**
	 * get a datagram socket
	 */
    DatagramSocket socket = new DatagramSocket();

	
    /**
     * Placeholder for data received from server
     */
	String serverData;
	/**	
	 * Offscreen image for double buffering, for some
	 * real smooth animation :)
	 */
	BufferedImage offscreen;
	NetPlayer netplayer;
	
	int x1, y1, y2, x2;
	private boolean receivedClear = false;
	private boolean clearItself = false;
	private boolean receivedRed = false;
	private boolean receivedBlack = false;
	private boolean receivedBlue = false;
	private boolean receivedYellow = false;
	private boolean receivedGreen = false;
	private boolean receivedWhite = false;
	private boolean receivedPink = false;
	
	private Color colorSelected;
	public boolean isTurn = false;
	public boolean newRound = false;
	public boolean endgame = false;
	public boolean resScore = false;
	private String givenWord = null;
	private int numPlayers;

	private String[] names;

	public String[] scores ;
	/**
	 * Basic constructor
	 * @param server
	 * @param name
	 * @throws Exception
	 */
	public GameClient(String server,final String name) throws Exception{
		this.server=server;
		this.name=name;
		
		//set some timeout for the socket
		socket.setSoTimeout(100);
		this.setBackground(Color.BLACK);
		this.addMouseMotionListener(new MouseMotionListener() {

			public void mouseDragged(MouseEvent e) {
				x=e.getX();y=e.getY();
				if (prevX != x || prevY != y){
					send("PLAYER "+name+" "+x+" "+y);
				}
				 // Now Paint the line				
			}
			
			public void mousePressed(MouseEvent e) {
				 x1 = e.getX();
				 y1 = e.getY();
			}

			public void mouseMoved(MouseEvent me) {
				
			}
			
		});
	
	}
	
	public Thread getThread() {
		return this.t;
	}
	
	/**
	 * Helper method for sending data to server
	 * @param msg
	 */
	public void send(String msg){
		try{
			byte[] buf = msg.getBytes();
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, PORT);
        	socket.send(packet);
        }catch(Exception e){}
		
	}
	
	/**
	 * The juicy part!
	 */
	public void run(){
		//create the buffer
		offscreen = (BufferedImage)this.createImage(640, 640);
		
		while(true){
			try{
				Thread.sleep(1);
			}catch(Exception ioe){}
						
			//Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try{
     			socket.receive(packet);
			}catch(Exception ioe){/*lazy exception handling :)*/}
			
			serverData=new String(buf);
			serverData=serverData.trim();

			//Study the following kids. 
			if (!connected && serverData.startsWith("CONNECTED")){
				connected=true;
				System.out.println("Connected.");
			}else if (!connected){
				System.out.println("Connecting..");				
				send("CONNECT "+name);
			}else if (connected && serverData.startsWith("CLEAR")) {
				receivedClear = true;
				clearPane();
			}else if (connected && serverData.startsWith("RED")) {
				receivedRed = true;
				changeColorRed(Color.RED);
			}else if (connected && serverData.startsWith("BLACK")) {
				receivedBlack = true;
				changeColorBlack(Color.BLACK);
			}else if (connected && serverData.startsWith("BLUE")) {
				receivedBlue = true;
				changeColorBlue(Color.BLUE);
			}else if (connected && serverData.startsWith("YELLOW")) {
				receivedYellow = true;
				changeColorYellow(Color.YELLOW);
			}else if (connected && serverData.startsWith("GREEN")) {
				receivedGreen = true;
				changeColorGreen(Color.GREEN);
			}else if (connected && serverData.startsWith("WHITE")) {
				receivedWhite = true;
				changeColorWhite(Color.WHITE);
			}else if (connected && serverData.startsWith("PINK")) {
				receivedPink = true;
				changeColorPink(Color.PINK);
			}
			else if (connected && serverData.startsWith("YOURTURN")) {
				isTurn = true;
				String[] data = serverData.split(" ");
				givenWord = data[1];
				currentPlayer = data[2];
			}
			else if (connected && serverData.startsWith("NOTYOURTURN")) {
				isTurn = false;
				String[] data = serverData.split(" ");
				givenWord = data[1];
				currentPlayer = data[2];
			}
			else if (connected && serverData.startsWith("GAMESTART")) {
				gameStart = true;
			}
			else if (connected && serverData.startsWith("NEWROUND")) {
				newRound = true;
			}
			else if (connected && serverData.startsWith("NUMPLAYERS")) {
				String[] data = serverData.split(" ");
				numPlayers = Integer.parseInt(data[1]);
			}
			else if (connected && serverData.startsWith("PLAYERNAMES")) {
				String[] data = serverData.split(" ");
				String pn = data[1];
				names = pn.split(";");
			}
			else if (connected && serverData.startsWith("SCORES")) {
				String[] data = serverData.split(" ");
				String pn = data[1];
				scores = pn.split(";");
				System.out.println("SCORE SETTING");
				resScore = true;
			}
			else if (connected && serverData.startsWith("ENDGAME")) {
				endgame = true;
			}
			else if (connected){
				//offscreen.getGraphics().clearRect(0, 0, 640, 480);
				if (serverData.startsWith("PLAYER")){
					String[] playersInfo = serverData.split(":");
					for (int i=0;i<playersInfo.length;i++){
						String[] playerInfo = playersInfo[i].split(" ");
						String pname = playerInfo[1];
						int x = Integer.parseInt(playerInfo[2]);
						int y = Integer.parseInt(playerInfo[3]);
						//draw on the offscreen image
						//offscreen.getGraphics().setColor(this.colorSelected);
						
						Graphics gd = offscreen.getGraphics();
						gd.setColor(this.colorSelected);
						gd.fillOval(x, y, 5, 5);		
						
					}
					//show the changes
					this.repaint();
				}			
			}			
		}
	}
	
	/**
	 * Repainting method
	 */
	public void paintComponent(Graphics g){
		g.drawImage(offscreen, 0, 0, null);
	}
	public void changeColorRed(Color c){
		this.colorSelected = c;
		if (!receivedRed) {
			send("RED " + name);
		}
		this.receivedRed = false;
	}
	public void changeColorBlue(Color c){
		this.colorSelected = c;
		if (!receivedBlue){
			send("BLUE " + name);
		}
		this.receivedBlue = false;
	}
	public void changeColorBlack(Color c){
		this.colorSelected = c;
		if (!receivedBlack) {
			send("BLACK " + name);
		}
		this.receivedBlack = false;
	}
	public void changeColorYellow(Color c){
		this.colorSelected = c;
		if (!receivedYellow) {
			send("YELLOW " + name);
		}
		this.receivedYellow = false;
	}
	public void changeColorGreen(Color c){
		this.colorSelected = c;
		if (!receivedGreen) {
			send("GREEN " + name);
		}
		this.receivedGreen = false;
	}
	public void changeColorWhite(Color c){
		this.colorSelected = c;
		if (!receivedWhite) {
			send("WHITE " + name);
		}
		this.receivedWhite = false;
	}
	public void changeColorPink(Color c){
		this.colorSelected = c;
		if (!receivedPink) {
			send("PINK " + name);
		}
		this.receivedPink = false;
	}
		
	public void clearPane(){
		if (isTurn || receivedClear) { //can only clear if its his turn or the current players clear itself
			offscreen = (BufferedImage)this.createImage(640, 640);
			repaint();
			if (!receivedClear || clearItself) send("CLEAR " + name);
			clearItself = false;
			receivedClear = false;
		}
		else {
			clearItself = false;
		}
	}
	
	public void setClearItself(boolean clearItself) {
		this.clearItself = clearItself;
	}
	
	public String getWord() {
		return this.givenWord;
	}
	
	public boolean getGameStart() {
		return this.gameStart;
	}
	
	public boolean getNewRound() {
		return this.newRound;
	}
	
	public String getCurrentPlayer() {
		return this.currentPlayer;
	}
	
	public int getNumPlayers() {
		return this.numPlayers;
	}
	
	public boolean getReceivedScore() {
		return this.resScore;
	}
	
	public void setReceivedScore(boolean b) {
		this.resScore = b;
	}
	
	public String[] getNames() {
		return this.names;
	}
	public void sendHasGuessed() {
		send("GUESSED " + name);
	}
	@SuppressWarnings("deprecation")
	public void stopThread(){
		this.t.stop();
	}

	class KeyHandler extends KeyAdapter{
		public void keyPressed(KeyEvent ke){
			prevX=x;prevY=y;
			switch (ke.getKeyCode()){
			case KeyEvent.VK_DOWN:y+=yspeed;break;
			case KeyEvent.VK_UP:y-=yspeed;break;
			case KeyEvent.VK_LEFT:x-=xspeed;break;
			case KeyEvent.VK_RIGHT:x+=xspeed;break;
			}
			if (prevX != x || prevY != y){
				send("PLAYER "+name+" "+x+" "+y);
			}	
		}
	}

	public void setGameStart(boolean b) {
		this.gameStart = b;
	}
}
   
