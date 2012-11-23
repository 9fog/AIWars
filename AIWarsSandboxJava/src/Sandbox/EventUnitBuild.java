package Sandbox;

public class EventUnitBuild {
	public static String EVENT = "UnitBuild";
	
	public final int unitId;
	public final String type;
	public final int x;
	public final int y;
	
	public EventUnitBuild(int unitId, String type, int x, int y) {
		this.unitId = unitId;
		this.type = type;
		this.x = x;
		this.y = y;
	}			
}
