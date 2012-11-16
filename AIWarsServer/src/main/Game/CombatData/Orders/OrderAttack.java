package main.Game.CombatData.Orders;

import main.Game.CombatData.Unit;
import core.*;

public class OrderAttack extends Order {
	private final Unit _unit; 
	private final Unit _target; 
	
	private int _mode = 0; //0 - aiming, 1 - shoting;
	
	public OrderAttack(Unit u, Unit t) {
		_unit = u;
		_target = t;
		
		u.setOrder2(null);
		u.setOrder3(null);
	}
	
	
	public void processTick(long timePoint) {
		if (!_target.isAlive()) {
			_unit.setOrder(null);
			_unit.setOrder2(new OrderIdle(_unit));
			_unit.setOrder3(null);
			return;
		}
		
		/*
		Long range2 = _unit.getRange2(_target); 
		if (_unit.getShotRangeMin2()>range2) { //Противник слишком близко
			_unit.setOrder(null);
			_unit.setOrder2(new OrderIdle(_unit));
			_unit.setOrder3(null);
		}
		
		//Проверим, не ушел ли юнит из зоны видимости
		if (!_unit.getCombat().getMyVisibility(_unit.getSide()).containsKey(_target.getId())) {
			_unit.setOrder(null);
			_unit.setOrder2(new OrderIdle(_unit));
			_unit.setOrder3(null);
			return;			
		}		
		
		int dir = 0;
		
		if (range2>_unit.getShotRangeMax2()) { //Противник далековато. Надо сблизиться
			if (_unit.isMobile()) {
				_unit.setOrder(new OrderFollow(_unit, _target));
			} else {
				_unit.setOrder(null);
				_unit.setOrder2(new OrderIdle(_unit));
				_unit.setOrder3(null);				
			}
			return;
		} 
			
		if (!_unit.isArmed()) return;		
		
		//Раз попали сюда, значит дистанция до противника нормальная. Можно вести огонь
		dir = _unit.getDirection(_target);
		
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
						_unit.setOrder3(new OrderFire(_unit, _target));
					}					
				break;
		}
		*/		
	}
}
