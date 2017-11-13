import java.net.*;
import java.util.*;
import java.awt.Color;
import java.io.*;

import org.eclipse.swt.widgets.Text;

public class Client implements Runnable {
	private Socket socket = null;
	private Thread thread = null;
	private String in = null;
	private DataOutputStream out = null;
	private ClientThread client = null;
	private String un = null;
	private UI ui = null;

	public Client(String serverName, int port, String un) {
		System.out.println("Connecting...");
		System.out.println("Enter to continue");
		try {
			socket = new Socket(serverName, port);
			out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(un);
			int id = socket.getPort();
			System.out.println("Client id: " + id);
			start(); //start once connected
		}catch(Exception e) {}
	}
	
	public void setUI(UI ui) {
		System.out.println("Set ui");
		this.ui = ui;
	}
	
	public UI getUI() {
		return this.ui;
	}

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

}

