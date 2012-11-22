package Sandbox;

//* Move specified unit to toX:toY
public class EventUnitMove {
	public static String EVENT = "UnitMove";
	
	public final int unitId;
	public final int toX;
	public final int toY;
		
	public EventUnitMove(int unitId, int toX, int toY) {
		this.unitId = unitId;
		this.toX = toX;
		this.toY = toY;
	}
}
