// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    openConnection();
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    
    {
      if (message.startsWith("#")) {
    	  handleCommand(message);
      }
      else {
    	  sendToServer(message);
      }
    }
      
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  
  private void handleCommand(String command) {
	  command = command.substring(1);
      String[] split = command.split(" ", 2);
      String c = split[0];
	  
      switch (c) {
      case "quit":
    	  clientUI.display("Quitting program...");
          quit();
          break;
          
      case "logoff":
    	  if (this.isConnected()) {
    		  try {
    			  this.closeConnection();
    			  System.out.println("You are now logged off!");
    		  }
    		  catch(IOException e) {
    			  System.out.println("An error occured!");
    		  }
    	  }
          break;
          
      case "sethost":
          if (split.length > 1 && !this.isConnected()) {
        	  clientUI.display("Setting host to " + split[1]); 
              setHost(split[1]);
          } else {
              clientUI.display("Error occured. Please be disconnected and input a host");
          }
          break;
          
      case "setport":
    	if (split.length > 1 && !this.isConnected()) {
        try {
            int port = Integer.parseInt(split[1]);
      	  	clientUI.display("Setting port to " + split[1]); 
            setPort(port);
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number: " + split[1] + " using default port");
            setPort(5555);
        }
    	} else {
    		clientUI.display("Error occured. Please be disconnected and input a port");
    	}
          break;
          
      case "login":
    	  if (!this.isConnected()) {
    		  try {
    			  this.openConnection();
    			  System.out.println("You are now logged in!");
    		  } 
    		  catch (IOException e) {
    			  System.out.println("An error occured!");
    		  }
    	  }
    	  else {
    		  clientUI.display("You cannot log in as you are already logged in");
    	  }
    	  
          break;
          
      case "gethost":
          clientUI.display("The current host is: " + getHost());
          break;
          
      case "getport":
          clientUI.display("The current port is: " + getPort());
          break;
          
      default:
          System.out.println("Unknown command: " + command);
      }
	  
  }
  
  
	/**
	 * Hook method called each time an exception is thrown by the client's
	 * thread that is waiting for messages from the server. The method may be
	 * overridden by subclasses.
	 * 
	 * @param exception
	 *            the exception raised.
	 */
  
  @Override
  protected void connectionException(Exception exception) {
	  clientUI.display("The server has shut down "); 
	  quit();
  }
  
  /**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or
	 * attempting to reconnect.
	 */
  
  @Override
  protected void connectionClosed() {
	  clientUI.display("Connection closed");
	}
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
