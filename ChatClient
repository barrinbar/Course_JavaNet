import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import chapter30.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ChatClient extends Application {

	private TextField tfName = new TextField();
	private TextField tfMsg = new TextField();
	private TextArea ta = new TextArea();
	
	private String name;
	
	  // Host name or ip
	  String host = "localhost";
	  @Override // Override the start method in the Application class
	  public void start(Stage primaryStage)
	  { GridPane inputPane = new GridPane();
		inputPane.add(new Label("Name: "), 0, 0);
		inputPane.add(tfName, 1, 0);    
		inputPane.add(new Label("Message: "), 0, 1);
		inputPane.add(tfMsg, 1, 1);
		  
		
		BorderPane mainPane = new BorderPane();
		ta.setEditable(false);
		mainPane.setCenter(ta);
		mainPane.setTop(inputPane);
	    tfName.setPrefColumnCount(25);
	    tfMsg.setPrefColumnCount(25);
	    tfMsg.setDisable(true);
	    Platform.runLater(() ->
	    	ta.appendText("Welcome, please enter your name to start chatting\n"));

	    // Create a scene and place it in the stage
	    Scene scene = new Scene(mainPane, 450, 200);
	    primaryStage.setTitle("Chat Client"); // Set the stage title
	    primaryStage.setScene(scene); // Place the scene in the stage
	    primaryStage.show(); // Display the stage
	    primaryStage.setAlwaysOnTop(true);
	    
	    tfName.setOnAction(e -> {
	    	// If name is not empty - keep it
	    	if (tfName.getText().trim() != "") {
	    		tfName.setDisable(true);
	    		
	    		name = tfName.getText().trim();
	    		
	    		// TODO: receive connected client list
	    		
	    		// Establish connection with the server
		        Socket socket;
				try {
					socket = new Socket(host, 8000);
				
		        // Create an output stream to the server
		        DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
		        
		        toServer.writeChars(name);
		        tfMsg.setDisable(false);
		        Platform.runLater(() ->
		        	ta.appendText("Hello " + name + "\nYou are now connected to the chat."));
		        
		    	new Thread(new ChatListener(socket)).start();
				
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    	}
	    });
	    
	    tfMsg.setOnAction(new MessageListener());
	  }
	  // Handle message action
	  private class MessageListener implements EventHandler<ActionEvent>
	  { @Override
	    public void handle(ActionEvent e)
	    { try
	      { // Establish connection with the server
	        @SuppressWarnings("resource")
			Socket socket = new Socket(host, 8000);
	        // Create an output stream to the server
	        ObjectOutputStream toServer =
	                new ObjectOutputStream(socket.getOutputStream());

	        // Get text fields
	        Message message = new Message(
	        		name, tfMsg.getText().trim(), null);

	        // Validate input
	        if (message.getMsg() == "")
	        	// ignore
	        	;
	        else {
	        // Send loan request to the server
	        toServer.writeObject(message);
	        Platform.runLater( () ->
	        	tfMsg.setText(""));
	        }
	      }
	      catch (IOException ex)
	      { ex.printStackTrace();
	      }
	    }
	  }
	  
	  private class ChatListener implements Runnable {

		  private ObjectInputStream fromServer;
		  private Message message;
		  
		  ChatListener(Socket socket) throws IOException {
			  this.fromServer =
				        new ObjectInputStream(socket.getInputStream());
		  }
		@Override
		public void run() {
	        // Create an output stream to the server
			while (true) {
				try {
					message = (Message)fromServer.readObject();
					Platform.runLater( () ->
			        { ta.appendText(message.toString() + '\n');
			        }); 
					
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
		}
	  }
	  
	  public static void main(String[] args)
	  { launch(args);
	  }
}
