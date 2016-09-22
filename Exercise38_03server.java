import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;

public class Exercise38_03server extends Application
{   // Text area for displaying contents
	  private TextArea ta = new TextArea();
	  private ServerSocket serverSocket;
	  private Socket socket;
	  // Number a client
	  private int clientNo = 0;
	  private Connection connection;
	  private java.sql.PreparedStatement stmt;
	  private TableView<Triplet> tableView = new TableView<>();
	  
	  @Override // Override the start method in the Application class
	  public void start(Stage primaryStage)
	  {
	
		   // Create a scene and place it in the stage
		    ta.setEditable(false);
			Scene scene = new Scene(new ScrollPane(ta), 450, 200);
		    primaryStage.setTitle("Courses Server"); // Set the stage title
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
		        { ta.appendText("Courses Server started at " 
		          + new Date() + '\n');
		        });
		        initializeDB();
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
	  private void initializeDB()
	  { try
	    { // Load the JDBC driver
	      Class.forName("com.mysql.jdbc.Driver");
	      System.out.println("Driver loaded");
	      // Establish a connection
	      connection = DriverManager.getConnection
	        ("jdbc:mysql://localhost/javabook", "scott", "tiger");
	      System.out.println("Database connected");
	      	      
	    }
	  catch (Exception e) {
			  e.printStackTrace();
		  }
	  }
		  // Define the thread class for handling new connection
		  class HandleAClient implements Runnable
		  { private Socket socket; // A connected socket
		  private ObjectInputStream inputFromClient;
		  private ObjectOutputStream outputToClient;
		    /** Construct a thread */
		    public HandleAClient(Socket socket)
		    { this.socket = socket;
		    }
		    /** Run a thread */
		    public void run()
		    { try
		      { // Create an input and output streams
		        inputFromClient =
		                new ObjectInputStream(socket.getInputStream());
		        outputToClient =
		                new ObjectOutputStream(socket.getOutputStream());

	            // Continuously serve the client
		        while (true)
		        { 
			        // Read from input
		        	int studentId = 
		              		inputFromClient.readInt();
		        	int staffId = 
		              		inputFromClient.readInt();
		            
		        	stmt = connection.prepareStatement("select enrollment.ssn as 'STUDENT',"
		            		+ " taughtby.ssn as 'STAFF',"
		            		+ " enrollment.courseId as 'COURSE'"
		          		+ " from taughtby, enrollment"
		          		+ " where (enrollment.ssn = ? or (? = 0))"
		          		+ " and (taughtby.ssn = ? or (? = 0))"
		          		+ " and (enrollment.courseId = taughtby.courseId)");
		            stmt.setInt(1, studentId);
		            stmt.setInt(2, studentId);
		            stmt.setInt(3, staffId);
		            stmt.setInt(4, staffId);
		            
		            // Calculate the Monthly and Total Payments 
		            
		            ResultSet resultSet = stmt.executeQuery();
		            //populateTableView(resultSet, tableView);
		          // Send back to the client
		          outputToClient.writeObject(resultSet);
		          Platform.runLater(() ->
			        { ta.appendText("Sent to client data for student id: " + studentId + 
			        		" and staffId: " + staffId + "\n");
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
		      } catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    }
		    
		    @SuppressWarnings({ "unchecked", "rawtypes" })
		    private void populateTableView(ResultSet rs, TableView tableView)
		    { 
		  	  tableView.getItems().clear();
		  	  tableView.getColumns().clear();
		  	  tableView.getColumns().clear();
		  	ObservableList<ObservableList> data = 
		        FXCollections.observableArrayList();
		      try
		      {
		      	// Add column names
		          for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
		              //We are using non property style for making dynamic table
		              final int j = i; 
		              TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
		              col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){
		                  public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
		                  	if (param.getValue().get(j) != null)
		                  		return new SimpleStringProperty(param.getValue().get(j).toString());
		                  	else
		                  		return new SimpleStringProperty("");
		                  }
		              });
		              tableView.getColumns().addAll(col);
		          }
		          
		          // Add data to ObservableList 
		          while(rs.next()){
		              //Iterate Row
		              ObservableList<String> row = FXCollections.observableArrayList();
		              for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
		                  //Iterate Column
		                  row.add(rs.getString(i));
		              }
		              data.add(row);
		          }
		          // Add to TableView
		          tableView.setItems(data);
		      } 
		      catch (Exception e)
		      { e.printStackTrace();
		        System.out.println("Error on Building Data");
		      }
		    }
		  }
  public static void main(String[] args)
  { launch(args);
  }
}
