package MVC;


import javafx.application.Application;
import javafx.stage.Stage;

public class ClocksMVC extends Application
{
	public static void main(String[] args)
	{
		launch(args);
	}
	
	public void start(Stage primaryStage)
	{
		ClockModel model = new ClockModel();
		ClockController controller = new ClockController(primaryStage);
		controller.setModelListener(model);
		model.setControllerListener(controller);
	}
}
