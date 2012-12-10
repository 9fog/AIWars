package mybot1;

import java.util.ArrayList;

import sandbox.AbstractBot;
import sandbox.EventCoins;
import sandbox.EventUnitBuild;
import sandbox.EventUnitHit;
import sandbox.EventUnitMove;
import sandbox.EventUnitShow;
import sandbox.ObjectFlag;
import sandbox.ObjectRock;
import sandbox.ObjectUnit;
import sandbox.Simulator;
import sandbox.WorldTick;


public class MyBot extends AbstractBot {
	private Simulator _sim;
	
	private int _mapSizeX;
	private int _mapSizeY;

	private ArrayList<ObjectRock> _rocks;
	private ArrayList<ObjectFlag> _flags;
	private ArrayList<ObjectUnit> _units;
	
	private int buildTrigger = 0;
	
	private int _tickNumber = 0;	
	
	@Override
	public void processInit(Simulator sim, int mySide, int mapSizeX, int mapSizeY, ArrayList<ObjectRock> rocks, ArrayList<ObjectFlag> flags, ArrayList<ObjectUnit> units) {		
		// TODO ...
		_sim = sim;
		
		_mapSizeX = mapSizeX;
		_mapSizeY = mapSizeY;
		
		_rocks = rocks;
		_flags = flags;
		_units = units;
	}

	@Override
	public void processTick(WorldTick worldObject) {
		// TODO...		
		for (EventUnitMove e : worldObject.getEventUnitMoveList()) {
			//.....
		}
		
		for (EventUnitShow e : worldObject.getEventUnitShowList()) {
			//.....
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
