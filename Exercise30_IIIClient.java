package ClientServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Exercise30_IIIClient extends Application
{
  private TextField tInterest = new TextField();
  private TextField tYears = new TextField();
  private TextField tAmount = new TextField();
  private TextArea ta = new TextArea();
  // Button for sending a student to the server
  private Button btSubmit = new Button("Submit");
  // Host name or ip
  String host = "localhost";
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage)
  { GridPane inputPane = new GridPane();
	inputPane.add(new Label("Annual Interest Rate "), 0, 0);
	inputPane.add(tInterest, 1, 0);    
	inputPane.add(new Label("Number of Years "), 0, 1);
	inputPane.add(tYears, 1, 1);
	inputPane.add(new Label("Loan Amount "), 0, 2);
	inputPane.add(tAmount, 1, 2);
	  
	BorderPane topPane = new BorderPane();
	topPane.setCenter(inputPane);
	topPane.setRight(btSubmit);
	
	BorderPane mainPane = new BorderPane();
	ta.setEditable(false);
	mainPane.setCenter(ta);
	mainPane.setTop(topPane);
    tInterest.setPrefColumnCount(15);
    tYears.setPrefColumnCount(15);
    tAmount.setPrefColumnCount(10);
    btSubmit.setOnAction(new ButtonListener());

    // Create a scene and place it in the stage
    Scene scene = new Scene(mainPane, 450, 200);
    primaryStage.setTitle("Loan Client"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.show(); // Display the stage
    primaryStage.setAlwaysOnTop(true);
  }
  // Handle button action
  private class ButtonListener implements EventHandler<ActionEvent>
  { @Override
    public void handle(ActionEvent e)
    { try
      { // Establish connection with the server
        Socket socket = new Socket(host, 8000);
        // Create an output stream to the server
        ObjectOutputStream toServer =
                new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream fromServer =
                new ObjectInputStream(socket.getInputStream());
       
        // Get text fields
        LoanForClient loan = new LoanForClient(
        		Double.parseDouble(tInterest.getText().trim()),
        		Integer.parseInt(tYears.getText().trim()),
        		Double.parseDouble(tAmount.getText().trim()));

        // Validate input
        if (loan.getAnnualInterestRate() <= 0 ||
        		loan.getNumberOfYears() <= 0 ||
        		loan.getLoanAmount() <= 0)
        	throw  new NumberFormatException();
        
        // Send loan request to the server
        toServer.writeObject(loan);
        // Get loan data from the server
        LoanForClient srvLoan = (LoanForClient)fromServer.readObject();
        
        // Make sure received loan object from server
        if (srvLoan != null)
        {
	        // Display to the text area
	        Platform.runLater( () ->
	        { ta.appendText("Annual interest rate: " + srvLoan.getAnnualInterestRate() + 
	        		" Number of Years: " + srvLoan.getNumberOfYears() +
	        		" Loan Amount: " + srvLoan.getLoanAmount() + "\n");
	        ta.appendText("Monthly Payment : " + srvLoan.getMonthlyPayment() +
	        		" Total Payment : " + srvLoan.getTotalPayment() + "\n");
	        });
        }
/*        // Display to the text area
        Platform.runLater( () ->
        { ta.appendText("Annual interest rate: " + loan.getAnnualInterestRate() + 
        		" Number of Years: " + loan.getNumberOfYears() +
        		" Loan Amount: " + loan.getLoanAmount() + "\n");
        });*/
      }
      catch (IOException ex)
      { ex.printStackTrace();
      }
    catch (NumberFormatException ex)
    {
    	Platform.runLater( () ->
        { ta.appendText("All values must be positive numbers\n");
        });    	
    } catch (ClassNotFoundException ex) {
		// TODO Auto-generated catch block
		ex.printStackTrace();
	}
    }
  }
  public static void main(String[] args)
  { launch(args);
  }
}
