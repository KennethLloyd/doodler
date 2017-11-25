import java.net.*;
import java.io.*;
import java.util.Scanner;

public class GreetingClient{
  public static void main(String [] args){
    int counter = 0;
    while (true) {
      try {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);
        Socket client;
        
        if (counter == 0) {
          /* Open a ClientSocket and connect to ServerSocket */
          System.out.println("Connecting to " + serverName + " on port " + port);
          counter++;
        }
        client = new Socket(serverName, port);

        /* Send data to the ServerSocket */
          //System.out.println("Just connected to " + client.getRemoteSocketAddress());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());
          
        Scanner sc = new Scanner(System.in);
        System.out.print("Client: ");
        String msg = sc.nextLine();
          
        out.writeUTF("Client: " + msg);
        
        /* Receive data from the ServerSocket */
        InputStream inFromServer = client.getInputStream();
        DataInputStream in = new DataInputStream(inFromServer);
        System.out.println(in.readUTF());
          
        client.close();
      }
      catch(IOException e){
          e.printStackTrace();
      }
    }
  }
}
