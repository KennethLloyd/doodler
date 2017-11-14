import java.net.*;
import java.util.*;
import java.io.*;

public class ClientThread extends Thread {
	private Socket socket = null;
	private Client client = null;
	private DataInputStream in = null;

	public ClientThread(Client client, Socket socket) {
		this.client = client;
		this.socket = socket;
		open();
		start();
	}

	public void open() {
		try {
			in = new DataInputStream(socket.getInputStream()); //open for reading
		}catch(Exception e) {}
	}

	public void close() {
		try {
			if (in != null) {
				in.close();
			}
		}catch(Exception e) {}
	}

	public void run() {
		while(true) {
			try {
				client.handle(in.readUTF()); //prints the received message to terminal
			}catch(Exception e) {}
		}
	}
}
