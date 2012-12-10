package sandbox;

//* Hide enemy or own(dead) unit
public class EventUnitHide {
	public static String EVENT = "UnitHide";
	
	private final int unitId;

	public EventUnitHide(int unitId) {
		this.unitId = unitId;
	}

	public int getUnitId() {
		return unitId;
	}		
}
