package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Set;

import chat.Message;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ChatClient extends Application {

	private TextField tfName = new TextField();
	private TextField tfMsg = new TextField();
	private TextArea ta = new TextArea();

	private String name;
	private Collection<String> clientCollection = null;

	// Host name or ip
	String host = "localhost";

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		GridPane inputPane = new GridPane();
		inputPane.add(new Label("Name: "), 0, 0);
		inputPane.add(tfName, 1, 0);
		inputPane.add(new Label("Message: "), 0, 1);
		inputPane.add(tfMsg, 1, 1);
		tfName.setPrefColumnCount(20);
		tfMsg.setPrefColumnCount(20);
		tfMsg.setDisable(true);

		BorderPane textPane = new BorderPane();
		ta.setEditable(false);
		textPane.setCenter(ta);
		textPane.setTop(inputPane);
		
		HBox hBox = new HBox();
	    hBox.getChildren().addAll(textPane);
	    hBox.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");
	    /*Pane mainPane = new Pane();
	    mainPane.getChildren().add(hBox);*/

		// Create a scene and place it in the stage
		Scene scene = new Scene(hBox/*, 450, 200*/);
		primaryStage.setTitle("Chat Client"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
		primaryStage.setAlwaysOnTop(false);
		primaryStage.setOnCloseRequest(
			      new EventHandler<WindowEvent>()
			    	{ public void handle(WindowEvent event)
			    	  { Platform.exit();
			    		System.exit(0);
			    	  }
			    	});

		Platform.runLater(() -> ta.appendText("Welcome, please enter your name to start chatting\n"));

		tfName.setOnAction(e -> {
			// If name is not empty - keep it
			if (tfName.getText().trim() != "") {

				name = tfName.getText().trim();

				// Establish connection with the server
				//Socket socket;
				try {
					Socket socket = new Socket(host, 8000);

					// Create input and output stream to the server
					ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());

					toServer.writeObject(name);

					Platform.runLater(() -> {
						ta.appendText("Sent my name to the server\n");
					});
					String nameOK = (String) fromServer.readObject();

					if (!nameOK.equals("Welcome")) {
						Platform.runLater(() -> {
							ta.appendText("The name " + name + " is already taken, please choose a different name.\n");
							tfName.setText("");
							tfName.requestFocus();
						});
					} else {
						Platform.runLater(() -> {
							ta.appendText("Hello " + name + ", welcome to the chat.\n");
							tfName.setDisable(true);
							tfMsg.setDisable(false);
						});

						tfMsg.setOnAction(new MessageListener(socket));
						new Thread(new ChatListener(socket)).start();
					}
				}catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	// Handle message action
	private class MessageListener implements EventHandler<ActionEvent> {
		
		private Socket socket;
		private ObjectOutputStream toServer;
		public MessageListener(Socket socket) throws IOException {
			this.socket = socket;
			// Create an output stream to the server
			this.toServer = new ObjectOutputStream(socket.getOutputStream());
		}
		@Override
		public void handle(ActionEvent e) {
			try { // Establish connection with the server
				// Validate input
				if (tfMsg.getText().trim() == "")
					// ignore
					;
				else {

					// Get fields
					Message message = new Message(tfMsg.getText().trim(), tfName.getText().trim());

					// Send loan request to the server
					toServer.writeObject(message);
					Platform.runLater(() -> {
						ta.appendText("Sent new message to server: " + message.getMsg() + "\n");
						tfMsg.setText("");
					});
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private class ChatListener implements Runnable {

		private ObjectInputStream fromServer;
		private Message message;
		private Socket socket;

		ChatListener(Socket socket) throws IOException {
			this.socket = socket;
			this.fromServer = new ObjectInputStream(socket.getInputStream());
		}

		@Override
		public void run() {
			try {
				// Create an output stream to the server
				//ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());
			while (true) {
					// Check if the server has sent a new message or an updated
					// clients list
					Object obj = fromServer.readObject();

					if (obj instanceof Message) {
						message = (Message) obj;
						Platform.runLater(() -> {
							ta.appendText(message.toString() + '\n');
						});
					} else
						throw new ClassNotFoundException(
							"Class " + obj.getClass() + " not expected. Should be either Message or Hashmap");
				
			}
				} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				Platform.runLater(() ->
				ta.appendText("Connection to server lost.\n"));
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
