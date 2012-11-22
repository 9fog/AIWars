package Sandbox;

public class EventUnitHit {
	public static String EVENT = "UnitHit";	
	
	public final int unitId;
	public final boolean isArmed;
	public final boolean isMobile;
	public final boolean isAlive;
	
	public EventUnitHit(int unitId, boolean isArmed, boolean isMobile,
			boolean isAlive) {
		this.unitId = unitId;
		this.isArmed = isArmed;
		this.isMobile = isMobile;
		this.isAlive = isAlive;
	}			
}
