package main.Game.CombatData.Events;

import net.minidev.json.JSONObject;

public abstract class Event {
	public abstract JSONObject toJSON();
}
