package GringoBot2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import sandbox.AbstractBot;
import sandbox.EventCoins;
import sandbox.EventFlag;
import sandbox.EventUnitBuild;
import sandbox.EventUnitHide;
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

	private ArrayList<MyFlag> _flags;
	private HashMap<Integer, MyUnit> _units;
	
	private int enemyBaseX = -1;
	private int enemyBaseY = -1;
	
	private int buildTrigger = 0;
	
	//=================================
	//private HashMap<Integer, ArrayList<String>> _flaggers;	
	private HashMap<Integer, ArrayList<MyUnit>> _flagGuard;
	private ArrayList<MyUnit> _artillery; 
	private HashMap<String, Integer> _notMoved;	
	
	private HashMap<Integer, MyFlag> _myFlags;
	private HashMap<Integer, MyFlag> _enemyFlags;
	private int _mySide;
	private int _myBaseX;
	private int _myBaseY;
	private int _myBaseId;
	private int _enemyBaseId = -1;
	boolean _baseUnderAttack = false; 
	private ArrayList<String> _defenders; 
	private ArrayList<String> _attackers; 
	private ArrayList<MyUnit> _freeFlaggers;
	private ArrayList<String> _allFlaggers;
	private int _alertCounter=0;
	boolean _enemyBaseVisible = false;
	
	@Override
	public void processInit(Simulator sim, int mySide, int mapSizeX, int mapSizeY, ArrayList<ObjectRock> rocks, ArrayList<ObjectFlag> flags, ArrayList<ObjectUnit> units) {		
		// TODO ...
		_sim = sim;
		_mySide = mySide;
		
		_mapSizeX = mapSizeX;
		_mapSizeY = mapSizeY;
				
		_freeFlaggers = new ArrayList<MyUnit>();
		_units = new HashMap<Integer, MyUnit>();
		//_allFlaggers = new ArrayList<String>();
		for (ObjectUnit ou : units) {
			MyUnit u = new MyUnit(ou.id, ou.x, ou.y, ou.type);
			if (ou.type.equals("base")) {
				_myBaseX = u.x;
				_myBaseY = u.y; 
				_myBaseId = u.id;				
			} else {
				_freeFlaggers.add(u);
				//_allFlaggers.add(u.id+"");
				_units.put(u.id, u);
			}
		}
		
		_flags = new ArrayList<MyFlag>();
		for (ObjectFlag f : flags) {
			_flags.add(new MyFlag(f.id, f.x, f.y, Math.abs(_myBaseX-f.x)+Math.abs(_myBaseY-f.y)));			
		}		
		Collections.sort(_flags);
		
		/*
		_myFlags = new HashMap<Integer, MyFlag>();
		_enemyFlags = new HashMap<Integer, MyFlag>();
		for (MyFlag f : _flags) {
			_enemyFlags.put(f.id, new MyFlag(f.id, f.x, f.y, Math.abs(_myBaseX-f.x)+Math.abs(_myBaseY-f.y)));
		}
		*/
		//System.out.println("My flags = "+_myFlags.size());
		//System.out.println("Enemy flags = "+_enemyFlags.size());
		
		//_flaggers = new HashMap<Integer, ArrayList<String>>();
		_flagGuard = new HashMap<Integer, ArrayList<MyUnit>>();
		_artillery = new ArrayList<MyUnit>();
		_notMoved = new HashMap<String, Integer>();		
	}

	
	@Override
	public void processTick(WorldTick worldObject) {
		boolean baseUnderAttack = false; 
		boolean enemyBaseFound = false;
		int myCoins = 0;

		//Фиксируем перемещения юнитов
		for (EventUnitMove e : worldObject.getEventUnitMoveList()) {
			MyUnit u = _units.get(e.getUnitId());
			if (u!=null) {
				u.x = e.getToX();
				u.y = e.getToY();
				_notMoved.remove(u.id+"");
			}			
		}
		
		//Анализируем изменения состояний флагов
		for (EventFlag e : worldObject.getEventFlagList()) {
			//...
		}
		
		//Анализируем попадания по юнитам
		for (EventUnitHit e : worldObject.getEventUnitHitList()) {
			if (!e.isAlive()) {
				MyUnit u = _units.get(e.getUnitId());
				if (u!=null) {
					_units.remove(e.getUnitId());
					_freeFlaggers.remove(e.getUnitId()+"");
					_artillery.remove(e.getUnitId()+"");
					ArrayList<MyUnit> g = _flagGuard.get(u.flagId);
					if (g!=null) {
						g.remove(u);
					}
				}
			}
			
			if (e.getUnitId() == _myBaseId) {
				baseUnderAttack = true;
			}			
		}

		//Считаем деньги :)
		for (EventCoins e : worldObject.getEventCoinsList()) {
			myCoins = e.getCoins();
		}
		
		//Новые построенные юниты нуждаются в приказах
		for (EventUnitBuild e : worldObject.getEventUnitBuildList()) {
			MyUnit unit = new MyUnit(e.getUnitId(), e.getX(), e.getY(), e.getType());
			_units.put(e.getUnitId(), unit);
			_notMoved.put(e.getUnitId()+"", 0);

			if (e.getType().equals("artillery")) {
				_artillery.add(unit);
			} else {
				_freeFlaggers.add(unit);									
			}			
		}
		
		//Замечены новые вражескию юниты
		for (EventUnitShow e : worldObject.getEventUnitShowList()) {
			//Заметили новый юнит противника. Если это база - запоминаем координаты, чтоб отсылать туда танки.
			if (e.getType().equals("base")) {
				enemyBaseX = e.getX();
				enemyBaseY = e.getY();
				_enemyBaseId = e.getUnitId();
				
				_enemyBaseVisible = true;	
				enemyBaseFound = true;
			}
			
			//Если враг недалеко от базы - поднимаем тревогу!
			if (Math.abs(e.getX()-_myBaseX)+Math.abs(e.getY() - _myBaseY)<12) {
				_sim.sendOrderAttack(this, _myBaseId, e.getUnitId());
				baseUnderAttack = true;
			}			
		}
		
		//Потеряли врагов из вида
		for (EventUnitHide e : worldObject.getEventUnitHideList()) {
			if (e.getUnitId() == _enemyBaseId) {
				_enemyBaseVisible = false;
			}			
		}
		//=========================================================================================
		
		//Раздаем приказы соответственно полученным оперативным данным
		
		for (int i=2; i<=2; i++) {
			ArrayList<MyUnit> remove = new ArrayList<MyUnit>();
			if (_freeFlaggers.size()>0) {
				for (MyUnit u : _freeFlaggers) {							
					for (MyFlag f : _flags) {
						ArrayList<MyUnit> guard = _flagGuard.get(f.id);
						if (guard==null) {
							guard = new ArrayList<MyUnit>();
							_flagGuard.put(f.id, guard);
						}
						if (guard.size()<i) {
							guard.add(u);
							u.flagId = f.id;
							if (_myBaseX<5) {
								_sim.sendOrderMove(this, u.id, f.x-1, f.y-1+u.id%3);
							} else {
								_sim.sendOrderMove(this, u.id, f.x+1, f.y-1+u.id%3);							
							}
							remove.add(u);
							break;
						}					
					}
				}
			}
			for (MyUnit u : remove) {
				_freeFlaggers.remove(u);
			}
		}
		
		
		if ((_freeFlaggers.size()>0)&&(_enemyBaseVisible)) {		
			for (MyUnit u : _freeFlaggers) {
				_sim.sendOrderAttack(this, u.id, _enemyBaseId);
			}
			_freeFlaggers = new ArrayList<MyUnit>();
		}
		
		//Натравим артиллерию на базу врага
		if (enemyBaseFound) {
			for (MyUnit u : _artillery) {
				//_sim.sendOrderAttack(this, u.id, _enemyBaseId);
				if (_myBaseX<5) {
					_sim.sendOrderMove(this, u.id, _mapSizeX - 4 - u.id%5, _myBaseY + 5 - u.id%10);
				} else {					
					_sim.sendOrderMove(this, u.id, 4 + u.id%5, _myBaseY + 5 - u.id%10);
				}										
			}
		}
						
		HashMap<String, Integer> newNotMoved = new HashMap<String, Integer>();
		for (Entry<String, Integer> e : _notMoved.entrySet()) {
			if (e.getValue()>5) {
				if (_enemyBaseVisible) {
					//_sim.sendOrderAttack(this, Integer.parseInt(e.getKey()), _enemyBaseId);
					if (_myBaseX<5) {
						_sim.sendOrderMove(this, Integer.parseInt(e.getKey()), _mapSizeX - 4 - Integer.parseInt(e.getKey())%5, _myBaseY + 5 - Integer.parseInt(e.getKey())%10);
					} else {					
						_sim.sendOrderMove(this, Integer.parseInt(e.getKey()), 4 + Integer.parseInt(e.getKey())%5, _myBaseY + 5 - Integer.parseInt(e.getKey())%10);
					}											
				} else {
					MyUnit u = _units.get(Integer.parseInt(e.getKey()));
					if (!u.type.equals("recon")&&(_units.size()<14)) {
						if (_myBaseX<5) {
							_sim.sendOrderMove(this, Integer.parseInt(e.getKey()), _myBaseX + 5 + Integer.parseInt(e.getKey())%5, _myBaseY + 5 - Integer.parseInt(e.getKey())%10);
						} else {					
							_sim.sendOrderMove(this, Integer.parseInt(e.getKey()), _myBaseX - 5 - Integer.parseInt(e.getKey())%5, _myBaseY + 5 - Integer.parseInt(e.getKey())%10);
						}
					} else {
						//Разведке - искать базу противника. Танкам - мочить
						if (_myBaseX<5) {
							_sim.sendOrderMove(this, Integer.parseInt(e.getKey()), _mapSizeX - 4 - Integer.parseInt(e.getKey())%5, _myBaseY + 5 - Integer.parseInt(e.getKey())%10);
						} else {					
							_sim.sendOrderMove(this, Integer.parseInt(e.getKey()), 4 + Integer.parseInt(e.getKey())%5, _myBaseY + 5 - Integer.parseInt(e.getKey())%10);
						}						
					}
				}
			} else {
				newNotMoved.put(e.getKey(), e.getValue()+1);
			}
		}
		_notMoved = newNotMoved;														
		
		if (myCoins>=6) {
			if ((buildTrigger%3<2)||(_baseUnderAttack)) {
				_sim.sendOrderBuild(this, "attack");
			} else {
				_sim.sendOrderBuild(this, "recon");	
				
				if ((_artillery.size()<4)&&(_units.size()>8)) {
					_sim.sendOrderBuild(this, "artillery");
				}			
			}
			buildTrigger++;
		}
		
	}
}
