package main.Game.CombatData.Orders;

import main.Game.CombatData.Unit;
import core.*;

public class OrderGroundShot extends Order {
	private final Unit _unit; 
	private int _toX, _toY;	

	
	public OrderGroundShot(Unit u, int toX, int toY) {
		_unit = u;
		_toX = toX;
		_toY = toY;
	}
	
	
	public void processTick(long timePoint) {
		if (!_unit.isArmed()) {
			_unit.setOrder2(new OrderIdle(_unit));
			return;
		}		
		
		long range2 = Utils.getRange2(_unit.getX(), _unit.getY(), _toX, _toY);
		int tX, tY;
		if ((range2>_unit.getFireDisperce()*_unit.getFireDisperce()*4)&&(Utils.runProb(70))) {
			tX = _toX - _unit.getFireDisperce() + Utils.getRand(_unit.getFireDisperce()*2+1)-1;
			tY = _toY - _unit.getFireDisperce() + Utils.getRand(_unit.getFireDisperce()*2+1)-1;
		} else {
			tX = _toX;
			tY = _toY;
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
		
		int flyTime = 50*(int)Math.sqrt(range2);
		
		//!!!! String send = Protocol.snd_Combat_UnitFire(_unit, tX, tY, flyTime);
		//!!!! _unit.getPlayer().send(send);
		//Оповестить противника, если он видит
		if (_unit.getLookingSize()>0) {
			//!!!! _unit.getLookingUnit().getPlayer().send(send);
		}						
		
		//!!! Boom boom = new Boom(_unit, tX, tY, flyTime);	
		//!!!! _unit.getCombat().addBoom(boom);
		
		_unit.setOrder2(new OrderIdle(_unit));
	}	
}
