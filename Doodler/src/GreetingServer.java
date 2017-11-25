import java.net.*;
import java.util.Scanner;
import java.io.*;

public class GreetingServer extends Thread{
  private ServerSocket serverSocket;

  public GreetingServer(int port) throws IOException{
    serverSocket = new ServerSocket(port);
    serverSocket.setSoTimeout(100000);
  }

  public void run(){
    System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
    while(true){
        try
        {
        /* Start accepting data from the ServerSocket */
        Socket server = serverSocket.accept();
        //System.out.println("Just connected to " + server.getRemoteSocketAddress());
          /* Read data from the ClientSocket */
          DataInputStream in = new DataInputStream(server.getInputStream());
          System.out.println(in.readUTF());
  
          DataOutputStream out = new DataOutputStream(server.getOutputStream());
          
          Scanner sc = new Scanner(System.in);
          System.out.print("Server: ");
          String msg = sc.nextLine();
          
          out.writeUTF("Server: " + msg);
          
          if (msg.equals("bye") || msg.equals("BYE") || msg.equals("Bye")) {
            /* Send data to the ClientSocket */
            out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");
            server.close();
          }
  
        }catch(SocketTimeoutException s){
          System.out.println("Socket timed out!");
          break;
        }catch(IOException e){
          e.printStackTrace();
          break;
        }
    }
  }

  public static void main(String [] args){
    int port = Integer.parseInt(args[0]);
    try {
      Thread t = new GreetingServer(port);
      t.start();
    }catch(IOException e){
      e.printStackTrace();
    }
  }
}
