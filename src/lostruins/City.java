package lostruins;

import java.util.ArrayList;

public class City {

	private int index;
	private String name;
	private int x;
	private int y;
	private int h;
	private ArrayList<City> linksList = new ArrayList<City>();
	
	public City(int index, String name, int x, int y, int h) {
		this.index = index;
		this.name = name;
		this.x = x;
		this.y = y;
		this.h = h;
	}

	//GETTERS AND SETTERS
	public String getName() {
		return name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public ArrayList<City> getLinksList() {
		return linksList;
	}

	public void setLinksList(ArrayList<City> linksList) {
		this.linksList = linksList;
	}
	
	
	
	
}
