import com.oracle.jrockit.jfr.InvalidEventDefinitionException;

import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
public class JavaFx3Exercise16 extends Application
{ @Override 
  public void start(Stage primaryStage)
  { 
	class MyFan extends Pane{
		private BorderPane pane;
		BorderPane getPane()
		{
			return pane;
		}
		MyFan() {
		FanPane1 fan = new FanPane1();
	    HBox hBox = new HBox(5);
	    Button btPause = new Button("Pause");
	    Button btResume = new Button("Resume");
	    Button btReverse = new Button("Reverse");
	    hBox.setAlignment(Pos.CENTER);
	    hBox.getChildren().addAll(btPause, btResume, btReverse);
	    pane = new BorderPane();
	    pane.setCenter(fan);
	    pane.setTop(hBox);

	    //
	    //
	    //
	    //
	    
	    // Slider
	    Slider slSpeed = new Slider(0, 50, 5);
	    slSpeed.setShowTickLabels(false);
	    slSpeed.setShowTickMarks(false);
	    slSpeed.setMajorTickUnit(1);
	    slSpeed.setBlockIncrement(1);
	    slSpeed.valueProperty().addListener(ov ->
	    { 	
	      fan.setIncrement(slSpeed.getValue());
	    });
	    pane.setBottom(slSpeed);
	    
	    
	    
	    fan.setCenterShape(true); //????
	    pane.widthProperty().addListener(e ->
	    				fan.setW(pane.getWidth()-20));
	    pane.heightProperty().addListener(e ->
	    				fan.setH(pane.getHeight()-20));

	    EventHandler<ActionEvent> eventHandler = e -> {
	    	fan.move();
	                                                    };
	    Timeline animation = new Timeline(
	    	      new KeyFrame(Duration.millis(100), eventHandler));
	    	    animation.setCycleCount(Timeline.INDEFINITE);
	    	    animation.play(); // Start animation
	    
	    // Pause
	    btPause.setOnAction(e->animation.pause());
	    
	    // Resume
	    btResume.setOnAction(e->animation.play());
	    
	    // Reverse
	    btReverse.setOnAction(e->fan.reverse());
	    
		}
	}

    
    // Create 3 panes
	MyFan fp1 = new MyFan();
	MyFan fp2 = new MyFan();
	MyFan fp3 = new MyFan();
	SplitPane sp = new SplitPane(fp1.getPane(), fp2.getPane(), fp3.getPane());
	//HBox hb1 = new HBox(5);
	//hb1.getChildren().addAll(fp1, fp2, fp3);
	
    // Big Pane
    HBox hBoxAll = new HBox(5);
    Button btStartAll = new Button("Start All");
    Button btStopAll = new Button("Stop All");
    hBoxAll.setAlignment(Pos.CENTER);
    hBoxAll.getChildren().addAll(btStartAll, btStopAll);
    BorderPane bigPane = new BorderPane();
    bigPane.setCenter(sp);
    bigPane.setBottom(hBoxAll);
    
    Scene scene = new Scene(bigPane, 600, 200 + 20);
    primaryStage.setTitle("javaFx3Exercise"); 
    primaryStage.setScene(scene); 
    primaryStage.show();

    // TODO:Stop All
    
    // TODO: Play All
    
    
    //
    //
    //
    //
    //
    //
    //
    //
    //
    //
  }
  public static void main(String[] args)
  { launch(args);
  }
} 
class FanPane1 extends Pane
{ private double w = 200;
  private double h = 200;
  private double radius = Math.min(w, h) * 0.45;
  private Arc arc[] = new Arc[4];   
  private double startAngle = 30;
  private Circle circle = new Circle(w / 2, h / 2, radius);
  public FanPane1()
  { circle.setStroke(Color.BLACK);
    circle.setFill(Color.WHITE);
    getChildren().add(circle);
    for (int i = 0; i < 4; i++)
    { arc[i] = new Arc(w / 2, h / 2, radius * 0.9, 
        radius * 0.9, startAngle + i * 90, 35);
      arc[i].setFill(Color.RED); // Set fill color
      arc[i].setType(ArcType.ROUND);
      getChildren().addAll(arc[i]); 
    } 
  }
  private double increment = 5;
  public double getIncrement() {
	return increment;
}
	public void setIncrement(double increment) {
		this.increment = increment;
	}
	public void reverse()
	  { increment = -increment;
	  }
	
  public void move()
  { setStartAngle(startAngle + increment);
  }
  public void setStartAngle(double angle)
  { startAngle = angle;
    setValues();
  }
  public void setValues()
  { radius = Math.min(w, h) * 0.45;
    circle.setRadius(radius);
    circle.setCenterX(w / 2);
    circle.setCenterY(h / 2);
    for (int i = 0; i < 4; i++)
    { arc[i].setRadiusX(radius * 0.9);
      arc[i].setRadiusY(radius * 0.9);
      arc[i].setCenterX(w / 2);
      arc[i].setCenterY(h / 2);
      arc[i].setStartAngle(startAngle + i * 90);
    }     
  }
  public void setW(double w)
  { this.w = w;
    setValues();
  }
  public void setH(double h)
  { this.h = h;
    setValues();
  }
}
