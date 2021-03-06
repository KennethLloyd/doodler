import java.net.InetAddress;

public class NetPlayer {
	/**
	 * The network address of the player
	 */
	private InetAddress address;
	
	/**
	 * The port number of  
	 */
	private int port;
	
	/**
	 * The name of the player
	 */
	private String name;
	/**
	 * The position of player
	 */
	private int x,y;
	private String givenWord;
	private int startPos;
	private int place;
	private int score;
	private boolean isTurn;

	/**
	 * Constructor
	 * @param name
	 * @param address
	 * @param port
	 */
	public NetPlayer(String name,InetAddress address, int port, int startPos){
		this.address = address;
		this.port = port;
		this.name = name;
		this.startPos = startPos;
		this.score = 0;
//		System.out.println("Player " + this.startPos);
	}

	/**
	 * Returns the address
	 * @return
	 */
	public InetAddress getAddress(){
		return address;
	}

	/**
	 * Returns the port number
	 * @return
	 */
	public int getPort(){
		return port;
	}
	
	public int getPlace() {
		return this.place;
		
	}
	public void setPlace(int p){
		this.place = p;
	}
	public void setScore(int s){
		this.score += s;
	}
	public int getScore() {
		return this.score;
		
	}
	/**
	 * Returns the name of the player
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Sets the X coordinate of the player
	 * @param x
	 */
	public void setX(int x){
		this.x=x;
	}
	
	
	/**
	 * Returns the X coordinate of the player
	 * @return
	 */
	public int getX(){
		return x;
	}
	
	
	/**
	 * Returns the y coordinate of the player
	 * @return
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Sets the y coordinate of the player
	 * @param y
	 */
	public void setY(int y){
		this.y=y;		
	}
	
	public int getStartPos() {
		return this.startPos;
	}
	
	public void setTurn(boolean isTurn) {
		this.isTurn = isTurn;
	}
	
	public boolean getTurn() {
		return this.isTurn;
	}

	/**
	 * String representation. used for transfer over the network
	 */
	public String toString(){
		String retval="";
		retval+="PLAYER ";
		retval+=name+" ";
		retval+=x+" ";
		retval+=y;
		return retval;
	}
}
