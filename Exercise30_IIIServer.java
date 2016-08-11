package ClientServer;
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
public class Exercise30_IIIServer extends Application 
{
	// Text area for displaying contents
	  private TextArea ta = new TextArea();
	  private ServerSocket serverSocket;
	  private Socket socket;
	  // Number a client
	  private int clientNo = 0;
	  @Override // Override the start method in the Application class
	  public void start(Stage primaryStage)
	  { // Create a scene and place it in the stage
	    ta.setEditable(false);
		Scene scene = new Scene(new ScrollPane(ta), 450, 200);
	    primaryStage.setTitle("Loan Server"); // Set the stage title
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
	        { ta.appendText("Loan Server started at " 
	          + new Date() + '\n');
	        });
	        while (true)
	        { // Listen for a new connection request
	          socket = serverSocket.accept();
	          // Increment clientNo
	          clientNo++;
	          Platform.runLater( () ->
	          { // Display the client number
	            ta.appendText("Starting thread for client " + clientNo +
	              " at " + new Date() + '\n');
	            // Find the client's host name, and IP address
	            InetAddress inetAddress = socket.getInetAddress();
	            ta.appendText("Client " + clientNo + "'s host name is "
	              + inetAddress.getHostName() + "\n");
	            ta.appendText("Client " + clientNo + "'s IP Address is "
	              + inetAddress.getHostAddress() + "\n");
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
	  private ObjectOutputStream outputToClient;
	  private ObjectInputStream inputFromClient;
	    /** Construct a thread */
	    public HandleAClient(Socket socket)
	    { this.socket = socket;
	    }
	    /** Run a thread */
	    public void run()
	    { try
	      { // Create an input and output streams
	        outputToClient =
	                new ObjectOutputStream(socket.getOutputStream());
	        inputFromClient =
	                new ObjectInputStream(socket.getInputStream());

            // Continuously serve the client
	        while (true)
	        { 
		        // Read from input
	            LoanForClient clientLoan = 
	              		(LoanForClient) inputFromClient.readObject();
	            
	            Loan serverLoan = new Loan(clientLoan.getAnnualInterestRate(),
	            		clientLoan.getNumberOfYears(),clientLoan.getLoanAmount());
	            
	            // Calculate the Monthly and Total Payments 
	            clientLoan.setMonthlyPayment(serverLoan.getMonthlyPayment());
	            clientLoan.setTotalPayment(serverLoan.getTotalPayment());
	            
	          // Send area back to the client
	          outputToClient.writeObject(clientLoan);
	          Platform.runLater(() ->
		        { ta.appendText("Annual interest rate: " + clientLoan.getAnnualInterestRate() + 
		        		" Number of Years: " + clientLoan.getNumberOfYears() +
		        		" Loan Amount: " + clientLoan.getLoanAmount() + "\n");
		        ta.appendText("Monthly Payment : " + clientLoan.getMonthlyPayment() +
		        		" Total Payment : " + clientLoan.getTotalPayment() + "\n");
		        }); 
	        }
	      }
	      catch(SocketException ex)
	      { try
	        { serverSocket.close();
	    	  //socket.close();
		    } 
	        catch (IOException e)
	        { 
		    }
	      }
	      catch(IOException ex)
	      { 
	      } catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    }
	  }
	  public static void main(String[] args)
	  { launch(args);
	  }
}
