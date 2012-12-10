package main.Game.CombatData.Orders;

import java.util.HashMap;

import main.Game.CombatData.Unit;
import core.*;

/**
 * Состояние юнита, когда он ни в кого не стреляет, но зорко бдит, ищет цель
 * 
 * @author 9fog
 *
 */
public class OrderIdle extends Order {
	private final Unit _unit; 

	
	public OrderIdle(Unit u) {
		_unit = u;
	}
	
	
	public void processTick(long timePoint) {			
		if (!_unit.isArmed()) { return; }
		
		HashMap<Integer, Unit> enemies = _unit.getCombat().getMyVisibility(_unit.getSide());
		if (enemies.size()>0) {
			Unit newTarget = null;
			long targetRange = Long.MAX_VALUE;
			for (Unit eu : enemies.values()) {
				//if (!eu.isArmed()) continue;
				
				long newRange = _unit.getRange2(eu); 
				if ((newRange>=_unit.getShotRangeMin2()) && (newRange<=_unit.getShotRangeMax2())
							&& (newRange<targetRange)) {
					newTarget = eu;
					targetRange = newRange;
					//break;
				}
			}
			
			if (newTarget!=null) {
				_unit.setOrder2(new OrderPassiveAttack(_unit, newTarget));
				_unit.getOrder2().processTick(timePoint);
			}
		}					
	}
	
	
	private void log(String txt) {
		Utils.log(this.getClass().getName()+" #"+_unit.getId()+": "+txt);
	}
}
