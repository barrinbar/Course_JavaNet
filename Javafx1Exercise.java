package exc;

import javafx.scene.paint.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class Javafx1Exercise extends Application
{
    double x1;
    double y1;
    double x2;
    double y2;
	
	@Override 
  public void start(Stage primaryStage)
  { Pane pane = new Pane();
    double paneWidth = 250; 
    double paneHeight = 250;
    x1 = Math.random() * (paneWidth - 12);
    y1 = Math.random() * (paneHeight - 12);
    x2 = Math.random() * (paneWidth - 12);
    y2 = Math.random() * (paneHeight - 12);
    Circle circle1 = new Circle(x1, y1, 10);
    Circle circle2 = new Circle(x2, y2, 10);
    Line line = new Line(x1, y1, x2, y2);
    Text text = new Text((x1 + x2) / 2, (y1 + y2) / 2, 
      (int)new Point2D(x1, y1).distance(x2, y2) + "");
    ObservableList<String> options =
    		FXCollections.observableArrayList("RED", "GREEN", "BLUE", "WHITE");
    ComboBox<String> cmbColor = new ComboBox<String>(options);
    pane.getChildren().addAll(line,circle1, circle2, text, cmbColor);
    circle1.setFill(Color.WHITE);
    circle1.setStroke(Color.BLACK);
    circle2.setFill(Color.WHITE);
    circle2.setStroke(Color.BLACK);
    
    // Catch Drag Events
    circle1.setOnMouseDragged(e -> { 
    	x1=e.getX();
    	y1=e.getY();
    	circle1.setCenterX(x1);
    	circle1.setCenterY(y1);
    	line.setStartX(x1);
    	line.setStartY(y1);
    	text.setX((x1 + x2) / 2);
    	text.setY((y1 + y2) / 2); 
    	text.setText((int)new Point2D(x1, y1).distance(x2, y2) + "");
    });
    
    circle2.setOnMouseDragged(e -> {
    	x2=e.getX();
    	y2=e.getY();
    	circle2.setCenterX(x2);
    	circle2.setCenterY(y2);
    	line.setEndX(x2);
    	line.setEndY(y2);
    	text.setX((x1 + x2) / 2);
    	text.setY((y1 + y2) / 2); 
    	text.setText((int)new Point2D(x1, y1).distance(x2, y2) + "");
    });
    
    // Catch Combobox change event
    cmbColor.setOnAction(e ->
    {
    	switch (cmbColor.getValue() + "")
    	{
    		case "RED":
    		{
    			circle1.setFill(Color.RED);
    			circle2.setFill(Color.RED);
    			break;
    		}
    		case "GREEN":
    		{
    			circle1.setFill(Color.GREEN);
    			circle2.setFill(Color.GREEN);
    			break;
    		}
    		case "BLUE":
    		{
    			circle1.setFill(Color.BLUE);
    			circle2.setFill(Color.BLUE);
    			break;
    		}
    		case "WHITE":
    		{
    			circle1.setFill(Color.WHITE);
    			circle2.setFill(Color.WHITE);
    			break;
    		}
    	}
    });
    Scene scene = new Scene(pane, paneWidth, paneHeight);
    primaryStage.setTitle("javaFx1 Exercise"); 
    primaryStage.setScene(scene); 
    primaryStage.setAlwaysOnTop(true);
    primaryStage.show(); 
  }
  public static void main(String[] args)
  { launch(args);
  }
} 
