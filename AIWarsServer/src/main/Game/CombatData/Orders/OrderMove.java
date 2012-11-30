package main.Game.CombatData.Orders;

import java.util.ArrayList;


import main.Game.Combat;
import main.Game.CombatData.Timer;
import main.Game.CombatData.Unit;
import main.Game.CombatData.CombatMap.ObjectPointer;
import main.Game.CombatData.Events.EventUnitMove;

public class OrderMove extends Order {
	private final Unit _unit; 
	private int _toX, _toY;	
	
	private ArrayList<ObjectPointer> _path;
	private ObjectPointer _curTarget;
	private Timer _timer;
	
	public OrderMove(Unit u, int toX, int toY) {
		_unit = u;
		_toX = toX;
		_toY = toY;
		
		_unit.setOrder2(new OrderIdle(_unit));
		_unit.setOrder3(null);
		
		_path = _unit.getCombat().getMap().findPath(_unit.getX(), _unit.getY(), _toX, _toY);
		
		if (_path==null) {
			isInvalid = true;
			return;
		}
		if (_path.size()==0) {
			isInvalid = true;
			return;
		}
	}
	
	private void prepare() {		
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
 	
	
	public void processTick(long timePoint) {
		if (!isReady) {
			prepare();
			return;
		}
		
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
			if (_path.size()>0) {
				setNextPoint(timePoint);
			} else {
				_unit.setOrder(null);
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
			_path = _unit.getCombat().getMap().findPath(_unit.getX(), _unit.getY(), _toX, _toY);			
			prepare();
		}

	}
}
