package main.Game.CombatData.Orders;

import core.*;
import main.Game.CombatData.Boom;
import main.Game.CombatData.Unit;
import main.Game.CombatData.Events.EventUnitFire;
import main.Game.CombatData.Events.EventUnitMove;

public class OrderFire extends Order {
	private final Unit _unit;
	private final Unit _target;
	
	private int tX, tY;
	
	
	public OrderFire(Unit u, Unit enemy) {
		_unit = u;
		_target = enemy;		
	}
	
	
	public void processTick(long timePoint) {
		if (!_target.isAlive()) {						
			return;
		}
		if (!_unit.isArmed()) {
			return;
		}
		
		if (_unit.getReloadTimer().getState(timePoint)==0) {
			long range2 = Utils.getRange2(_unit.getX(), _unit.getY(), _target.getX(), _target.getY());
			if ((range2>_unit.getFireDisperce()*_unit.getFireDisperce()*4)&&(Utils.runProb(70))) {
				tX = _target.getX() - _unit.getFireDisperce() + Utils.getRand(_unit.getFireDisperce()*2+1)-1;
				tY = _target.getY() - _unit.getFireDisperce() + Utils.getRand(_unit.getFireDisperce()*2+1)-1;
			} else {
				tX = _target.getX();
				tY = _target.getY();
			}
			
			if (tX>=_unit.getCombat().getMap().getSizeX()) {
				tX = _unit.getCombat().getMap().getSizeX()-1;
			} else 
			if (tX<0) {
				tX = 0;
			}
			
			if (tY>=_unit.getCombat().getMap().getSizeY()) {
				tY = _unit.getCombat().getMap().getSizeY()-1;
			} else 
			if (tY<0) {
				tY = 0;
			}
			
			int flyTime = 10*(int)range2;
			
			_unit.getCombat().addEvent(_unit.getSide(), new EventUnitFire(_unit.getId()), "fire "+_unit.getSide()+" "+_unit.getId()+" "+_target.getId());
			
			//Оповестить противника, если он видит
			if (_unit.getLookingUnits().size()>0) {
				for (int i=0; i<_unit.getCombat().getSidesCount(); i++) {
					if (i!=_unit.getSide()) {
						if (_unit.getLookingSize(i)>0) {
							_unit.getCombat().addEvent(i, new EventUnitFire(_unit.getId()), "fire "+i+" "+_unit.getId()+" "+_target.getId());							
						}
					}
				}
			}
			
			//log("fromX="+_unit.getX()+"; fromY="+_unit.getY()+"; tX="+tX+"; tY="+tY+"; range2 = "+range2+"; flyTime="+flyTime);
			
			//if (flyTime>_unit.getCombat().TICK_TIME) {
			Boom boom = new Boom(_unit, timePoint, tX, tY, flyTime);
			if (!boom.isDead) {
				_unit.getCombat().addBoom(boom);
			}
			
			_unit.getReloadTimer().setEndTime(timePoint + _unit.getFireRate());
		}		
	}
	
	private void log(String txt) {
		Utils.log(this.getClass().getName()+" #"+_unit.getId()+": "+txt);
	}	
}
