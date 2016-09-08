package MVC;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ClockController extends Stage 
  implements ViewControllerEvents, ModelControllerEvents
{
	private ComboBox<ClockView> cbCountries = new ComboBox<ClockView>();
	private Button btnSelect = new Button();
	ClockModel model;
	
	public ClockController(Stage primaryStage) {
		
		// Create stage and panes
		GridPane inputPane = new GridPane();
		btnSelect.setText("Submit");
		
		// Fill Combo
		cbCountries.setItems(model.getAllClocks());
		/*for (Country cnt : Country.values())
		{
			cbCountries.getItems().add(cnt.toString());
		}*/
		
		inputPane.add(new Label("Select Country: "), 0, 0);
		inputPane.add(cbCountries, 1, 0);
		inputPane.add(btnSelect, 2, 0);
		inputPane.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: blue;");

		// Create a scene and place it in the stage
		Scene scene = new Scene(inputPane);
		primaryStage.setTitle("MVC Clocks - Controller"); // Set the stage title
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
		
        cbCountries.valueProperty().addListener(new ChangeListener<ClockView>() {
            

			@Override
			public void changed(ObservableValue<? extends ClockView> observable, ClockView oldValue,
					ClockView newValue) {
				viewSelected(((ClockView)cbCountries.getSelectionModel().
					getSelectedItem()).country);
				
			}    
        });

		/*btnSelect.setOnAction(e -> {
			viewSelected(((ClockView)cbCountries.getSelectionModel().
					getSelectedItem()).country);
			//model.setCurrentView(
			//		Country.values()[cbCountries.getSelectionModel().getSelectedIndex()]);
		});*/
	}

	@Override
	public void updateLog(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void viewSelected(Country country) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void viewClosed(Country country) {
		// TODO Auto-generated method stub
		
	}

	public void setModelListener(ClockModel model) {
		// TODO Auto-generated method stub
		this.model = model;
		
	}	
}
