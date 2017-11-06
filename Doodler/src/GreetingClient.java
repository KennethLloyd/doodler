import java.net.*;
import java.io.*;

public class GreetingClient{
  public static void main(String [] args){
    String serverName = args[0];
    int port = Integer.parseInt(args[1]);

    try
    {
      /* Open a ClientSocket and connect to ServerSocket */
      System.out.println("Connecting to " + serverName + " on port " + port);
      Socket client = new Socket(serverName, port);

      /* Send data to the ServerSocket */
      System.out.println("Just connected to " + client.getRemoteSocketAddress());
      OutputStream outToServer = client.getOutputStream();
      DataOutputStream out = new DataOutputStream(outToServer);
      out.writeUTF("Hello from " + client.getLocalSocketAddress());

      /* Receive data from the ServerSocket */
      InputStream inFromServer = client.getInputStream();
      DataInputStream in = new DataInputStream(inFromServer);
      System.out.println("Server says " + in.readUTF());
      client.close();

    }catch(IOException e){
      e.printStackTrace();
    }
  }
}
