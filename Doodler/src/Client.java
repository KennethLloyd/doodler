import java.net.*;
import java.util.*;
import java.io.*;

public class Client implements Runnable {
	private Socket socket = null;
	private Thread thread = null;
	private String in = null;
	private DataOutputStream out = null;
	private ClientThread client = null;
	private String un = null;

	public Client(String serverName, int port) {
		System.out.println("Connecting...");Scanner sc = new Scanner(System.in);
		System.out.print("Enter username: "); //temporary (username should be asked on client-side)
		un = sc.nextLine();
		try {
			socket = new Socket(serverName, port);
			out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(un);
			start(); //start once connected
		}catch(Exception e) {}
	}

	public void run() {
		while (thread != null) {
			try {
				out.writeUTF(in); //send the input to server to be distributed
				out.flush();
				start(); //avoid loop printing
			}catch(Exception e) {}
		}
	}

	public void handle(String msg) {
		if (msg.equals("bye")) {
			System.out.println("Bye");
			stop();
		}
		else {
			System.out.println(msg);
		}
	}

	public void start() throws IOException {
		//ask for input
		Scanner sc = new Scanner(System.in);
		in = sc.nextLine();
		out = new DataOutputStream(socket.getOutputStream()); //for server
		if (thread == null) {
			client = new ClientThread(this, socket);
			thread = new Thread(this);
			thread.start();
		}
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

	public static void main(String[] args) {
		Client client = null;
		String serverName = args[0];
		int port = Integer.parseInt(args[1]);
		client = new Client(serverName,port);
	}
}

