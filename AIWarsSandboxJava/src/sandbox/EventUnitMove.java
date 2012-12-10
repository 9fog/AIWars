package sandbox;

//* Move specified unit to toX:toY
public class EventUnitMove {
	public static String EVENT = "UnitMove";
	
	private final int unitId;
	private final int toX;
	private final int toY;
		
	public EventUnitMove(int unitId, int toX, int toY) {
		this.unitId = unitId;
		this.toX = toX;
		this.toY = toY;
	}

	public int getUnitId() {
		return unitId;
	}

	public int getToX() {
		return toX;
	}

	public int getToY() {
		return toY;
	}	
}
