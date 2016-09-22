import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.PreparedStatement;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
public class Exercise38_03JavaFx extends Application
{   private TextField tfStudent = new TextField();
	private TextField tfStaff = new TextField();
	private TableView<Triplet> tableView = new TableView<>();
  private Button btShowContents = new Button("Execute Query");
  private Label lblStatus = new Label();
  // Statement for executing queries
  private Connection connection;
  private java.sql.PreparedStatement stmt;
  @Override // Override the start method in the Application class
  public void start(Stage primaryStage)
  {
	  GridPane gridPane = new GridPane();
	  gridPane.add(new Label("Student"), 0, 0);
	  gridPane.add(tfStudent, 1, 0);
	  gridPane.add(new Label("Staff"), 0, 1);
	  gridPane.add(tfStaff, 1, 1);
	  HBox hBox = new HBox(2);
    hBox.getChildren().addAll(gridPane, btShowContents);
    hBox.setAlignment(Pos.CENTER);
    BorderPane pane = new BorderPane();
    pane.setCenter(tableView);
    pane.setTop(hBox);
    pane.setBottom(lblStatus);
    // Create a scene and place it in the stage
    Scene scene = new Scene(pane, 420, 180);
    primaryStage.setTitle("Exercise38_04JavaFx"); // Set the stage title
    primaryStage.setScene(scene); // Place the scene in the stage
    primaryStage.setAlwaysOnTop(true);
    primaryStage.show(); // Display the stage  
    initializeDB();
    btShowContents.setOnAction(e -> showContents());    
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
      // Create a statement
      //stmt = connection.createStatement();
      /*stmt = connection.prepareStatement("select enrollment.ssn as 'STUDENT',"
        		+ " taughtby.ssn as 'STAFF',"
        		+ " enrollment.courseId as 'COURSE'"
      		+ " from taughtby, enrollment"
      		+ "where (enrollment.ssn = ? or (? = 0))"
      		+ " and (taughtby.ssn = ? OR (? = 0))"
      		+ " and (enrollment.courseId = taughtby.courseId)");*/
      
    }
    catch (Exception ex)
    { ex.printStackTrace();
    }
  }
  private void showContents()
  { int studentId = Integer.parseInt(tfStudent.getText().trim());
  int staffId = Integer.parseInt(tfStaff.getText().trim());
    try
    {
    	
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
    	/*if (studentId == 0) {
    		if (staffId == 0) {
    			stmt = connection.prepareStatement("select enrollment.ssn as 'STUDENT',"
    	        		+ " taughtby.ssn as 'STAFF',"
    	        		+ " enrollment.courseId as 'COURSE'"
    	      		+ " from taughtby, enrollment"
    	      		+ " where (enrollment.courseId = taughtby.courseId)");
    		}
    		else {
    			stmt = connection.prepareStatement("select enrollment.ssn as 'STUDENT',"
    	        		+ " taughtby.ssn as 'STAFF',"
    	        		+ " enrollment.courseId as 'COURSE'"
    	      		+ " from taughtby, enrollment"
    	      		+ " where (taughtby.ssn = ?)"
    	      		+ " and (enrollment.courseId = taughtby.courseId)");
    	        stmt.setInt(1, staffId);
    		}
    	}
    	else {
    		if (staffId == 0) {
    			stmt = connection.prepareStatement("select enrollment.ssn as 'STUDENT',"
    	        		+ " taughtby.ssn as 'STAFF',"
    	        		+ " enrollment.courseId as 'COURSE'"
    	      		+ " from taughtby, enrollment"
    	      		+ " where (enrollment.ssn = ?)"
    	      		+ " and (enrollment.courseId = taughtby.courseId)");
    	        stmt.setInt(1, studentId);
    		}
    		else {
    			stmt = connection.prepareStatement("select enrollment.ssn as 'STUDENT',"
    	        		+ " taughtby.ssn as 'STAFF',"
    	        		+ " enrollment.courseId as 'COURSE'"
    	      		+ " from taughtby, enrollment"
    	      		+ " where (enrollment.ssn = ?)"
    	      		+ " and (taughtby.ssn = ?)"
    	      		+ " and (enrollment.courseId = taughtby.courseId)");
    	        stmt.setInt(1, studentId);
    	        stmt.setInt(2, staffId);    			
    		}
    	}*/

      ResultSet resultSet = stmt.executeQuery();
      //resultSet = stmt.executeQuery(queryString);
      populateTableView(resultSet, tableView);
    } 
    catch (SQLException ex)
    { ex.printStackTrace();
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
  public static void main(String[] args)
  { launch(args);
  }
}
