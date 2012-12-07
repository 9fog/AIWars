package main.Game.CombatData.Orders;

import main.Game.CombatData.Unit;
import core.Utils;

public class OrderPassiveAttack extends Order {
	private final Unit _unit;
	private final Unit _enemy;
	
	private int _mode = 0; //0 - aiming, 1 - shoting;

	
	public OrderPassiveAttack (Unit unit, Unit enemy) {
		_unit = unit;
		_enemy = enemy;
	}
	
	
	public void processTick(long timePoint) {
		if (!_enemy.isAlive()) {
			_unit.setOrder2(new OrderIdle(_unit));
			_unit.setOrder3(null);
			return;
		}
		
		//Проверим, не ушел ли юнит из зоны видимости		
		if (!_unit.getLookingUnits().contains(_enemy)) {
			_unit.setOrder2(new OrderIdle(_unit));
			_unit.setOrder3(null);
			return;			
		}
		
		//Проверим, не ушел ли юнит из зоны поражения
		long range2 = _unit.getRange2(_enemy);		
		if ((range2<_unit.getShotRangeMin2())||(range2>_unit.getShotRangeMax2())) {
			_unit.setOrder2(new OrderIdle(_unit));
			_unit.setOrder3(null);
			return;
		}
		
		int dir = _unit.getDirection(_enemy);
		
		//Поворот башни отменяем, я думаю
		if (_unit.getTurretLook()!=dir) {
			_mode = 0;
		} else {
			_mode = 1;
		}				
		
		switch (_mode) {
			case 0: //Поворот башни
					if (!(_unit.getOrder3() instanceof OrderRotateTurret)) {
						_unit.setOrder3(new OrderRotateTurret(_unit, dir));
					} else {
						if (((OrderRotateTurret)_unit.getOrder3()).getDir()!=dir) {
							_unit.setOrder3(new OrderRotateTurret(_unit, dir));							
						}
					}
				break;
			case 1: //Стрельба
					if (!(_unit.getOrder3() instanceof OrderFire)) {
						_unit.setOrder3(new OrderFire(_unit, _enemy));
						_unit.getOrder3().processTick(timePoint);
					}
				break;
		}		
	}
		
	
	private void log(String txt) {
		Utils.log(this.getClass().getName()+" #"+_unit.getId()+": "+txt);
	}	
}
