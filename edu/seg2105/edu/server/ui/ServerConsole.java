package edu.seg2105.edu.server.ui;

import edu.seg2105.edu.server.backend.EchoServer;
import edu.seg2105.client.common.ChatIF;
import edu.seg2105.client.ui.ClientConsole;

import java.io.IOException;
import java.util.Scanner;

public class ServerConsole implements ChatIF {
	//class variables
	private EchoServer echoServer;
	private Scanner fromConsole;
	
	
	//default constructor 
	public ServerConsole(int port) {
		try {
			this.echoServer = new EchoServer(port);
		}
		catch(Error e) {
			System.out.println("A problem was encountered while trying to create ServerConsole!");
			System.exit(0);
		}
		
		fromConsole = new Scanner(System.in);
		
	}
	
    //ChatIF method
	
    @Override
    public void display(String message) {
        System.out.println(message);
    }
	
	public void accept() 
	  {
	  try
	    {
	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	        
	        if (!message.isEmpty()) {
	        	if (message.startsWith("#")){
	        		handleCommand(message);
	        	}
	        	else {
	        		echoServer.sendToAllClients("SERVER MSG> " + message);
	        		display("SERVER MSG> " + message);
	        	}
	        }
	        
	      }
	    } 
	    catch (Exception ex) 
	    {
	      System.out.println
	        ("Unexpected error while reading from console!");
	    }
	  }
	
	
	private void handleCommand(String command){
		command = command.substring(1);
	    String[] split = command.split(" ", 2);
	    String c = split[0];
		  
	    switch (c) {
	      case "quit":
	    	  display("Serving is now quitting...");
	    	  echoServer.sendToAllClients("SERVER MSG> Server is now quitting.");
	    	  echoServer.stopListening();
	    	  System.exit(0);
	    	  
	    	  break;
	      case "stop":
	    	  display("Serving is now not accepting new clients...");
	    	  echoServer.sendToAllClients("SERVER MSG> Server is now not accepting new clients.");
	          echoServer.stopListening();
	          
	          break; 
	      case "close":
	    	  display("Server will now disconnect all clients and stop listening for new ones...");
	    	  echoServer.sendToAllClients("SERVER MSG> Server is now stopping and will be disconnecting all users");
	    	  echoServer.stopListening();
	    	  echoServer.disonnectAllActiveUsers();

	      break;
	          
	      case "setport":
	    	  if (split.length > 1 && !echoServer.isListening()) {
	    		  try {
	    			  int port = Integer.parseInt(split[1]);
	    			  display("Setting port to " + split[1]); 
	    			  echoServer.setPort(port);
	    		  } catch (NumberFormatException e) {
	    			  System.out.println("Invalid port number: " + split[1] + " using default port");
	    			  echoServer.setPort(5555);
	    		  }
    	    	  } else {
    	    		  display("Error occured. Please be disconnected and input a port");
    	    	  }
	    	  break;
	    	       
	      case "start": 	  
	    	  if(!echoServer.isListening()) {
	    		  display("Server is now listening...");
	    		  try {
	    			  echoServer.listen();
	    		  } catch(IOException e) {
	    			  System.out.println("An error occured!");
	    			  System.out.println(e);
	    		  }
	    	  }
	    	  else {
	    		  System.out.println("Please stop the server before starting again.");
	    	  }
	    	  
	    	  break;
	    	  
	      case "getport":
	    	  display("The current port is: " + echoServer.getPort());
	    	  break; 
	      default:
	          System.out.println("Unknown command: " + command);
	      }
		
	}
	  
	public static void main(String[] args) {
		int port = 6969;
		
	    try
	    {
	      port = Integer.parseInt(args[0]); 
	    }
	    catch(ArrayIndexOutOfBoundsException e)
	    {
	      port = EchoServer.DEFAULT_PORT; 
	    }
	    catch (NumberFormatException ne) {
	      port = EchoServer.DEFAULT_PORT; 
	    }
	    
	    
	    ServerConsole server = new ServerConsole(port);
	    
	    try{
	    	server.accept();  //Wait for console data
	    }
	    catch(Exception ex) {
          System.out.println("ERROR - Could not listen for clients!");
          
          System.out.println(ex);
	        
	    }
	  }
  
}
