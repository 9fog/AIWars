package main.Game.CombatData.Orders;

import main.Game.CombatData.Unit;
import main.Game.Net.Protocol;
import core.Timer;
import core.Utils;

public class OrderRotateTurret extends Order {
	private final Unit _unit;
	private final int _dir, _delta;
	private final Timer _rotateTimer;
	
	public OrderRotateTurret(Unit u, int dir) {
		_unit = u;
		_dir = dir;
		
		int da = dir - u.getTurretLook();
		if (Math.abs(da)>4) {
			_delta = (int)Math.signum(-da);
		} else {
			_delta = (int)Math.signum(da);
		}
		
		_rotateTimer = new Timer("", Utils.getTimeStamp() + _unit.getRotateSpeed());
	}
	
	public int getDir() { return _dir;}
	
	
	public void processTick() {
		if (_rotateTimer.getState()==0) {
			_unit.setTurretLook(_unit.getTurretLook() + _delta);

			String send = Protocol.snd_Combat_UnitRotateTurret(_unit);
			//!!! _unit.getPlayer().send(send);
			//Оповестить противника, если он видит
			if (_unit.getLookingSize()>0) {
				//!!! _unit.getLookingUnit().getPlayer().send(send);
			}			
			
			if (_unit.getTurretLook() == _dir) {
				_unit.setOrder3(null);
				return;
			} else {
				_rotateTimer.setEndTime(Utils.getTimeStamp() + _unit.getRotateSpeed());
			}
		}
	}
}
