package sandbox;

public class EventUnitFire {
	public static String EVENT = "UnitFire";
	
	private final int unitId;

	public EventUnitFire(int unitId) {
		this.unitId = unitId;
	}

	public int getUnitId() {
		return unitId;
	}		
}
