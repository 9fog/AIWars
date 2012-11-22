package Sandbox;

public class EventUnitRotateTurret {
	public static String EVENT = "UnitRotateTurret";
	
	public static final int[][] DIRECTIONS = {{7, 0, 1}, 
        									  {6, 0, 2},
                                              {5, 4, 3}};	
	
	public final int unitId;
	public final int turretLook;
	
	public EventUnitRotateTurret(int unitId, int turretLook) {
		this.unitId = unitId;
		this.turretLook = turretLook;
	}
}
