package chat;

import java.io.*;
import java.net.*;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
public class ChatServer extends Application
{ // Text area for displaying contents
  private TextArea ta = new TextArea();
  private ServerSocket serverSocket;
  private Socket socket;
  private ClientsList clients;
  
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage)
  { // Create a scene and place it in the stage
    ta.setEditable(false);
	Scene scene = new Scene(new ScrollPane(ta), 450, 200);
    primaryStage.setTitle("Chat Server"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage
    primaryStage.setAlwaysOnTop(true);
    primaryStage.setOnCloseRequest(
       new EventHandler<WindowEvent>()
       { public void handle(WindowEvent event)
         { Platform.exit();
      	   System.exit(0);
		 } 
       });
    new Thread( () ->
    { try
      { // Create a server socket
        serverSocket = new ServerSocket(8000);
        Platform.runLater( () ->
        { ta.appendText("Server started at " 
          + new Date() + '\n');
        });
        
        // Init clients list
        clients = new ClientsList();
        
        while (true)
        { // Listen for a new connection request
          socket = serverSocket.accept();
          
          
          Platform.runLater( () ->
          { // Display the client number
            ta.appendText(socket.toString() + '\n');
          });
          // Create and start a new thread for the connection
          new Thread(new HandleAClient(socket)).start();
        }
      }
      catch(IOException ex)
      { 
      }
    }).start();
  }
  // Define the thread class for handling new connection
  class HandleAClient implements Runnable
  { private Socket socket; // A connected socket
  private String clientName = "";
  private ClientKey clientKey = null;
  
    /** Construct a thread */
    public HandleAClient(Socket socket)
    { this.socket = socket;
    }
    /** Run a thread */
    @SuppressWarnings("resource")
	public void run()
    { try
      { // Create data input and output streams
    	BufferedReader dataFromClient
        = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
    	ObjectInputStream fromClient = new ObjectInputStream(
          socket.getInputStream());
        ObjectOutputStream toClient = new ObjectOutputStream(
          socket.getOutputStream());
        
        // Receive client name
        clientName = dataFromClient.readLine();
        clientKey = new ClientKey(socket.getPort(), socket.getInetAddress().getHostName());
    	// Make sure client doesn't already exist
        if (!clients.clientExists(clientKey)) {
	        if (clients.nameExists(clientName)) { // If the name already exists
	        	// Inform the client
	        	toClient.writeObject(null);
	        }
	        else {
	        	// Add the new client and send the updated list to the server
	        	clients.addClient(clientKey, clientName);
	        	toClient.writeObject(clients.sortByValue());
	        }
	        
	        // Continuously serve the client
	        while (true)
	        { // Receive message from the client
	          Message message = (Message)fromClient.readObject();
	          
	          // Check if user wants to disconnect
	          if (message == null) {
	        	  Platform.runLater(() ->
		            { ta.appendText(clientName + " has left the chat.\n");
		            });
	          }
	          else {
	        	// Send message to all clients
				for (ClientKey client : clients.getClients().keySet()) {
					Socket msgSocket = new Socket(client.getHostName(), client.getPort());
					ObjectOutputStream outputToClient = new ObjectOutputStream(
							msgSocket.getOutputStream());
					outputToClient.writeObject(message);

					// TODO: send just to destination clients
				}
	          Platform.runLater(() ->
	            { ta.appendText("Message from " +
	                clientName + ": " + message + '\n');
	            });
	          }
	        }
	      }
      }
      catch(SocketException ex)
      { try
        { serverSocket.close();
    	  socket.close();
	    } 
        catch (IOException e)
        { 
	    }
      }
      catch(IOException ex)
      { 
      }catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
    }
  }
  public static void main(String[] args)
  { launch(args);
  }
  
}
