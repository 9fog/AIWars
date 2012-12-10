package sandbox;

public class EventUnitHit {
	public static String EVENT = "UnitHit";	
	
	private final int unitId;
	private final boolean isArmed;
	private final boolean isMobile;
	private final boolean isAlive;
	
	public EventUnitHit(int unitId, boolean isArmed, boolean isMobile,
			boolean isAlive) {
		this.unitId = unitId;
		this.isArmed = isArmed;
		this.isMobile = isMobile;
		this.isAlive = isAlive;
	}

	public int getUnitId() {
		return unitId;
	}

	public boolean isArmed() {
		return isArmed;
	}

	public boolean isMobile() {
		return isMobile;
	}

	public boolean isAlive() {
		return isAlive;
	}		
}
