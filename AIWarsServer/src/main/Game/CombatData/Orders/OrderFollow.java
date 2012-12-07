package main.Game.CombatData.Orders;

import java.util.ArrayList;

import core.*;
import main.Game.Combat;
import main.Game.CombatData.Timer;
import main.Game.CombatData.Unit;
import main.Game.CombatData.CombatMap.ObjectPointer;
import main.Game.CombatData.Events.EventUnitMove;


public class OrderFollow extends Order {
	private final Unit _unit; 
	private final Unit _target;
	
	private ArrayList<ObjectPointer> _path;
	private ObjectPointer _curTarget;
	private Timer _timer;	
	
	public OrderFollow(Unit u, Unit t) {
		_unit = u;
		_target = t;		
		
		_unit.setOrder2(new OrderIdle(_unit));
		
		prepare();
	}
	
	
	private void prepare() {		
		_path = _unit.getCombat().getMap().findPath(_unit.getX(), _unit.getY(), _target.getX(), _target.getY());
		if (_path==null) {
			_unit.setOrder(null);
			return;
		}
		if (_path.size()==0) {
			_unit.setOrder(null);
			return;
		}
		isReady = true;		
	}
		
	
	public void processTick(long timePoint){
		if (!_unit.isMobile()) {
			_unit.setOrder(null);
			return;			
		}
		
		if (_path==null) {
			_unit.setOrder(null);
			return;
		}
		if (_timer == null) {
			setNextPoint(timePoint);
		}
				
		if (_timer.getState(timePoint)==0) { //Дошли до очередной точки
			if ((_path.size()>0)&&(_unit.getRange2(_target)>_unit.getShotRangeMax2())) {
				setNextPoint(timePoint);
			} else {
				_unit.setOrder(new OrderAttack(_unit, _target));
			}
		}			
	}
	
	
	private void setNextPoint(long timePoint) {
		_timer = new Timer("", timePoint + _unit.getMovingSpeed());
		
		_curTarget = _path.get(0);
		_path.remove(_curTarget);
		
		Integer nextX = _curTarget.x;
		Integer nextY = _curTarget.y;
		
		if (!_unit.getCombat().getMap().isObject(nextX, nextY)) {
			_unit.getCombat().getMap().moveObject(_unit, nextX, nextY);
			
			_unit.setGearLook(Combat.DIRECTIONS[nextY - _unit.getY() + 1][nextX - _unit.getX() + 1]);
			_unit.setXY(nextX, nextY);
			
			_unit.getCombat().addEvent(_unit.getSide(), new EventUnitMove(_unit.getId(), nextX, nextY), "move "+_unit.getSide()+" "+_unit.getId()+" "+nextX+" "+nextY+" "+_unit.getMovingSpeed());
			
			_unit.getCombat().updateVisibility(_unit);
			
			//Оповестить противника, если он видит
			if (_unit.getLookingUnits().size()>0) {
				for (int i=0; i<_unit.getCombat().getSidesCount(); i++) {
					if (i!=_unit.getSide()) {
						if (_unit.getLookingSize(i)>0) {
							_unit.getCombat().addEvent(i, new EventUnitMove(_unit.getId(), nextX, nextY), "move "+i+" "+_unit.getId()+" "+nextX+" "+nextY+" "+_unit.getMovingSpeed());							
						}
					}
				}
			}
		} else {
			prepare();
		}
	}	

}
