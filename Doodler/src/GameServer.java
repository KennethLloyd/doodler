import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Timer;

import java.awt.event.*;

public class GameServer implements Runnable, Constants {
	/**
	 * Placeholder for the data received from the player
	 */	 
	String playerData;
	
	int turn = 1;
	
	/**
	 * The number of currently connected player
	 */
	int playerCount=0;
	
	/**
	 * The socket
	 */
    DatagramSocket serverSocket = null;
    
    /**
     * The current game state
     */
	GameState game;

	/**
	 * The current game stage
	 */
	int gameStage=WAITING_FOR_PLAYERS;
	
	/**
	 * Number of players
	 */
	int numPlayers;
	
	/**
	 * The main game thread
	 */
	Thread t = new Thread(this);
	
	Timer timer = null;
	/**
	 * Simple constructor
	 */
	public GameServer(final int numPlayers){
		this.numPlayers = numPlayers;
		try {
            serverSocket = new DatagramSocket(PORT);
			serverSocket.setSoTimeout(100);
		} catch (IOException e) {
            System.err.println("Could not listen on port: "+PORT);
            System.exit(-1);
		}catch(Exception e){}
		//Create the game state
		game = new GameState();
		
		System.out.println("Game created...");
		
		timer = new Timer(10000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (turn != numPlayers) {
					turn++;
				}
				else {
					turn = 1;
				}
			}
			
		});
		//Start the game thread
		t.start();
	}
	
	/**
	 * Helper method for broadcasting data to all players
	 * @param msg
	 */
	public void broadcast(String msg){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			NetPlayer player=(NetPlayer)game.getPlayers().get(name);			
			send(player,msg);	
		}
	}
	
	public void broadcastClear(String senderName){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			if (!name.equals(senderName)) {
				NetPlayer player=(NetPlayer)game.getPlayers().get(name);
				send(player, "CLEAR ");
			}
		}
	}
	public void broadcastChangeRed(String senderName){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			if (!name.equals(senderName)) {
				NetPlayer player=(NetPlayer)game.getPlayers().get(name);
				send(player, "RED ");
			}
		}
	}
	public void broadcastChangeBlue(String senderName){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			if (!name.equals(senderName)) {
				NetPlayer player=(NetPlayer)game.getPlayers().get(name);
				send(player, "BLUE ");
			}
		}
	}
	public void broadcastChangeBlack(String senderName){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			if (!name.equals(senderName)) {
				NetPlayer player=(NetPlayer)game.getPlayers().get(name);
				send(player, "BLACK ");
			}
		}
	}


	/**
	 * Send a message to a player
	 * @param player
	 * @param msg
	 */
	public void send(NetPlayer player, String msg){
		DatagramPacket packet;	
		byte buf[] = msg.getBytes();		
		packet = new DatagramPacket(buf, buf.length, player.getAddress(),player.getPort());
		try{
			serverSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	/**
	 * The juicy part
	 */
	public void run(){
		int startPos = 1;
		while(true){
						
			// Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try{
     			serverSocket.receive(packet);
			}catch(Exception ioe){}
			
			/**
			 * Convert the array of bytes to string
			 */
			playerData=new String(buf);
			
			//remove excess bytes
			playerData = playerData.trim();
			//if (!playerData.equals("")){
			//	System.out.println("Player Data:"+playerData);
			//}
		
			// process
			switch(gameStage){
				  case WAITING_FOR_PLAYERS:
						//System.out.println("Game State: Waiting for players...");
						if (playerData.startsWith("CONNECT")){
							String tokens[] = playerData.split(" ");
							NetPlayer player=new NetPlayer(tokens[1],packet.getAddress(),packet.getPort(), startPos);
							startPos++;
							System.out.println("Player connected: "+tokens[1]);
							game.update(tokens[1].trim(),player);
							broadcast("CONNECTED "+tokens[1]);
							playerCount++;
							if (playerCount==numPlayers){
								gameStage=GAME_START;
							}
						}
					  break;	
				  case GAME_START:
					  System.out.println("Game State: START");
					  broadcast("START");
					  gameStage=IN_PROGRESS;
					  timer.start();
					  break;
				  case IN_PROGRESS:
					  //System.out.println("Game State: IN_PROGRESS");
					  
					  //Player data was received!
					  if (playerData.startsWith("PLAYER")){
						  //Tokenize:
						  //The format: PLAYER <player name> <x> <y>
						  String[] playerInfo = playerData.split(" ");					  
						  String pname = playerInfo[1];
						  int x = Integer.parseInt(playerInfo[2].trim());
						  int y = Integer.parseInt(playerInfo[3].trim());
						  //Get the player from the game state
						  NetPlayer player=(NetPlayer)game.getPlayers().get(pname);
						  if (turn == player.getStartPos()) { //can only draw if its their turn
							  player.setX(x);
							  player.setY(y);
							  //Update the game state
							  game.update(pname, player);
							  //Send to all the updated game state
							  broadcast(game.toString());  
						  }	
					  }
					  
					  else if (playerData.startsWith("CLEAR")) {
						  String[] playerInfo = playerData.split(" ");					  
						  String pname = playerInfo[1];
						  NetPlayer player=(NetPlayer)game.getPlayers().get(pname);
						  if (turn == player.getStartPos()) {
							  broadcastClear(pname);  
						  }
					  }
					  else if (playerData.startsWith("RED")) {
						  String[] playerInfo = playerData.split(" ");					  
						  String pname = playerInfo[1];
						  broadcastChangeRed(pname);
					  }
					  else if (playerData.startsWith("BLUE")) {
						  String[] playerInfo = playerData.split(" ");					  
						  String pname = playerInfo[1];
						  broadcastChangeBlue(pname);
					  }
					  else if (playerData.startsWith("BLACK")) {
						  String[] playerInfo = playerData.split(" ");					  
						  String pname = playerInfo[1];
						  broadcastChangeBlack(pname);
					  }
					  break;
					
			}				  
		}
	}	
}
