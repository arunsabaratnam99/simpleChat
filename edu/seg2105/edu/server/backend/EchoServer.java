package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
	boolean run = true; 
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
	  
	    // Make message into string
	    String message = msg.toString(); // Convert the message to a string
	    
	    if (client.getInfo("FirstMessageSent") == null) {
	  		
	  		if (!message.startsWith("#login")) {
	  			System.out.println("The first message must be #login <loginID>, server is now closing");
	  			try {
					client.close();
				} catch (IOException e) {
					System.out.println("Error has occured");
				}
	  		}
	  		else {
	  			client.setInfo("FirstMessageSent", true);
	  		}
	  	}

	    // If message starts with #login then call the loginClientMessage method
	    if (message.startsWith("#login")) {
	  	      loginClientMessage(message, client);
	   
	    } else {
	      // Other processing for regular messages
	      System.out.println("Message received: " + message + " from " + client.getInfo("loginID"));

	      // Sending to all the clients on the same server
	      this.sendToAllClients(client.getInfo("loginID") + " -> " + message);
	    }
	   
	  }
  
  
  private void loginClientMessage(String command, ConnectionToClient client) {

	    // Splits (#login and clientLoginId) to different strings to set client info
	    String[] parts = command.split(" ");


	    // Assigns 2nd split of the string to be the clientLoginId
	    String clientLoginId = parts[1];

	    // Set the client's login ID in their connection information
	    System.out.println("Message received: " + command + " from " + client.getInfo("loginID"));
	    client.setInfo("loginID", clientLoginId);
	    System.out.println(clientLoginId + " has logged on.");
	    

	  }
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  /**
   * Hook method called each time a new client connection is
   * accepted. The default implementation does nothing.
   * @param client the connection connected to the client.
   */
  
  @Override
  protected void clientConnected(ConnectionToClient client) {
	 String message = "A new client has connected.";

	 this.sendToAllClients(message);
	 System.out.println(message);
  }

  /**
   * Hook method called each time a client disconnects.
   * The default implementation does nothing. The method
   * may be overridden by subclasses but should remains synchronized.
   *
   * @param client the connection with the client.
   */
  
  @Override
  synchronized protected void clientDisconnected(ConnectionToClient client) {
	 String message = client.getInfo("loginID") + " has disconnected.";

    this.sendToAllClients(message);
    System.out.println(message);
	    
  }
  
  public void quitGracefully() {
  	stopListening();
  
    Thread[] clientThreadList = getClientConnections();

    for (int i=0; i<clientThreadList.length; i++)
    {
      try
      {
        ((ConnectionToClient)clientThreadList[i]).close();
      }
      catch (Exception ex) {}
    }
  } 
}
  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
//End of EchoServer class
