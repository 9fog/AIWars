package main.Game.CombatData;

import main.Game.Combat;
import main.Game.Net.Protocol;
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
		if (_timer.getState(timePoint)==0) {
			countUnits();
			
			int delta = _sidesPower[0] - _sidesPower[1]; 
			//log("delta = "+delta);
			if (delta>0) {
				incState(0, delta);
			} else
			if (delta<0) {
				incState(1, -delta);
			}
			
			_timer.setEndTime(Utils.getTimeStamp() + PERIOD);
		}
	}
	
	
	private void countUnits() {
		_sidesPower[0] = 0;
		_sidesPower[1] = 0;
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
	}
	
	private void incState(int side, int delta) {
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
		//_combat.send2all(Protocol.snd_Combat_Flag(_id, _side, _state));
	}
	
	private void log(String txt) {
		Utils.log(this.getClass().getName()+" #"+_id+": "+txt);
	}	
}
