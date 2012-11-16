package MyBot1;

import java.util.ArrayList;

import Sandbox.AbstractBot;
import Sandbox.EventMoveUnit;
import Sandbox.ObjectFlag;
import Sandbox.ObjectRock;
import Sandbox.ObjectUnit;
import Sandbox.Simulator;

public class MyBot extends AbstractBot {
	private Simulator _sim;
	
	private int _mapSizeX;
	private int _mapSizeY;

	private ArrayList<ObjectRock> _rocks;
	private ArrayList<ObjectFlag> _flags;
	private ArrayList<ObjectUnit> _units;
	
	private int _tickNumber = 0;
	
	@Override
	public void processInit(Simulator sim, int mapSizeX, int mapSizeY, ArrayList<ObjectRock> rocks, ArrayList<ObjectFlag> flags, ArrayList<ObjectUnit> units) {		
		// TODO ...
		_sim = sim;
		
		_mapSizeX = mapSizeX;
		_mapSizeY = mapSizeY;
		
		_rocks = rocks;
		_flags = flags;
		_units = units;
	}

	@Override
	public void processTick(ArrayList<Object> events) {
		// TODO...
		for (Object eventObject : events) {
			if (eventObject instanceof EventMoveUnit) {
				//TODO::...
			}
		}
		
		//Test orders
		if (_tickNumber==0) { //First tick
			//Give orders to all units - move to flags!
			int flagNum = 0;
			for (ObjectUnit u : _units) {
				if (!u.type.equals("base")) {
					ObjectFlag f = _flags.get(flagNum);
					_sim.sendOrderMove(this, u.id, f.x+1, f.y);
				
					flagNum++;
					if (flagNum>=_flags.size()) {
						flagNum = 0;
					}
				}
			}
			_tickNumber++;
		}
	}
}
