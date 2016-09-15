import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.*;
import java.util.Random;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
public class Exercise38_01JavaFx extends Application
{ private DBConnectionPane dBConnectionPane 
    = new DBConnectionPane();
  private Label lblConnectionStatus 
    = new Label("No connection now");
  private TextArea taDisplay = new TextArea();
  private Statement statement;
  private String strCommands;
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage)
  { Button btBatchUpdate = new Button("Batch Update");
    Button btNonBatchUpdate = new Button("Non-Batch Update");
    HBox hBoxButtons = new HBox(10);
    hBoxButtons.getChildren().addAll(btBatchUpdate, 
      btNonBatchUpdate);
    hBoxButtons.setAlignment(Pos.CENTER);
    BorderPane borderPaneConnect = new BorderPane(); 
    Button btConnectDB = new Button("Connect to Database");
    borderPaneConnect.setCenter(lblConnectionStatus);
    borderPaneConnect.setRight(btConnectDB);
    BorderPane pane = new BorderPane();
    pane.setTop(borderPaneConnect);
    pane.setBottom(hBoxButtons);    
    pane.setCenter(taDisplay);
    // Create a scene and place it in the stage
    Scene scene = new Scene(pane, 320, 350);
    primaryStage.setTitle("Exercise38_01JavaFx"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.setAlwaysOnTop(true);
    primaryStage.show(); // Display the stage   
    btConnectDB.setOnAction(e -> openDialog());
    btBatchUpdate.setOnAction(e ->
    { if (dBConnectionPane.getConnection() == null)
      { taDisplay.setText("Please connect to the database first");
        return;
      }
      new Thread(() ->  batchUpdate()).start();
    });
    btNonBatchUpdate.setOnAction(e ->
    { if (dBConnectionPane.getConnection() == null)
      { taDisplay.setText("Please connect to the database first");
        return;
      }
      new Thread(() ->  nonBatchUpdate()).start();
    });
  }
  private void openDialog()
  { BorderPane pane = new BorderPane();
    pane.setCenter(dBConnectionPane);
    Button btCloseDialog = new Button("Close Dialog");
    pane.setBottom(btCloseDialog);
    BorderPane.setAlignment(btCloseDialog, Pos.CENTER);
    Stage stage = new Stage();
    stage.setScene(new Scene(pane, 420, 180)); // Place the scene in the stage
    stage.setTitle("Connect to DB");
    stage.show(); // Display the stage   
    btCloseDialog.setOnAction(e ->
    { stage.hide();
      if (dBConnectionPane.getConnection() != null)
        lblConnectionStatus.setText("DB connected");
    });
  }
  private void nonBatchUpdate()
  { try
    { 
      long startTime = System.currentTimeMillis();
      executeSQL();
      long endTime = System.currentTimeMillis();
      Platform.runLater(() ->
      { taDisplay.appendText("Non-batch update completed\n");
        taDisplay.appendText("The elapsed time is " +
         (endTime - startTime) + "\n");
        lblConnectionStatus.setText("Non-batch update succeeded");
      });
    }
    catch (Exception ex)
    { ex.printStackTrace();
    }
  }
  
  /** Execute SQL commands */
  private void executeSQL()
  { if (dBConnectionPane.getConnection() == null)
    { taDisplay.setText("Please connect to a database first");
      return;
    }
    else
    { String sqlCommands = strCommands.trim();
      String[] commands = sqlCommands.replace('\n', ' ').split(";");
      for (String aCommand: commands)
      { if (aCommand.trim().toUpperCase().startsWith("SELECT"))
        { processSQLSelect(aCommand);
        }
        else
        { processSQLNonSelect(aCommand);
        }
      }
    }
  }
  /** Execute SQL SELECT commands */
  private void processSQLSelect(String sqlCommand)
  { try
    { // Get a new statement for the current connection
      statement = dBConnectionPane.getConnection().createStatement();
      // Execute a SELECT SQL command
      ResultSet resultSet = statement.executeQuery(sqlCommand);
      // Find the number of columns in the result set
      int columnCount = resultSet.getMetaData().getColumnCount();
      String row = "";
      // Display column names
      for (int i = 1; i <= columnCount; i++)
      { row += resultSet.getMetaData().getColumnName(i) + "\t";
      }
      taDisplay.appendText(row + '\n');
      while (resultSet.next())
      { // Reset row to empty
        row = "";
        for (int i = 1; i <= columnCount; i++)
        { // A non-String column is converted to a string
          row += resultSet.getString(i) + "\t"; 
        }
        taDisplay.appendText(row + '\n');
      }
    }
    catch (SQLException ex)
    { taDisplay.setText(ex.toString());
    }
  }
  /** Execute SQL DDL, and modification commands */
  private void processSQLNonSelect(String sqlCommand)
  { try
    { // Get a new statement for the current connection
      statement = dBConnectionPane.getConnection().createStatement();
      // Execute a non-SELECT SQL command
      statement.executeUpdate(sqlCommand);
    }
    catch (SQLException ex)
    { taDisplay.setText(ex.toString());
    }
  }
  
  
  
  private void batchUpdate()
  { if (dBConnectionPane.getConnection() != null)
    { try
      { // 
    	String sqlCommands = strCommands.trim();
        String[] commands = sqlCommands.replace('\n', ' ').split(";");
        

        statement = dBConnectionPane.getConnection().createStatement();
        for (String aCommand: commands)
        {
        	statement.addBatch(aCommand);
        }
        
        long startTime = System.currentTimeMillis();
        statement.executeBatch();
        
        
        long endTime = System.currentTimeMillis();
        Platform.runLater(() ->
        { taDisplay.appendText("Batch update completed\n");
          taDisplay.appendText("The elapsed time is " +
            (endTime - startTime) + "\n");
          lblConnectionStatus.setText("Batch update succeeded");
        });
      }
      catch (Exception ex)
      { ex.printStackTrace();
      }
    }
  }
  public static void main(String[] args)
  { launch(args);
  }
  class DBConnectionPane extends BorderPane
  { private Connection connection;
    private Label lblConnectionStatus = new Label("No connection");
    private Button btConnect = new Button("Connect to DB");
    private ComboBox<String> cboDriver = new ComboBox<>(
      FXCollections.observableArrayList(
        "com.mysql.jdbc.Driver", "sun.jdbc.odbc.JdbcOdbcDriver",
        "oracle.jdbc.driver.OracleDriver"));
    private ComboBox<String> cboURL = new ComboBox<>(
      FXCollections.observableArrayList(
        "jdbc:mysql://localhost/javabook",
        "jdbc:odbc:exampleMDBDataSource"));
    private TextField tfUsername = new TextField();
    private PasswordField pfPassword = new PasswordField();
    /** Creates new form DBConnectionPanel */
    public DBConnectionPane()
    { cboDriver.setEditable(true);
      cboURL.setEditable(true);
      GridPane gridPane = new GridPane();
      gridPane.add(new Label("JDBC Drive"), 0, 0);
      gridPane.add(new Label("Database URL"), 0, 1);
      gridPane.add(new Label("Username"), 0, 2);
      gridPane.add(new Label("Password"), 0, 3);
      gridPane.add(cboDriver, 1, 0);
      cboDriver.setPrefWidth(350);
      cboDriver.getSelectionModel().selectFirst();
      gridPane.add(cboURL, 1, 1);
      cboURL.getSelectionModel().selectFirst();
      gridPane.add(tfUsername, 1, 2);
      gridPane.add(pfPassword, 1, 3);
      gridPane.add(btConnect, 1, 4);
      GridPane.setHalignment(btConnect, HPos.RIGHT);
      this.setTop(lblConnectionStatus);
      this.setCenter(gridPane);
      btConnect.setOnAction(e -> connectDB());
    }
    private void connectDB()
    { // Get database information from the user input
      String driver = cboDriver.getValue();
      String url = cboURL.getValue();
      String username = tfUsername.getText().trim();
      String password = new String(pfPassword.getText());
      // Connection to the database
      try
      { Class.forName(driver);
        connection = DriverManager.getConnection(
          url, username, password);
        lblConnectionStatus.setText("Connected to " + url);
        strCommands = "truncate tmp;";
        // Create random SQL 
		for (int i = 0; i < 400; i++)
		{
			Random rand = new Random();
			int  n = rand.nextInt(50) + 1;
			  
			strCommands = strCommands + "INSERT INTO tmp VALUES (" + n + ");";
		}
      }
      catch (java.lang.Exception ex)
      { ex.printStackTrace();
      }
    }
    /** Return connection */
    public Connection getConnection()
    { return connection;
    }
  }
}
