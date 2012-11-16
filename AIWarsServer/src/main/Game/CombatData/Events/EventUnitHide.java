package main.Game.CombatData.Events;

import net.minidev.json.JSONObject;

public class EventUnitHide extends Event {
	public final int unitId;

	public EventUnitHide(int unitId) {
		this.unitId = unitId;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		jo.put("event", "UnitHide");
		jo.put("unitId", unitId);		
		return jo;
	}

}
