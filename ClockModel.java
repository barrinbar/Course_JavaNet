package MVC;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClockModel implements ControllerModelEvents
{
	ClockController controller;
	Country country;
	ClockViewList clocks;
	ClockView currentView;

	public ClockModel() {
		// Create clock view list
		for (Country cnt : Country.values())
		{
			ClockView cv = new ClockView(cnt);
			clocks.add(cv);
		}
	}
	
	@Override
	public void setCurrentView(Country country) {
		this.country = country;
		currentView = clocks.find(country);
	}

	public void setControllerListener(ClockController controller) {
		this.controller = controller;
	}
	
	public ObservableList<ClockView> getAllClocks() {
		return FXCollections.observableArrayList(clocks.list);
	}
}
