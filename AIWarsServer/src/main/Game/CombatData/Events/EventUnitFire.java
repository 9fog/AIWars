package main.Game.CombatData.Events;

import net.minidev.json.JSONObject;

public class EventUnitFire extends Event {
	public final int unitId;	

	public EventUnitFire(int unitId) {
		this.unitId = unitId;
	}
	

	@Override
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		jo.put("event", "UnitFire");
		jo.put("unitId", unitId);
		return jo;	
	}	
}
