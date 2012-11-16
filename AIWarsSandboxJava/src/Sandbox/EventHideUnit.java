package Sandbox;

//* Hide enemy or own(dead) unit
public class EventHideUnit {
	public static String EVENT = "UnitHide";
	
	public final int unitId;

	public EventHideUnit(int unitId) {
		this.unitId = unitId;
	}
}
