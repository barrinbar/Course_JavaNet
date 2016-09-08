package MVC;


import javafx.stage.Stage;

import java.text.NumberFormat;
import java.util.Locale;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class ClockView extends Stage implements Comparable<ClockView>, ChangeListener<ClockView>
{

	public Country country;
	
	public ClockView(Country country)
	{
		minHeightProperty().set(350);
		minWidthProperty().set(350);
		this.country = country;
		ClockPane clock = new ClockPane();
		int hour = clock.getHour() + country.getTimeZone();
		if (hour >= 24) hour -= 24;
		clock.setHour(hour);
		
		Label lblCurrentTime = 
				new Label(String.format("%02d : %02d : 02d" ,
						clock.getHour(), clock.getMinute(), clock.getSecond()));
		
		Label lblPopulation =
				new Label ("Current population: " +
					NumberFormat.getNumberInstance(Locale.US).format(country.getPopulation()));

		Label lblCapital =
				new Label (country.getCapital());

		Label lblFunfact =
				new Label (country.getInfo());
		
		ImageView imgFlag =
				new ImageView(country.getImageURL());
		
		
	}
	
	/*@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		// TODO: implement
		
	}*/

	@Override
	public int compareTo(ClockView arg0) {
		if (this.equals(arg0))
			return 0;
		else
			return -1;
	}
	
	@Override
	public boolean equals(Object obj) {
		ClockView cv = (ClockView)obj;
		
		if (cv.country.equals(this.country))
			return true;
		else
			return false;
	}

	@Override
	public void changed(ObservableValue<? extends ClockView> observable, ClockView oldValue, ClockView newValue) {
		country = newValue.country;
	};
	
}
