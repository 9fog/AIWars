package main.Game.CombatData.Orders;

import main.Game.Combat;

public abstract class Order {
	public boolean isReady = false;
	public boolean isInvalid = false;

	public abstract void processTick(long timePoint);
}
