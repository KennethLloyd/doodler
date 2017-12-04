import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.*;

import javax.swing.Timer;

import java.awt.event.*;

public class GameServer implements Runnable, Constants {
	/**
	 * Placeholder for the data received from the player
	 */	 
	String playerData;
	String currentPlayer;
	
	int turn = 0;
	
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
	static int numCorrectPlayers;
	
	/**
	 * The main game thread
	 */
	Thread t = new Thread(this);
	String currentWord;
	Timer timer = null;
	int index;
	private ArrayList<String> wordList = new ArrayList();
	private ArrayList<String> usedWords = new ArrayList();
	
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
		
		/*puts words into arrayList*/
		readFile();
		/*randomize word*/
		final Random rand = new Random();
		index = rand.nextInt(wordList.size());
		currentWord = wordList.get(index);
		usedWords.add(currentWord);
		//then notify players then give them the word
		
		//Start the game thread
		t.start();
		
		timer = new Timer(100000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numCorrectPlayers=0;
				if (turn != numPlayers) {//set next player
					turn++;
				}
				else {
					setDoodlerScore(turn,numCorrectPlayers);
					turn = 1;
					game.setRound(game.getRound()+1);
				}
				if(game.getRound()==MAX_ROUND){//set next round
					gameStage = END_GAME;
				}
				else{
					do {//get the word to draw
						index = rand.nextInt(wordList.size());
						currentWord = wordList.get(index);
						if (wordList.size() == usedWords.size()) {
							break;
						}
					}while(usedWords.contains(currentWord));
						
					usedWords.add(currentWord);

					System.out.println("NEW TURN: "+ turn);
//					if(numCorrectPlayers!=1) {//if the 
//						
//						checkDoodlerScore(turn);
//					}

					notifyPlayers();
					clearAllCanvas();
				}
			}
			
		});
	}
	
	public void getCurrentPlayerName() {
		 for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
				String name=(String)ite.next();
				NetPlayer player=(NetPlayer)game.getPlayers().get(name);			
				if (turn == player.getStartPos()) {
					currentPlayer = player.getName();
				}
		  }
	}
	
	public void checkDoodlerScore(int turn){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			NetPlayer player=(NetPlayer)game.getPlayers().get(name);			
			if (turn == player.getStartPos()) {
				player.setPlace(numCorrectPlayers);
				player.setPlace(numCorrectPlayers);
				player.setScore(MAX_SCORE-((player.getPlace()-1)*(BASE_SCORE/(numPlayers-1))));
			}
	  }
	}
	public void setDoodlerScore(int turn, int numCorrectPlayers){
		System.out.println("Entered setting doodler score");
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			NetPlayer player=(NetPlayer)game.getPlayers().get(name);			
			if (turn == player.getStartPos()) {
				System.out.println(player.getName());
				System.out.println(player.getScore());
				System.out.println(numCorrectPlayers);
				System.out.println(player.getPlace());
				player.setPlace(numCorrectPlayers);
				System.out.println("score MAX:"+MAX_SCORE);
				System.out.println("score place:"+player.getPlace());
				System.out.println("score BASE:"+BASE_SCORE);
				player.setPlace(numCorrectPlayers);
				System.out.println("score player: "+(MAX_SCORE-((player.getPlace()-1)*(BASE_SCORE/(numPlayers-1)))));
				player.setScore(MAX_SCORE-((player.getPlace()-1)*(BASE_SCORE/(numPlayers-1))));
			}
	  }
	}
	public void notifyPlayers() {
		//broadcast the current word and notify the players if its already their turn
		  for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
				String name=(String)ite.next();
				NetPlayer player=(NetPlayer)game.getPlayers().get(name);			
				if (turn == player.getStartPos()) {
					send(player, "YOURTURN " + currentWord + " " + currentPlayer);
				}
				else {
					send(player, "NOTYOURTURN " + currentWord + " " + currentPlayer);
				}
		  }
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
	public void broadcastChangeYellow(String senderName){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			if (!name.equals(senderName)) {
				NetPlayer player=(NetPlayer)game.getPlayers().get(name);
				send(player, "YELLOW ");
			}
		}
	}
	public void broadcastChangeGreen(String senderName){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			if (!name.equals(senderName)) {
				NetPlayer player=(NetPlayer)game.getPlayers().get(name);
				send(player, "GREEN ");
			}
		}
	}
	public void broadcastChangeWhite(String senderName){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			if (!name.equals(senderName)) {
				NetPlayer player=(NetPlayer)game.getPlayers().get(name);
				send(player, "WHITE ");
			}
		}
	}
	public void broadcastChangePink(String senderName){
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			if (!name.equals(senderName)) {
				NetPlayer player=(NetPlayer)game.getPlayers().get(name);
				send(player, "PINK ");
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
	
	/*read files*/
	public void readFile() {
		FileReader fr = null;
		BufferedReader br = null;
		int i=0;
		
		try {
			fr = new FileReader("./rsc/words.txt");
			br = new BufferedReader(fr);
			
			String word;

			while ((word = br.readLine()) != null) {
				wordList.add(word);
				System.out.println(word);
				i++;
			}

		} catch (IOException e) {

			e.printStackTrace();

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
					  for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
							String name=(String)ite.next();
							NetPlayer player=(NetPlayer)game.getPlayers().get(name);			
							player.setPlace((2*numPlayers)-1);
							player.setScore(MAX_SCORE-((player.getPlace()-1)*(BASE_SCORE/(numPlayers-1))));
							System.out.println(player.getPlace());
					  }
					  timer.setInitialDelay(0);
					  timer.start();
					  break;
				  case IN_PROGRESS:
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
					  else if (playerData.startsWith("YELLOW")) {
						  String[] playerInfo = playerData.split(" ");					  
						  String pname = playerInfo[1];
						  broadcastChangeYellow(pname);
					  }
					  else if (playerData.startsWith("GREEN")) {
						  String[] playerInfo = playerData.split(" ");					  
						  String pname = playerInfo[1];
						  broadcastChangeGreen(pname);
					  }
					  else if (playerData.startsWith("WHITE")) {
						  String[] playerInfo = playerData.split(" ");					  
						  String pname = playerInfo[1];
						  broadcastChangeWhite(pname);
					  }
					  else if (playerData.startsWith("PINK")) {
						  String[] playerInfo = playerData.split(" ");					  
						  String pname = playerInfo[1];
						  broadcastChangePink(pname);
					  }
					  else if (playerData.startsWith("GUESSED ")) {
						  String[] playerInfo = playerData.split(" ");					  
						  String pname = playerInfo[1];
						  System.out.println("Has Guessed");
						  for(String s:playerInfo)System.out.println(s);
						  for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
								String name=(String)ite.next();
								if (name.equals(pname)) {
									NetPlayer player=(NetPlayer)game.getPlayers().get(pname);
									System.out.println(pname);
									System.out.println(player.getScore());
									System.out.println(numCorrectPlayers+1);
									System.out.println(player.getPlace());
									player.setPlace(numCorrectPlayers+1);
									System.out.println("score MAX:"+MAX_SCORE);
									System.out.println("score place:"+player.getPlace());
									System.out.println("score BASE:"+BASE_SCORE);
									System.out.println("score guesser: "+(MAX_SCORE-((player.getPlace()-1)*(BASE_SCORE/(numPlayers-1)))));
									player.setScore(MAX_SCORE-((player.getPlace()-1)*(BASE_SCORE/numPlayers-1)));
									System.out.println(player.getScore());
								}
							}
						  numCorrectPlayers++;
						  checkIfCorrectAll();
					  }
					  break;
				  case END_GAME:
					  System.out.println("END_GAME");
					  for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
							String name=(String)ite.next();
							
							NetPlayer player=(NetPlayer)game.getPlayers().get(name);			
							System.out.println(player.getName()+" : "+player.getScore()); 
					  }
					  gameStage=5;
					  timer.stop();
					  break;
				  case STATIC_GAME:
					  break;
			}				  
		}
	}
	
	public void clearAllCanvas() { //before proceeding to next round
	  for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			NetPlayer player=(NetPlayer)game.getPlayers().get(name);//Lois:para saan to			
			broadcastClear(name); 
	  }
	}
	
	public void checkIfCorrectAll() {
		System.out.println("NUMBR: "+numPlayers);
		System.out.println("NUMBR2: "+numCorrectPlayers);
		if (numCorrectPlayers == numPlayers-1) { //all players guessed right
			numCorrectPlayers = 0;
			clearAllCanvas();
			timer.stop();
			timer.setInitialDelay(0);
			timer.start();
		}
	}
}
