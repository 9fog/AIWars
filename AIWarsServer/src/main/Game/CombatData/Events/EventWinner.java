package main.Game.CombatData.Events;

import net.minidev.json.JSONObject;

public class EventWinner extends Event {
	public final int winnerId;	
			
	public EventWinner(int winnerId) {
		this.winnerId = winnerId;
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		jo.put("event", "Winner");		
		jo.put("winnerId", winnerId);
		return jo;			
	}
}
