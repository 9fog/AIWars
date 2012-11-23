package main.Game.CombatData.Events;

import net.minidev.json.JSONObject;

public class EventCoins extends Event {
	public final int coins;
		
	public EventCoins(int coins) {
		this.coins = coins;
	}

	@Override
	public JSONObject toJSON() {
		JSONObject jo = new JSONObject();
		jo.put("event", "Coins");		
		jo.put("coins", coins);
		return jo;		
	}
}
