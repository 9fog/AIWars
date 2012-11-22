package Sandbox;

//* Hide enemy or own(dead) unit
public class EventUnitHide {
	public static String EVENT = "UnitHide";
	
	public final int unitId;

	public EventUnitHide(int unitId) {
		this.unitId = unitId;
	}
}
