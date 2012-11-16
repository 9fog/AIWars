package main.Game.CombatData.Events;

import net.minidev.json.JSONObject;

public class EventUnitMove extends Event{
	public final int unitId;
	public final int toX;
	public final int toY;
	
	public EventUnitMove(int unitId, int toX, int toY) {
		this.unitId = unitId;
		this.toX = toX;
		this.toY = toY;
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		jo.put("event", "UnitMove");
		jo.put("unitId", unitId);
		jo.put("toX", toX);
		jo.put("toY", toY);
		return jo;
	}
}
