package main.Game.CombatData;

import core.Utils;

public class Timer {
	private long _endTime;  //Millis
	private final String _name;
	
	public Timer(String name, long endTime) {
		_name = name;
		_endTime = endTime;
	}
	
	public Timer(int lifeTimeSeconds) {
		_name = "";
		_endTime = Utils.getTimeStamp() + lifeTimeSeconds*1000;
	}
	
	public void setEndTime(long endTime) {
		_endTime = endTime;
	}
	
	public long getState(long curTime) {
		if (curTime < _endTime) { //Таймер еще не кончился
			return _endTime - curTime;  
		}
		return 0;
	}	
	
	public long getEndTime() {
		return _endTime;
	}
	
	public String getName() {
		return _name;
	}
}
