package chat;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ChatServer extends Application { // Text area for displaying
												// contents
	private TextArea ta = new TextArea();
	private ServerSocket serverSocket;
	private Socket socket;
	private ClientsList clients = new ClientsList();
	private Map<String, ObjectOutputStream> clientOutStreams;

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) { // Create a scene and place it in
											// the stage
		ta.setEditable(false);
		Scene scene = new Scene(new ScrollPane(ta), 450, 200);
		primaryStage.setTitle("Chat Server"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
		primaryStage.setAlwaysOnTop(false);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});
		new Thread(() -> {
			try { // Create a server socket
				serverSocket = new ServerSocket(8000);
				clientOutStreams = new HashMap<>();
				Platform.runLater(() -> {
					ta.appendText("Server started at " + new Date() + '\n');
				});

				while (true) { // Listen for a new connection request
					socket = serverSocket.accept();

					Platform.runLater(() -> { // Display the client info
						ta.appendText(socket.toString() + '\n');
					});
					
					ObjectOutputStream toClient = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());

					// Receive client name
					String clientName;
					try {
						clientName = (String) fromClient.readObject();
						
						ClientKey clientKey = new ClientKey(socket.getPort(), socket.getInetAddress().getHostName());
	
						if (clientOutStreams.containsKey(clientName)) {
							toClient.writeObject("Name already taken");
						}
						else {
							// TODO: Collections.sort(clientOutStreams);
						//if (!clients.clientExists(clientKey)) {
							/*if (clients.nameExists(clientName)) {
								// Inform the client by sending an empty clients list
								toClient.writeObject(new ClientsList());
							} else {
								// Add the new client and send the updated list to the
								// server
								clients.addClient(clientKey, clientName);
								clients.sortByValue();*/
								clientOutStreams.put(clientName, toClient);
/*								for (ClientKey client : clients.getClients().keySet()) {
									clientOutStreams.get(client.getPort()).writeObject(clients);
									Platform.runLater(() -> {
										ta.appendText("Sending to " + client.getPort() + " ");
									});
								}	
*/								Platform.runLater(() -> ta.appendText("\nNew client connected: " + clientName + "\n"));
								// Create and start a new thread for the connection
								new Thread(new HandleAClient(socket, clientName)).start();
							//}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (IOException ex) {
			}
		}).start();
	}

	// Define the thread class for handling new connection
	class HandleAClient implements Runnable {
		private Socket socket; // A connected socket
		private String clientName = "";

		/** Construct a thread */
		public HandleAClient(Socket socket, String clientName) {
			this.socket = socket;
			this.clientName = clientName;
		}

		/** Run a thread */
		@SuppressWarnings("resource")
		public void run() {
			try {
				Platform.runLater(() -> {
					ta.appendText("In thread of " + clientName +"\n");
				});
				SerClients ca = new SerClients(clientOutStreams.keySet());
				// Send new clients list to all clients
				for (ObjectOutputStream client : clientOutStreams.values()) {
					client.writeObject(ca);
				}	
				// Continuously serve the client
				while (true) {
					ObjectInputStream fromClient = new ObjectInputStream(socket.getInputStream());

					// Receive message from the client
					Message message = (Message) fromClient.readObject();

					// Check if user wants to disconnect
					if (message.toString() == "") {
						Platform.runLater(() -> {
							ta.appendText(clientName + " has left the chat.\n");
						});
						break;
					} else {
						
						Platform.runLater(() -> {
							ta.appendText("Sending message from " + clientName + ": "+ message.getMsg() +"\n");
						});
						// Send message to all clients
						for (ObjectOutputStream client : clientOutStreams.values()) {
							client.writeObject(message);
						}
						
						Platform.runLater(() -> {
							ta.appendText("\nMessage from " + clientName + ": " + message + " was sent \n");
						});
					}
				}
			} catch (SocketException ex) {
				ex.printStackTrace();
				try {
					serverSocket.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
