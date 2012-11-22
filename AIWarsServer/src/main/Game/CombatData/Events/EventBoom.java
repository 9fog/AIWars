package main.Game.CombatData.Events;

import net.minidev.json.JSONObject;

public class EventBoom extends Event {
	public final int tX;
	public final int tY;	
	
	public EventBoom(int tx, int ty) {
		tX = tx;
		tY = ty;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		jo.put("event", "Boom");		
		jo.put("x", tX);
		jo.put("y", tY);
		return jo;	
	}
}
