package MyBot1;

import java.util.ArrayList;

import Sandbox.AbstractBot;
import Sandbox.EventCoins;
import Sandbox.EventUnitBuild;
import Sandbox.EventUnitHit;
import Sandbox.EventUnitMove;
import Sandbox.EventUnitShow;
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
	
	private int enemyBaseX = -1;
	private int enemyBaseY = -1;
	
	private int buildTrigger = 0;
	
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
			if (eventObject instanceof EventUnitMove) {
				//TODO::...
			} else
			
			if (eventObject instanceof EventCoins) { //Есть бабло
				int coins = ((EventCoins)eventObject).coins;
				if (coins>=6) {
					if (buildTrigger%2==0) {
						_sim.sendOrderBuild(this, "attack");
					} else {
						_sim.sendOrderBuild(this, "recon");						
					}
					buildTrigger++;
				}
			} else
				
			if (eventObject instanceof EventUnitBuild) { //Построили новый юнит - в бой его!
				EventUnitBuild e = (EventUnitBuild)eventObject;
				
				if (enemyBaseX==-1) { //База еще не обнаружена. Работаем по флагам
					if (e.type.equals("recon")) { //Разведка подсвечивает базу противника
						int toX = 0;
						int toY = 0;
						if (e.x<5) {
							toX = _mapSizeX-5;
						} else {
							toX = 5;							
						}
						toY = _mapSizeY/2 - 1 + 2*buildTrigger%2;
						_sim.sendOrderMove(this, e.unitId, toX, toY);
					} else {
						ObjectFlag f = _flags.get(0);
						//Random seed imitation :)
						int delta = -1;
						if (events.indexOf(eventObject)%2==0) {
							delta = 1;
							f = _flags.get(_flags.size()-1);
						}
						_sim.sendOrderMove(this, e.unitId, f.x+delta, f.y-delta);
					}
				} else { //База врага уже обнаружена. Отправляем новый юнит к ней
					int toX=0, toY=0;
					if (enemyBaseX<5) {
						toX = 5 + 2*e.unitId%2; //Типа рандома такого. Чтоб юниты не отсылались в одну и ту же точку
					} else {
						toX = _mapSizeX - 5 - 2*e.unitId%2;
					}
					
					toY = enemyBaseY - 2 + 2*e.unitId%2;
					if (toX%2==0) {
						toY++;
					} else {
						toY--;
					}
					_sim.sendOrderMove(this, e.unitId, toX, toY);
				}
			}
			
			if (eventObject instanceof EventUnitShow) {
				//Земетили новый юнит противника. Если это база - запоминаем координаты, чтоб отсылать туда танки.
				EventUnitShow e = (EventUnitShow)eventObject;
				if (e.type.equals("base")) {
					enemyBaseX = e.x;
					enemyBaseY = e.y;
				}
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
