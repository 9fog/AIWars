package sandbox;

//* Show enemy unit in x:y
public class EventUnitShow {
	public static String EVENT = "UnitShow";
	
	private final int unitId;
	private final int side;
	private final String type;
	private final int x;
	private final int y;
	private final boolean isArmed;
	private final boolean isMobile;
	
	public EventUnitShow(int unitId, int side, String type, int x, int y, boolean isArmed, boolean isMobile) {
		this.unitId = unitId;
		this.side = side;
		this.type = type;
		this.x = x;
		this.y = y;
		this.isArmed = isArmed;
		this.isMobile = isMobile;
	}

	public int getUnitId() {
		return unitId;
	}

	public int getSide() {
		return side;
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

	public boolean isArmed() {
		return isArmed;
	}

	public boolean isMobile() {
		return isMobile;
	}		
}
