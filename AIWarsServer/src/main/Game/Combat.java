package main.Game;


import java.util.ArrayList;
import java.util.HashMap;

import main.Game.CombatData.Flag;
import main.Game.CombatData.MapObjectRock;
import main.Game.CombatData.Unit;
import main.Game.CombatData.CombatMap.CombatMap;
import main.Game.CombatData.CombatMap.CombatMapLoader;
import main.Game.CombatData.CombatMap.FlagPointer;
import main.Game.CombatData.CombatMap.ObjectPointer;
import main.Game.CombatData.CombatMap.UnitPointer;
import main.Game.CombatData.Events.Event;
import main.Game.CombatData.Events.EventUnitHide;
import main.Game.CombatData.Events.EventUnitShow;
import main.Game.CombatData.Orders.OrderMove;
import main.Game.DataTables.UnitTypesTable;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.jboss.netty.channel.Channel;

import core.Utils;

public class Combat {
	private final Channel _channel;
	private final int     _sides;
	
	private CombatMap _map;
	private boolean[] _readyList;
	private ArrayList<HashMap<Integer, Unit>> _squads, _deadList;
	private ArrayList<ArrayList<Event>> _events;
	private HashMap<Integer, Unit> _allUnits;
	private ArrayList<HashMap<Integer, Unit>> _visibility;
	private ArrayList<Flag> _flags;
	
	private int _tickNumber = 0; 
	
	public static final int MAX_TICKS = 10;
	public static final int TICK_TIME = 500;
	
	public static final int[][] DIRECTIONS = {{7, 0, 1}, 
		                                      {6, 0, 2},
		                                      {5, 4, 3}};
	
	public final int[] SQUADS_PRESET = {1, 1, 2, 2, 2, 2, 3, 3};
	
	//public final String[] DEFAULT_MAPS = {"testMap1x1.map", "testMap2x2.map"};
	public final String[] DEFAULT_MAPS = {"testMapSmall.map", "testMap2x2.map"};
	
	
	public Combat(Channel channel, int botsCount, String mapName) throws Exception{
		_channel = channel;
		_sides = botsCount;				
		
		if (mapName.equals("")) { //Default map
			if (_sides==2) {
				mapName = DEFAULT_MAPS[0];
			} else 
			if (_sides==4) {
				mapName = DEFAULT_MAPS[1];				
			} else {
				throw new Exception("Invalid bots count. Can be 2 or 4 only");
			}
		}
		
		CombatMapLoader cml = new CombatMapLoader(mapName);
		if (_sides!=cml.playersCount) {
			throw new Exception("Invalid bots count. Can be 2 or 4 only");			
		}
		
		_map = new CombatMap(cml);
		
		_squads   = new ArrayList<HashMap<Integer, Unit>>();
		_deadList = new ArrayList<HashMap<Integer, Unit>>();
		_allUnits = new HashMap<Integer, Unit>();
		_flags    = new ArrayList<Flag>();
		
		_visibility = new ArrayList<HashMap<Integer, Unit>>();
		for (int i=0; i<_sides; i++) {
			_visibility.add(new HashMap<Integer, Unit>());
			_deadList.add(new HashMap<Integer, Unit>());
		}		
		
		_readyList = new boolean[_sides];
		for (int j=0; j<_sides; j++) {
			_squads.add(new HashMap<Integer, Unit>());
			_readyList[j] = false;
		}		
		
		//Расставляем флаги и юниты
		for (ObjectPointer op : cml.objects) {
			if (op instanceof FlagPointer) {
				Flag f = new Flag(this, ((FlagPointer)op).id, op.x, op.y);
				_flags.add(f);
				_map.placeObject(f);
			} else
			if (op instanceof UnitPointer) {
				UnitPointer up = (UnitPointer)op;
				Unit u = new Unit(this, up.id, up.x, up.y, up.side, UnitTypesTable.getInstance().getType(up.typeId));				
				_map.placeObject(u);
				
				_allUnits.put(u.getId(), u);
				_squads.get(u.getSide()).put(u.getId(), u);
			} else {
				_map.placeObject(new MapObjectRock(), op.x, op.y);
			}
		}		
		
		
		//Отправить клентам стартовые состояния
		//Дамп карты, расположение флагов, 
		//расположение своих юнитов		
		for (int i=0; i<botsCount; i++) {
			sendToChannel(_channel, getStartInfo(i));
		}				
	}
	
	public int getSidesCount() {return _sides;}
	public CombatMap getMap() {return _map;} 
	public ArrayList<Flag> getFlags() {return _flags;}
		
	public String getStartInfo(int side) {
		JSONObject res = new JSONObject();
		
		res.put("_op", "init");
		res.put("_side", side);
		res.put("mapSizeX", _map.getSizeX());
		res.put("mapSizeY", _map.getSizeY());
		res.put("mapDump", _map.getDump());		
		
		JSONArray fl = new JSONArray();
		for (Flag f : _flags) {
			JSONObject fjo = new JSONObject();
			fjo.put("id", f.getId());
			fjo.put("x", f.getX());
			fjo.put("y", f.getY());
			fl.add(fjo);
		}
		res.put("flags", fl);
		
		JSONArray units = new JSONArray();
		for (Unit u : _squads.get(side).values()) {
			JSONObject ju = new JSONObject();
			ju.put("id", u.getId());
			ju.put("type", u.getType().role());
			ju.put("x", u.getX());
			ju.put("y", u.getY());
			units.add(ju);
		}
		res.put("units", units);
		
		return res.toJSONString();
	}
	
	public void processReady(int side) {
		_readyList[side] = true;
		
		if (allReady()) {
			nextTick();
		}
	}
	
	public void processOrderMove(int side, int unitId, int toX, int toY) {
		Unit u = _squads.get(side).get(unitId);
		if (u.isMobile()) {
			u.setOrder(new OrderMove(u, toX, toY));
		}
	}
	
	public boolean allReady() {
		for (int i=0; i<_sides; i++) {
			if (!_readyList[i]) {
				return false;
			}
		}
		return true;
	}
	
	private void nextTick() {
		for (int i=0; i<_sides; i++) {
			_readyList[i] = false;
		}
		
		if (_tickNumber>=MAX_TICKS) {
			for (int i=0; i<_sides; i++) {
				JSONObject cmd = new JSONObject();
				cmd.put("_op", "finish");
				cmd.put("_side", i);
				sendToChannel(_channel, cmd.toJSONString());
			}			
			return;
		}	
				
		long virtualTime = _tickNumber * TICK_TIME;
		_events = new ArrayList<ArrayList<Event>>(); 
		for (int i=0; i<_sides; i++) {
			_events.add(new ArrayList<Event>());
		}
		
		//TODO:: process moving, shoting and capturing
    	for (HashMap<Integer, Unit> hm : _squads) {
    		for (Unit u : hm.values()) {
    			u.processTick(virtualTime);
    		}
    	}		
		
		
    	//===============================================
    	
    	//Send tick events to bots
		for (int i=0; i<_sides; i++) {
			JSONObject cmd = new JSONObject();
			cmd.put("_op", "tick");
			cmd.put("_side", i);
			
			JSONArray events = new JSONArray();
			for (Event e : _events.get(i)) {
				events.add(e.toJSON());
			}
			cmd.put("events", events);
			
			sendToChannel(_channel, cmd.toJSONString());
		}
		
		_tickNumber++;
	}
	
	public void addEvent(int forSide, Event event) {
		_events.get(forSide).add(event);
	}
	
	
	public void notifyFlag(Flag ff) {
		int side = ff.getSide();
		for (Flag f : _flags) {
			if (f.getSide()!=side) {
				//stopFlagTimer();
				return;							
			}
		}
		//startFlagTimer();
	}	
	
	public void notifyDeath(main.Game.CombatData.Unit u) {
		/*
		_deadList.get(u.getSide()).put(u.getId(), u);
		if (_deadList.get(u.getSide()).size()==_squads.get(u.getSide()).size()) { //Все юниты одной из сторон мертвы
			if (_flagTimer==null) {
				Final(false);
			}
		}
		*/
	}
	
	public void updateVisibility(Unit u) {
		//!!! Этот алгоритм рассчитан только на две воюющие стороны !!!		
		for (int side = 0; side<_sides; side++) {
			if (side != u.getSide()) {
				for (Unit eu : _squads.get(side).values()) {
					if (eu.canSee(u)) {
						eu.addSeenUnit(u);
						u.addLookingUnit(eu);
						//log("Unit #"+eu.getId()+" can see unit #"+u.getId());
					} else {
						eu.removeSeenUnit(u);
						u.removeLookingUnit(eu);
						//log("Unit #"+eu.getId()+" can NOT see unit #"+u.getId());
					}
					
					if (u.canSee(eu)) { //Этот юнит видит врага
						u.addSeenUnit(eu);
						eu.addLookingUnit(u);						
						//log("Unit #"+u.getId()+" can see unit #"+eu.getId());
					} else {
						u.removeSeenUnit(eu);
						eu.removeLookingUnit(u);
						//log("Unit #"+u.getId()+" can NOT see unit #"+eu.getId());
					}
					
					if (eu.getLookingSize(u.getSide())==0) { //Этот юнит врага я не вижу
						if (_visibility.get(u.getSide()).containsKey(eu.getId())) { //Но до этого его видели
							_visibility.get(u.getSide()).remove(eu.getId());
							//TODO:: Отправить мне пакет "скрыть юнит"
							//---------
							addEvent(u.getSide(), new EventUnitHide(eu.getId()));
							//log("Hide unit " + eu.getId());
							//u.getPlayer().send(Protocol.snd_Combat_HideUnit(eu));
						}
					} else { //Этот юнит вижу
						if (!_visibility.get(u.getSide()).containsKey(eu.getId())) { //А раньше не видел
							_visibility.get(u.getSide()).put(eu.getId(), eu);
							//TODO:: Отправить мне пакет "показать юнит"
							addEvent(u.getSide(), new EventUnitShow(eu.getId(), eu.getType().role(), eu.getSide(), eu.getX(), eu.getY(), eu.isArmed(), eu.isMobile()));
							//log("Show unit " + eu.getId());							
							//u.getPlayer().send(Protocol.snd_Combat_ShowUnit(eu));
						}						
					}
				}
								
				if (u.getLookingSize(side)==0) { //Меня не видят
					//log("Меня не видят");
					if (_visibility.get(side).containsKey(u.getId())) { //А раньше видели
						_visibility.get(side).remove(u.getId());
						//TODO:: Отправить врагу пакет "скрыть юнит"
						addEvent(side, new EventUnitHide(u.getId()));
						//log("Hide unit " + u.getId());
						//_sides_players.get(side).send(Protocol.snd_Combat_HideUnit(u));
					}
				} else {  //Меня видят
					//log("Меня видят");
					if (!_visibility.get(side).containsKey(u.getId())) { //А раньше не видели
						_visibility.get(side).put(u.getId(), u);
						//TODO:: Отправить врагу пакет "показать юнит"
						addEvent(side, new EventUnitShow(u.getId(), u.getType().role(), u.getSide(), u.getX(), u.getY(), u.isArmed(), u.isMobile()));
						//log("Show unit " + u.getId());						
						//_sides_players.get(side).send(Protocol.snd_Combat_ShowUnit(u));
					}					
				}
			}			
		}		
	}

	public void sendToChannel(Channel ch, String data) {
		Utils.log("SRV > "+data);
		ch.write(data+"\0");
	}	
	
	public void stop() {
		
	}
}
