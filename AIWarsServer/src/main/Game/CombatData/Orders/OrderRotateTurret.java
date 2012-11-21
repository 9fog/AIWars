package main.Game.CombatData.Orders;

import main.Game.CombatData.Unit;
import main.Game.CombatData.Events.EventUnitMove;
import main.Game.CombatData.Events.EventUnitRotateTurret;
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
	
	
	public void processTick(long timePoint) {
		if (_rotateTimer.getState()==0) {
			_unit.setTurretLook(_unit.getTurretLook() + _delta);

			_unit.getCombat().addEvent(_unit.getSide(), new EventUnitRotateTurret(_unit.getId(), _unit.getTurretLook()), "turret "+_unit.getSide()+" "+_unit.getId()+" "+_unit.getTurretLook());
			
			//Оповестить противника, если он видит
			if (_unit.getLookingUnits().size()>0) {
				for (int i=0; i<_unit.getCombat().getSidesCount(); i++) {
					if (i!=_unit.getSide()) {
						if (_unit.getLookingSize(i)>0) {
							_unit.getCombat().addEvent(i, new EventUnitRotateTurret(_unit.getId(), _unit.getTurretLook()), "turret "+i+" "+_unit.getId()+" "+_unit.getTurretLook());							
						}
					}
				}
			}
			
			if (_unit.getTurretLook() == _dir) {
				_unit.setOrder3(null);
				return;
			} else {
				_rotateTimer.setEndTime(timePoint + _unit.getRotateSpeed());
			}
		}
	}
}
