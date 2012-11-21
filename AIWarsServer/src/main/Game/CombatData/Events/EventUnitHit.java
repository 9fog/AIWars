package main.Game.CombatData.Events;

import net.minidev.json.JSONObject;

public class EventUnitHit extends Event {
	public final int unitId;
	public final boolean isArmed;
	public final boolean isMobile;
	public final boolean isAlive;	
	
	public EventUnitHit(int id, boolean armed, boolean mobile, boolean alive) {
		unitId = id;
		isArmed = armed;
		isMobile = mobile;
		isAlive = alive;
	}
	



	@Override
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		jo.put("event", "UnitHit");
		jo.put("unitId", unitId);
		jo.put("isArmed", isArmed);
		jo.put("isMobile", isMobile);
		jo.put("isAlive", isAlive);
		return jo;	
	}

}
