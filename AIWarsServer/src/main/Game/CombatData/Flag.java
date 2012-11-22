package main.Game.CombatData;

import main.Game.Combat;
import main.Game.CombatData.Events.EventFlag;
import core.*;

public class Flag extends MapObject {
	private final int PERIOD = 500, STATE_MAX = 10;  
	private Combat _combat;
	private int _id;
	private int _side, _state;
	private int[] _sidesPower;
	private Timer _timer;
	
	public Flag(Combat c, int id, int x, int y) {
		_combat = c;
		_id = id;
		
		setXY(x, y);
		
		_side = -1;
		_state = 0;
		
		_sidesPower = new int[c.getSidesCount()];
		
		_timer = new Timer("", 0);		
	}
	
	public int getId() {return _id;}
	public int getSide() { return _side;}
	
	
	public void processTick(long timePoint) {
		//log("processTick");
		if (_timer.getState(timePoint)==0) {
			countUnits();
			
			int dominateSide = -1;
			int dominatePower = 0;
			int sidesNearFlag = 0;
			for (int i=0; i<_combat.getSidesCount(); i++) {
				if (_sidesPower[i]>0) {
					sidesNearFlag++;
				}
				
				if (_sidesPower[i]>dominatePower) {
					dominateSide = i;
					dominatePower = _sidesPower[i];
				}
			}
			
			if ((dominateSide>-1)&&(sidesNearFlag<3)) {			
				int delta = dominatePower;
				for (int i=0; i<_combat.getSidesCount(); i++) {						
					if (i!=dominateSide) {
						delta -= _sidesPower[i];
					}
				}
																			
				//log("delta = "+delta);
				if (delta>0) {
					//if (dominateSide==_side) {
						incState(dominateSide, delta);
					//} else {
					//	incState(dominateSide, -delta);						
					//}
				}			
			}
			
			_timer.setEndTime(timePoint + PERIOD);
		}
	}	
	
	private void countUnits() {
		for (int i=0; i<_combat.getSidesCount(); i++) {
			_sidesPower[i] = 0;			
		}
		
		for (int i=getX()-1; i<=getX()+1; i++) {
			for (int j=getY()-1; j<=getY()+1; j++) {
				//log(i+":"+j);
				try {
					MapObject mo = _combat.getMap().getObject(i, j); 
					if ((mo!=null)&&(mo instanceof Unit)&&((Unit)mo).isAlive()) {
						_sidesPower[((Unit)mo).getSide()]++;
					}
				}catch(Exception e) {}
			}
		}
		
		/*
		String log = "";
		for (int i=0; i<_combat.getSidesCount(); i++) {
			log += _sidesPower[i]+ " ";			
		}
		log(log);
		*/
	}
	
	private void incState(int side, int delta) {
		//log("Side:"+_side+"; Dominator:"+side+"; Delta:"+delta);
		if (_side==side) {
			if (_state<STATE_MAX) {
				_state+=delta;
				if (_state>STATE_MAX) {
					_state = STATE_MAX;
				}
			} else {
				return;
			}
		} else {
			if (_state==0) {
				_side = side;
				_state = delta;
				_combat.notifyFlag(this);
			} else {
				if (_state<delta) {
					_state=0;
				} else {
					_state-=delta;
				}
			}
		}
		
		//REPORT
		for (int i=0; i<_combat.getSidesCount(); i++) {
			_combat.addEvent(i, new EventFlag(_id, _side, _state), "capture "+i+" "+_id+" "+_side+" "+_state);
		}
	}
	
	private void log(String txt) {
		Utils.log(this.getClass().getName()+" #"+_id+": "+txt);
	}	
}
