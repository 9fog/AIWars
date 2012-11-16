package Sandbox;

//* Move specified unit to toX:toY
public class EventMoveUnit {
	public static String EVENT = "UnitMove";
	
	public final int unitId;
	public final int toX;
	public final int toY;
		
	public EventMoveUnit(int unitId, int toX, int toY) {
		this.unitId = unitId;
		this.toX = toX;
		this.toY = toY;
	}
}
