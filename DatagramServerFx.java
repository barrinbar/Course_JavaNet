import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
public class DatagramServerFx extends Application
{ private TextArea ta;
  private DatagramSocket socket; 
  private byte[] buf = new byte[256];
  private static int currClient = -1;
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage)
  { ta = new TextArea();
    Scene scene = new Scene(new ScrollPane(ta), 450, 200);
    primaryStage.setTitle("Server"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage
    primaryStage.setAlwaysOnTop(true);
    primaryStage.setOnCloseRequest(
      new EventHandler<WindowEvent>()
      { public void handle(WindowEvent event)
        { socket.close();
    	  Platform.exit();
          System.exit(0);
        } 
      });
    new Thread( () ->
    { try
      { // Create a server socket
    	socket = new DatagramSocket(8000);
        Platform.runLater(() ->
          ta.appendText("Server started at " + new Date() + '\n'));
        // Create a packet for receiving data
        DatagramPacket receivePacket =
          new DatagramPacket(buf, buf.length);
        
        while (true)
        { Arrays.fill(buf, (byte)0);
          // Receive radius from the client in a packet
          System.out.println("Before Receive");
          System.out.flush();
          socket.receive(receivePacket);
          
          System.out.println("After Receive");
          System.out.flush();
          System.out.println("Curr client: " + currClient);
          System.out.flush();
          if (receivePacket.getPort() != currClient) {
        	  System.out.println("New thread");
              System.out.flush();
                new Thread(new HandleAClient(receivePacket.getAddress(),
                		receivePacket.getPort(),
                		Double.parseDouble(new String(buf).trim()))).start();
                currClient = receivePacket.getPort();
          }
        }
      }
      catch(IOException ex)
      { return;
      }
    }).start();
  }
  
  // Define the thread class for handling new connection
  class HandleAClient implements Runnable
  { 
	  private InetAddress clientAddress;
	  private int clientPort;
	  private double radius;
	  
	  /** Construct a thread */
    public HandleAClient(InetAddress clientAddress,
    		int clientPort,
    		double radius)
    { 
    	this.clientAddress = clientAddress;
    	this.clientPort = clientPort;
    	this.radius = radius;
    	System.out.println("Created new thread");
        System.out.flush();
    }
    /** Run a thread */
    public void run()
    {
    	System.out.println("Started new thread");
        System.out.flush();
    	
        Platform.runLater(() ->
        { ta.appendText("The client host name is " +
            clientAddress.getHostName() +
              " and port number is " + clientPort + '\n');
          ta.appendText("Radius received from client is " +
            radius + '\n');
        }); 
        // Compute area
        double area = radius * radius * Math.PI;
        //double area = -1;
        Platform.runLater(() ->
        { ta.appendText("Area is " + area + '\n');
        });

     // Create a packet for sending data
        DatagramPacket sendPacket =
          new DatagramPacket(buf, buf.length);
        
        // Send area to the client in a packet
        sendPacket.setAddress(clientAddress);
        sendPacket.setPort(clientPort);
        sendPacket.setData(new Double(area).toString().getBytes());
        try {
			socket.send(sendPacket);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        currClient = -1;
    }
  }  
  
  public static void main(String[] args)
  { launch(args);
  }
}
