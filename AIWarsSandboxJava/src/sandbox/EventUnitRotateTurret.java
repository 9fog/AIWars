package sandbox;

public class EventUnitRotateTurret {
	public static String EVENT = "UnitRotateTurret";
	
	public static final int[][] DIRECTIONS = {{7, 0, 1}, 
        									  {6, 0, 2},
                                              {5, 4, 3}};	
	
	private final int unitId;
	private final int turretLook;
	
	public EventUnitRotateTurret(int unitId, int turretLook) {
		this.unitId = unitId;
		this.turretLook = turretLook;
	}

	public int getUnitId() {
		return unitId;
	}

	public int getTurretLook() {
		return turretLook;
	}	
}
