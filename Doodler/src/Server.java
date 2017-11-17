
public class Server {
	public static void main(String[] args) {
		int numPlayers = 2;
		ChatServer cs = new ChatServer();
		GameServer gs = new GameServer(numPlayers);
	}
}
