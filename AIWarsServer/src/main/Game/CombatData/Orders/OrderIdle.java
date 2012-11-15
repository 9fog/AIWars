package main.Game.CombatData.Orders;

import java.util.HashMap;

import main.Game.CombatData.Unit;
import core.*;

public class OrderIdle extends Order {
	private final Unit _unit; 

	
	public OrderIdle(Unit u) {
		_unit = u;
	}
	
	
	public void processTick() {			
		if (!_unit.isArmed()) { return; }

		/*
		HashMap<Integer, Unit> enemies = _unit.getCombat().getMyVisibility(_unit.getSide());
		if (enemies.size()>0) {
			Unit newTarget = null;
			for (Unit eu : enemies.values()) {
				if (!eu.isAlive()) continue;
				
				long newRange = _unit.getRange2(eu); 
				if ((newRange>=_unit.getShotRangeMin2())&&(newRange<=_unit.getShotRangeMax2())) {
					newTarget = eu;
					break;
				}
			}
			
			if (newTarget!=null) {
				_unit.setOrder2(new OrderPassiveAttack(_unit, newTarget));
			}
		}	
		*/			
	}
	
	
	private void log(String txt) {
		Utils.log(this.getClass().getName()+" #"+_unit.getId()+": "+txt);
	}
}
