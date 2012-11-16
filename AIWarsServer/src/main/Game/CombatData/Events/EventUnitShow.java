package main.Game.CombatData.Events;

import net.minidev.json.JSONObject;

public class EventUnitShow extends Event {
	public final int unitId;
	public final String type;
	public final int side;
	public final int x;
	public final int y;
	public final boolean isArmed;
	public final boolean isMobile;
		
	public EventUnitShow(int unitId, String type, int side, int x, int y, boolean isArmed, boolean isMobile) {
		this.unitId = unitId;
		this.type = type;
		this.side = side;
		this.x = x;
		this.y = y;
		this.isArmed = isArmed;
		this.isMobile = isMobile;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		jo.put("event", "UnitShow");
		jo.put("unitId", unitId);		
		jo.put("type", type);		
		jo.put("side", side);
		jo.put("x", x);
		jo.put("y", y);
		jo.put("isArmed", isArmed);
		jo.put("isMobile", isMobile);
		return jo;
	}			
}
