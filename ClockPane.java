package MVC;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class ClockPane extends Pane implements ChangeListener<Number>
{
	private int hour;
	private int minute;
	private int second;
	
	// dimension properties
	private DoubleProperty clockWidth = new SimpleDoubleProperty();
	public DoubleProperty clockWidthProperty() { return clockWidth; }
	public double getClockWidth() { return clockWidthProperty().get(); }
	public void setClockWidth(double value) { clockWidthProperty().set(value); }

	private DoubleProperty clockHeight = new SimpleDoubleProperty();
	public DoubleProperty clockHeightProperty()	{ return clockHeight; }
	public double getClockHeight() { return clockHeightProperty().get(); }
	public void setClockHeight(double value) { clockHeightProperty().set(value); }
	
	// constructor
	public ClockPane()
	{
		setClockWidth(250);
		clockWidth.addListener(this);
		setClockHeight(250);
		clockHeight.addListener(this);
		setGMT();
	}
	
	// methods
	
	public int getHour() { return hour; }
	public int getMinute() { return minute; }
	public int getSecond() { return second; }
	public void setHour(int hour)
	{
		this.hour = hour;
		paintClock();
	}
	public void setMinute(int minute)
	{
		this.minute = minute;
		paintClock();
	}
	public void setSecond(int second)
	{
		this.second = second;
		paintClock();
	}
	
	public void setGMT()
	{
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
	    hour = calendar.get(Calendar.HOUR_OF_DAY);
	    minute = calendar.get(Calendar.MINUTE);
	    second = calendar.get(Calendar.SECOND);
	    paintClock();
	}
	
	public void clockTick()
	{
		second += 1;
		if(second == 60)
		{
			second = 0;
			minute += 1;
			if(minute == 60)
			{
				minute = 0;
				hour += 1;
				if(hour == 24) hour = 0;
			}
		}
		paintClock();
	}
	
	private void paintClock()
	{
		// Initialize clock parameters
		double clockDimX = clockWidthProperty().get();
		double clockDimY = clockHeightProperty().get() - 110;
		double clockRadius = Math.min(clockDimX, clockDimY) * 0.4;
		double centerX = clockDimX / 2;
		double centerY = clockDimY / 2;
		
		// Draw circle
		Circle circle = new Circle(centerX, centerY, clockRadius);
		circle.setFill(Color.WHITE);
		circle.setStroke(Color.BLACK);
		
		// Draw second hand
		double sLength = clockRadius * 0.8;
		double secondX = centerX + sLength * Math.sin(second * (2 * Math.PI / 60));
		double secondY = centerY - sLength * Math.cos(second * (2 * Math.PI / 60));
		Line sLine = new Line(centerX, centerY, secondX, secondY);
		sLine.setStroke(Color.RED);
		
		// Draw minute hand
		double mLength = clockRadius * 0.65;
		double xMinute = centerX + mLength * Math.sin(minute * (2 * Math.PI / 60));
		double minuteY = centerY - mLength * Math.cos(minute * (2 * Math.PI / 60));
		Line mLine = new Line(centerX, centerY, xMinute, minuteY);
		mLine.setStroke(Color.BLUE);
		
		// Draw hour hand
		double hLength = clockRadius * 0.5;
		double hourX = centerX + hLength * Math.sin((hour % 12 + minute / 60.0) * (2 * Math.PI / 12));
		double hourY = centerY - hLength * Math.cos((hour % 12 + minute / 60.0) * (2 * Math.PI / 12));
		Line hLine = new Line(centerX, centerY, hourX, hourY);
		hLine.setStroke(Color.GREEN);
		getChildren().clear();  
		getChildren().addAll(circle, sLine, mLine, hLine);
		
		// minute marks
		for (double i = 0; i < 60; i++)
		{
			double percent = 0.95;
			if (i % 5 == 0) percent = 0.9;
			
			double xOuter = centerX + clockRadius * Math.sin(i * (2 * Math.PI / 60));
			double yOuter = centerY - clockRadius * Math.cos(i * (2 * Math.PI / 60));
			double xInner = centerX + percent * clockRadius * Math.sin(i * (2 * Math.PI / 60));
			double yInner = centerY - percent * clockRadius * Math.cos(i * (2 * Math.PI / 60));
			
			getChildren().add(new Line(xOuter, yOuter, xInner, yInner));
		}
		
		// hour marks
	    for (int i = 0; i < 12; i++)
	    {
	    	double x = centerX + 0.8 * clockRadius * Math.sin(i * (2 * Math.PI / 12));
	    	double y = centerY - 0.8 * clockRadius * Math.cos(i * (2 * Math.PI / 12));
	    	Text text = new Text(x - 4, y + 4, "" + ((i == 0) ? 12 : i));
	    	
	    	getChildren().add(text);
	    }
	}

	public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2)
	{
		paintClock();
	}
}
