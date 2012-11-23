package main.Game.CombatData.Events;

import net.minidev.json.JSONObject;

public class EventUnitBuild extends Event {
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

	@Override
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		jo.put("event", "UnitBuild");
		jo.put("unitId", unitId);		
		jo.put("type", type);		
		jo.put("x", x);
		jo.put("y", y);
		return jo;
	}

}
