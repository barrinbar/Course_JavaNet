package MVC;

public enum Country
{
	CA("Canada", -4, 35696192, "Ottawa", "Canada has over 30,000 lakes", "images/ca.jpg"),
	CN("China", 8, 1373380603, "Beijing", "Reincarnation is forbidden in China without government permission", "images/cn.jpg"),
	DE("Germany", 2, 79860594, "Berlin", "Over 100 Germans have been awarded the Nobel prize in their field, including Albert Einstein, who was born in Germany", "images/de.jpg"),
	FR("France", 2, 66811315, "Paris", "France is the most visited country in the world, with over 80 million visitors every year", "images/fr.jpg"),
	IL("Israel", 3, 8340917, "Jerusalem", "An estimated one million notes are left in the Western Wall each year", "images/il.jpg"),
	JP("Japan", 9, 126364031, "Tokyo", "Japan's birth rate is so low that adult diapers are sold more than baby diapers", "images/jp.jpg"),
	NL("Netherlands", 2, 16966074, "Amsterdam", "Around 20% of the Netherlands is located below sea level", "images/nl.jpg"),
	RU("Russia", 3, 146308896, "Moscow", "Lake Baikal, located in the south of the Siberian region, is the largest freshwater lake in the world", "images/ru.jpg"),
	UK("United Kingdom", 1, 64815891, "London", "To paste a stamp that has the Monarch’s face upside down is considered treason", "images/uk.jpg"),
	US("United States", -4, 324494050, "Washington DC", "Alaska was purchased from Russia in 1867 and is the largest state in the US by land area", "images/us.jpg");
	
	private String country;
	private int zone;
	private long population;
	private String capital;
	private String info;
	private String imageURL;
	Country(String country, int zone, long population, 
	  String capital, String info, String imageURL)
	{
		this.country = country;
		this.zone = zone;
		this.population = population;
		this.capital = capital;
		this.info = info;
		this.imageURL = imageURL;
	}
	
	public String toString()
	{
		return country;
	}
	
	public int getTimeZone()
	{
		return zone;
	}
	
	public long getPopulation()
	{
		return population;
	}
	
	public String getCapital()
	{
		return capital;
	}
	
	public String getInfo()
	{
		return info;
	}
	
	public String getImageURL()
	{
		return imageURL;
	}
}
