package Sandbox;

//* Show enemy unit in x:y
public class EventShowUnit {
	public static String EVENT = "UnitShow";
	
	public final int unitId;
	public final int side;
	public final String type;
	public final int x;
	public final int y;
	public final boolean isArmed;
	public final boolean isMobile;
	
	public EventShowUnit(int unitId, int side, String type, int x, int y, boolean isArmed, boolean isMobile) {
		this.unitId = unitId;
		this.side = side;
		this.type = type;
		this.x = x;
		this.y = y;
		this.isArmed = isArmed;
		this.isMobile = isMobile;
	}		
}
