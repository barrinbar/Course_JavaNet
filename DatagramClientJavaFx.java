import java.io.*;
import java.net.*;
import java.util.Arrays;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
public class DatagramClientJavaFx extends Application
{ private DatagramSocket socket;
  // The byte array for sending and receiving datagram packets
  private byte[] buf = new byte[256];
  // Server InetAddress
  private InetAddress address;
  // The packet sent to the server
  private DatagramPacket sendPacket;
  // The packet received from the server
  private DatagramPacket receivePacket;
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage) throws IOException
  { // Panel p to hold the label and text field
    BorderPane paneForTextField = new BorderPane();
    paneForTextField.setPadding(new Insets(5, 5, 5, 5)); 
    paneForTextField.setStyle("-fx-border-color: green");
    paneForTextField.setLeft(new Label("Enter a radius: "));
    TextField tf = new TextField();
    tf.setAlignment(Pos.BOTTOM_RIGHT);
    paneForTextField.setCenter(tf);
    BorderPane mainPane = new BorderPane();
    // Text area to display contents
    TextArea ta = new TextArea();
    ta.setEditable(false);
    mainPane.setCenter(new ScrollPane(ta));
    mainPane.setTop(paneForTextField);
    // Create a scene and place it in the stage
    Scene scene = new Scene(mainPane, 450, 200);
    primaryStage.setTitle("Client"); // Set the stage title
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
    tf.setOnAction(e ->
    { try
      { // Initialize buffer for each iteration
        Arrays.fill(buf, (byte)0);
        // Get the radius from the text field
        double radius = Double.parseDouble(tf.getText().trim());
        if (radius <= 0) throw  new NumberFormatException();
        // send radius to the server in a packet
        sendPacket.setData(tf.getText().trim().getBytes());
        try
        { socket.send(sendPacket);
		  // receive area from the server in a packet
          socket.receive(receivePacket);
        } 
        catch (Exception e1)
        { e1.printStackTrace();
		}
        double area = Double.parseDouble(new String(buf).trim());
        if (area < 0)
        { Platform.runLater( () ->
          { primaryStage.close();
            socket.close();
			Platform.exit();
            System.exit(0);
            });  
 	      }
        else
        { // Display to the text area
          Platform.runLater( () ->
          { ta.appendText("Radius is " + tf.getText().trim() + "\n");
            ta.appendText("Area received from the server is "
              +  Double.parseDouble(new String(buf).trim()) + '\n'); 
          });  
        }    	
     }
     catch (NumberFormatException ex)
     { Platform.runLater( () ->
       { ta.appendText("Radius Must be > 0.0\n");
       });  
     }
    });
    try
    { // get a datagram socket
      socket = new DatagramSocket();
      address = InetAddress.getByName("localhost");
      sendPacket =  new DatagramPacket(buf, buf.length, address, 8000);
      receivePacket = new DatagramPacket(buf, buf.length);
    }
    catch(SocketException e1)
    { socket.close();
    }
  }
  public static void main(String[] args)
  { launch(args);
  }
}
