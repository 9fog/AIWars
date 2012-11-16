package main.Game.CombatData.Orders;

import core.*;
import main.Game.CombatData.Unit;
import main.Game.Net.Protocol;

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
			//log("fromX="+_unit.getX()+"; fromY="+_unit.getY()+"; tX="+tX+"; tY="+tY+"; range2 = "+range2+"; flyTime="+flyTime);
			
			String send = Protocol.snd_Combat_UnitFire(_unit, tX, tY, flyTime);
			//!!!_unit.getPlayer().send(send);
			
			//Оповестить противника, если он видит
			if (_unit.getLookingSize()>0) {
				//!!! _unit.getLookingUnit().getPlayer().send(send);
			}						
			
			//!!! Boom boom = new Boom(_unit, tX, tY, flyTime);	
			//!!! _unit.getCombat().addBoom(boom);
			
			_unit.getReloadTimer().setEndTime(Utils.getTimeStamp() + _unit.getFireRate());
		}		
	}
	
	private void log(String txt) {
		Utils.log(this.getClass().getName()+" #"+_unit.getId()+": "+txt);
	}	
}
