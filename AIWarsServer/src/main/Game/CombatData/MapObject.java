package main.Game.CombatData;

public class MapObject {
	private int _x, _y;

	public MapObject() {}
	
	public MapObject(int x, int y) {
		_x = x;
		_y = y;
	}
	
	public int getX() {return _x;}
	public int getY() {return _y;}
	public void setXY(int x, int y) {_x = x; _y = y;}	
}
