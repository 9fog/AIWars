package main.Game.CombatData.Events;

import net.minidev.json.JSONObject;

public class EventUnitRotateTurret extends Event {
	public final int unitId;
	public final int turretLook;
	
	public EventUnitRotateTurret(int unitId, int turretLook) {
		this.unitId = unitId;
		this.turretLook = turretLook;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		jo.put("event", "UnitRotateTurret");
		jo.put("unitId", unitId);
		jo.put("turretLook", turretLook);
		return jo;
	}
}
