package main.Game.CombatData;

import java.util.ArrayList;
import java.util.HashMap;

import main.Game.Combat;
import main.Game.CombatData.Events.EventBoom;
import main.Game.CombatData.Events.EventUnitHit;
import main.Game.CombatData.Events.EventUnitMove;

import core.*;

public class Boom {
	private final int RANGE_FALL = 3;
	
	private final int _tX, _tY;
	private final Combat _combat;
	private final Unit _shoter;
	private Timer _timer;
	private long _createTime;
	public boolean isDead = false;
	
	public Boom(Unit shoter, long createTime, int x, int y, int flyTime) {
		_createTime = createTime;
		_tX = x;
		_tY = y;
		_combat = shoter.getCombat();
		_shoter = shoter;
		
		if (flyTime<=Combat.TICK_TIME) { //Если долетит быстро - взрываем сразу
			_timer = new Timer("", 0);
			processTick(0);
		} else { //Если будет лететь больше одного тика - пусть летит		
			_timer = new Timer("", _createTime + flyTime);
		}
	}
	
	
	public void processTick(long timePoint) {
		if (isDead) return;
		
		if (_timer.getState(timePoint)==0) {
			isDead = true;
			
			ArrayList<MapObject> victims = new ArrayList<MapObject>();
			
			//Найдем всех, кого задело
			int radius = _shoter.getFirePower()/RANGE_FALL;
			for (int i=_tX-radius; i<=_tX+radius; i++) {
				for (int j=_tY-radius; j<=_tY+radius; j++) {
					MapObject mo = null;
					try {
						mo = _combat.getMap().getObject(i, j);
						
						if (mo instanceof Unit) {
							if (((Unit)mo).isAlive()) {
								victims.add(mo);
							}
						}
					} catch (Exception e) {}
				}				
			}		
			
			for (int i=0; i<_combat.getSidesCount(); i++) {
				_combat.addEvent(i, new EventBoom(_tX, _tY), "boom "+i+" "+_tX+" "+_tY+" "+_shoter.getFirePower());
			}			
			
			//Нанести повреждения
			//ArrayList<HashMap<String, String>> victims_data = new ArrayList<HashMap<String, String>>();
			for (MapObject mo : victims) {
				int subDamage = (_shoter.getFirePower()/(((int)Utils.getRange2(_tX, _tY, mo.getX(), mo.getY())*RANGE_FALL+1)))/2;
				if (subDamage>0) {
					int damage = subDamage + Utils.getRand(subDamage);					
					Unit u = (Unit)mo;
					u.applyDamage(damage);
					
					for (int i=0; i<_combat.getSidesCount(); i++) {
						_combat.addEvent(i, new EventUnitHit(u.getId(), u.isArmed(), u.isMobile(), u.isAlive()), "hit "+i+" "+u.getId()+" "+u.getMaxHP()+" "+u.getHP()+" "+u.isArmed()+" "+u.isMobile()+" "+u.isAlive());						
					}
					/* old version
					HashMap<String, String> data = new HashMap<String, String>();
					data.put("id", u.getId() + "");
					data.put("curhp", u.getHP() + "");
					data.put("maxhp", u.getMaxHP() + "");
					data.put("armed", u.isArmed() + "");
					data.put("mobile", u.isMobile() + "");
					data.put("alive", u.isAlive() + "");
					victims_data.add(data);
					*/
				}
			}
			
			// old version
			//_combat.send2all(Protocol.snd_Combat_Boom(_tX, _tY, _shoter.getFirePower(), victims_data));
			//log("Boom lifetime "+(Utils.getTimeStamp() - _createTime)+"; flyTime="+_flyTime);
		}
	}
	
	private void log(String txt) {
		Utils.log(this.getClass().getName()+" at "+_tX+":"+_tY+": "+txt);
	}	
}
