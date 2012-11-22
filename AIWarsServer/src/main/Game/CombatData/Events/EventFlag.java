package main.Game.CombatData.Events;

import net.minidev.json.JSONObject;

public class EventFlag extends Event {
	public final int flagId;
	public final int ownerSide;
	public final int flagState; 
		
	public EventFlag(int flagId, int ownerSide, int flagState) {
		this.flagId = flagId;
		this.ownerSide = ownerSide;
		this.flagState = flagState;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		jo.put("event", "Flag");		
		jo.put("id", flagId);
		jo.put("side", ownerSide);
		jo.put("state", flagState);
		return jo;	
	}
}
