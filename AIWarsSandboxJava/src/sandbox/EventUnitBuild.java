package sandbox;

public class EventUnitBuild {
	public static String EVENT = "UnitBuild";
	
	private final int unitId;
	private final String type;
	private final int x;
	private final int y;
	
	public EventUnitBuild(int unitId, String type, int x, int y) {
		this.unitId = unitId;
		this.type = type;
		this.x = x;
		this.y = y;
	}

	public int getUnitId() {
		return unitId;
	}

	public String getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}			
}
