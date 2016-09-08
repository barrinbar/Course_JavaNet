package MVC;


import java.util.ArrayList;
import java.util.Iterator;

public class ClockViewList implements Iterable<ClockView>
{
	ArrayList<ClockView> list = new ArrayList<ClockView>();
	
	public void add(ClockView view)
	{
		list.add(view);
	}
	
	public void remove(ClockView view)
	{
		list.remove(view);
	}
	
	public boolean contains(ClockView view)
	{
		return list.contains(view);
	}
	
	public ClockView find(ClockView view)
	{
		return find(view.country);
	}
	
	public ClockView find(Country country)
	{
		for (ClockView cv: list)
			if(cv.country == country)
				return cv;
		return null;
	}

	@Override
	public Iterator<ClockView> iterator()
	{
		return list.iterator();
	}
}
