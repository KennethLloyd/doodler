import java.net.*;
import java.util.*;
import java.io.*;

public class Server implements Runnable {
	private ArrayList<ServerThread> clients = new ArrayList<ServerThread>();
	private ServerSocket server = null;
	private Thread thread = null;

	public Server(int port) {
		try {
			System.out.println("Listening to port " + port);
			server = new ServerSocket(port);
			start();
		}catch(Exception e) {}
	}

	public void run() { //after creating thread
		while (thread != null) {
			try {
				System.out.println("Waiting..."); //wait for a client
				Socket s = server.accept();
				DataInputStream name = new DataInputStream(new BufferedInputStream(s.getInputStream()));
				addThread(s,name.readUTF()); //then add it to arraylist
			}catch(Exception e) {}
		}
	}

	public void start() { //will only run in the beginning
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void stop() {
		if (thread != null) {
			//thread.stop();
			thread = null;
		}
	}

	private void addThread(Socket socket, String name) throws IOException {
//		Scanner sc = new Scanner(System.in);
//		System.out.print("Enter username: "); //temporary (username should be asked on client-side)
//		String un = sc.nextLine();
//		
		
		
		System.out.println(name);
		clients.add(new ServerThread(this,socket,name)); //initialize these parameters to the thread
		try {
			clients.get(clients.size()-1).open(); //open for reading and writing the newly added client
			clients.get(clients.size()-1).start(); //start the client thread
		}catch(Exception e) {}
	}

	public synchronized void handle(String un, String input) {
		/*if (input.equals("bye")) {
			clients.get(findClient(id)).send("bye");
			remove(id);
		}
		else {*/
			for (int i=0;i<clients.size();i++) { //send inputs of each client to every clients
				clients.get(i).send(un + ": " + input);
			}
		//}
	}

	public synchronized void remove(int id) {
		/*int pos = findClient(id);
		if (pos >= 0) {
			ServerThread toTerminate = clients.get(pos);
			System.out.println("Removing client thread " + id + " at " + pos);
			if (pos < clients.size()-1) {
				for (int i=pos+1;i<clients.size();i++) {
					clients.set(i-1,clients.get(i));
				}
				clients.remove(clients.get(clients.size()-1));
			}
			try {
				toTerminate.close();
			}catch(Exception e) {}
			toTerminate.stop();
		}*/
	}

	public static void main(String[] args) {
		Server server = null;
		int port = Integer.parseInt(args[0]);
		server = new Server(port);
	}
}

