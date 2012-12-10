package main.Game;


import java.util.ArrayList;
import java.util.HashMap;

import main.Game.CombatData.Boom;
import main.Game.CombatData.CombatLog;
import main.Game.CombatData.CombatSide;
import main.Game.CombatData.Flag;
import main.Game.CombatData.MapObjectRock;
import main.Game.CombatData.Unit;
import main.Game.CombatData.UnitType;
import main.Game.CombatData.CombatMap.CombatMap;
import main.Game.CombatData.CombatMap.CombatMapLoader;
import main.Game.CombatData.CombatMap.FlagPointer;
import main.Game.CombatData.CombatMap.ObjectPointer;
import main.Game.CombatData.CombatMap.UnitPointer;
import main.Game.CombatData.Events.Event;
import main.Game.CombatData.Events.EventCoins;
import main.Game.CombatData.Events.EventUnitBuild;
import main.Game.CombatData.Events.EventUnitHide;
import main.Game.CombatData.Events.EventUnitShow;
import main.Game.CombatData.Events.EventWinner;
import main.Game.CombatData.Orders.OrderAttack;
import main.Game.CombatData.Orders.OrderBuild;
import main.Game.CombatData.Orders.OrderMove;
import main.Game.DataTables.UnitTypesTable;
import main.Game.Simulator.CombatSimulator;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import org.jboss.netty.channel.Channel;

import core.IdFactory;
import core.Utils;
import core.coreConfig;

public class Combat {
	public static final String LOG_URL = coreConfig.getInstance().get("logUrl");//"http://aiwars.9fog.com/log_player/view.html?url=";
	public static final String LOG_DIR = coreConfig.getInstance().get("logDir");
	
	private final CombatSimulator _parent;
	
	//private final Channel _channel;
	private final int     _sidesCount;
	
	private CombatMap _map;
	private boolean[] _readyList;
	private ArrayList<HashMap<Integer, Unit>> _squads, _deadList;
	private ArrayList<ArrayList<Event>> _events;
	private HashMap<Integer, Unit> _allUnits;
	private ArrayList<HashMap<Integer, Unit>> _visibility;
	private ArrayList<Flag> _flags;	
	private ArrayList<Boom> _booms;
	private ArrayList<Unit> _deadUnits;
	private ArrayList<CombatSide> _sides;
	private ArrayList<OrderBuild> _buildOrders;
	
	private int _unitIdFactory;
	
	private int _tickNumber = 0; 	
	private final int _maxTicks;
	
	private CombatLog _log;
	
	public static final int TICK_TIME = 500;
	
	public static final int[][] DIRECTIONS = {{7, 0, 1}, 
		                                      {6, 0, 2},
		                                      {5, 4, 3}};
	
	//public final int[] SQUADS_PRESET = {1, 1, 2, 2, 2, 2, 3, 3};
	
	//public final String[] DEFAULT_MAPS = {"testMap1x1.map", "testMap2x2.map"};
	public final String[] DEFAULT_MAPS = {"testMap1x1.map", "testMap2x2.map"};
		
//	public Combat(Main parent, Channel channel, ArrayList<String> botNames, int maxTicks, String mapName) throws Exception{
	public Combat(CombatSimulator parent, ArrayList<String> botNames, int maxTicks, String mapName) throws Exception{
		_parent = parent;
		
		//_channel = channel;
		_sidesCount = botNames.size();		
		_maxTicks = maxTicks;
		
		if (mapName.equals("")) { //Default map
			if (_sidesCount==2) {
				mapName = DEFAULT_MAPS[0];
			} else 
			if (_sidesCount==4) {
				mapName = DEFAULT_MAPS[1];				
			} else {
				throw new Exception("Invalid bots count. Can be 2 or 4 only");
			}
		}
		
		CombatMapLoader cml = new CombatMapLoader(mapName);
		if (_sidesCount!=cml.playersCount) {
			throw new Exception("Invalid bots count. Can be 2 or 4 only");			
		}
		
		_unitIdFactory = cml.unitIdFactory;
		
		_map = new CombatMap(cml);
		
		_squads      = new ArrayList<HashMap<Integer, Unit>>();
		_deadList    = new ArrayList<HashMap<Integer, Unit>>();
		_allUnits    = new HashMap<Integer, Unit>();
		_flags       = new ArrayList<Flag>();
		_booms       = new ArrayList<Boom>();
		_sides       = new ArrayList<CombatSide>();
    	_buildOrders = new ArrayList<OrderBuild>();
		
		//TODO: Надо будет объединить потом все for ..._sides в один.
		_visibility = new ArrayList<HashMap<Integer, Unit>>();
		for (int i=0; i<_sidesCount; i++) {
			_visibility.add(new HashMap<Integer, Unit>());
			_deadList.add(new HashMap<Integer, Unit>());
			CombatSide cs = new CombatSide();
			cs.sideId = i;
			_sides.add(cs);
		}		
		
		_readyList = new boolean[_sidesCount];
		for (int j=0; j<_sidesCount; j++) {
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
				
				if (u.getType().role().equalsIgnoreCase("base")) {
					_sides.get(u.getSide()).base = u;
				}
			} else {
				_map.placeObject(new MapObjectRock(), op.x, op.y);
			}
		}		
				
		//Проинициализировать COMBAT LOG
		_log = new CombatLog(LOG_DIR, Utils.getTimeStamp()+".clog");
		for (int i=0; i<_sidesCount; i++) {
			_log.append("player "+i+" "+botNames.get(i));
			for (Unit u : _squads.get(i).values()) {
				_log.append("create "+u.getId()+" "+u.getType().id()+" "+u.getX()+" "+u.getY()+" "+i);
			}
		}		
		_log.append("map "+_map.getSizeX()+" "+_map.getSizeY()+" "+_map.getDumpStr());		
		for (Flag f : _flags) {
			_log.append("flag "+f.getId()+" "+f.getX()+" "+f.getY());
		}		
		_log.append("end");		
	}
	
	public void startCombat() {
		//Отправить клентам стартовые состояния
		//Дамп карты, расположение флагов, 
		//расположение своих юнитов		
		for (int i=0; i<_sidesCount; i++) {
			_parent.send(getStartInfo(i));
			//sendToChannel(_channel, getStartInfo(i));
		}
		
		new readyChecker().start();		
	}
	
	public int getSidesCount() {return _sidesCount;}
	public CombatMap getMap() {return _map;} 
	public ArrayList<Flag> getFlags() {return _flags;}
	public CombatLog getLog() { return _log;} 
	public ArrayList<CombatSide> getSides() {return _sides;}
	
	public void addBoom(Boom b) {		
		_booms.add(b);
	}	
	
	public void notifyDeath(Unit u) {
		_deadUnits.add(u);
		_map.placeObject(null, u.getX(), u.getY());
		
		//Не уничтожена ли база одного из противников?
		if (u.getType().role().equals(Unit.ROLE_BASE)) {
			_sides.get(u.getSide()).isAlive = false;
			
			int aliveCount = 0;
			int aliveId = -1;
			for (int i=0; i<_sidesCount; i++) {
				if (_sides.get(i).isAlive) {
					aliveCount++;
					aliveId = i;
				}
			}
			
			if (aliveCount==1) {  //ПОБЕДА!!!
				for (int i=0; i<_sidesCount; i++) {
					addEvent(i, new EventWinner(aliveId), "winner "+aliveId);
				}
				_tickNumber = _maxTicks;
			}
		}
	}
		
	//public String getStartInfo(int side) {
	public JSONObject getStartInfo(int side) {
		JSONObject res = new JSONObject();
		
		res.put("_op", "init");		
		res.put("_side", side);
		res.put("logUrl", LOG_URL+_log.getFileName());
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
		
		//return res.toJSONString();
		return res;
	}
	
	public void processReady(int side) {
		_readyList[side] = true;
		/*
		if (allReady()) {
			nextTick();
		}
		*/
	}
	
	public void processOrderMove(int side, int unitId, int toX, int toY) {
		Unit u = _squads.get(side).get(unitId);
		
		if ((u==null)||(side!=u.getSide())||u.getType().role().equals("base")) {
			return;
		}
		
		if (u.isMobile()) {
			u.setOrder(new OrderMove(u, toX, toY));
		}
	}

	public void processOrderAttack(int side, int unitId, int targetId) {
		Unit u = _squads.get(side).get(unitId);
				
		if ((u==null)||(side!=u.getSide())) {
			return;
		}
		
		Unit target = _allUnits.get(targetId);
		if (target==null) return;
		
		if (u.isArmed()) {
			u.setOrder2(new OrderAttack(u, target));
		}		
	}	
	
	public void processOrderBuild(int side, String unitRole) {
		_buildOrders.add(new OrderBuild(side, unitRole));
	}
	
	public boolean allReady() {
		for (int i=0; i<_sidesCount; i++) {
			if (!_readyList[i]) {
				return false;
			}
		}
		return true;
	}
	
	private void nextTick() {
		_deadUnits = new ArrayList<Unit>();
		
		for (int i=0; i<_sidesCount; i++) {
			_readyList[i] = false;
		}
		
		if (_tickNumber>=_maxTicks) {
			for (int i=0; i<_sidesCount; i++) {
				JSONObject cmd = new JSONObject();
				cmd.put("_op", "finish");
				cmd.put("_side", i);
				_parent.send(cmd);
				//sendToChannel(_channel, cmd.toJSONString());
				_log.close();				
				if (_parent!=null) {
					_parent.reportFinish();
				}				
				_tickNumber = _maxTicks+5; //Это для readyChecker :)
			}			
			return;
		}	
		
		_log.append("turn "+_tickNumber);
				
		long virtualTime = _tickNumber * TICK_TIME;
		_events = new ArrayList<ArrayList<Event>>(); 
		for (int i=0; i<_sidesCount; i++) {
			_events.add(new ArrayList<Event>());
		}
		
		//Process orders
    	for (HashMap<Integer, Unit> hm : _squads) {
    		for (Unit u : hm.values()) {
    			u.processTick(virtualTime);
    		}
    	}		
    	
    	//Process booms
    	ArrayList<Boom> _toDel = new ArrayList<Boom>();
    	for (Boom b : _booms) {
    		b.processTick(virtualTime);   
    		if (b.isDead) {
    			_toDel.add(b);
    		}
    	}    	
    	for (Boom b : _toDel) {
    		_booms.remove(b);
    	}    	
    	
    	//Clear dead units
    	for (Unit u : _deadUnits) {    		
    		_allUnits.remove(u);
    		_squads.get(u.getSide()).remove(u.getId());    		
    	}
    	
    	//Process flag capturing
    	for (Flag f : _flags) {
    		f.processTick(virtualTime);
    	}
    	
		//ProcessBuilds
		for (OrderBuild ob : _buildOrders) {
			buildUnit(getSides().get(ob.side), UnitTypesTable.getInstance().getTypeByRole(ob.type));
		}    	
    	_buildOrders = new ArrayList<OrderBuild>();
		
    	//===============================================
    	
    	//Send tick events to bots
		for (int i=0; i<_sidesCount; i++) {
			JSONObject cmd = new JSONObject();
			cmd.put("_op", "tick");
			cmd.put("_side", i);
			
			JSONArray events = new JSONArray();
			for (Event e : _events.get(i)) {
				events.add(e.toJSON());
			}
			cmd.put("events", events);
			
			_parent.send(cmd);
			//sendToChannel(_channel, cmd.toJSONString());
		}
		
		_tickNumber++;
		_log.append("end");
	}
	
	public void addEvent(int forSide, Event event, String logEvent) {
		_events.get(forSide).add(event);
		_log.append(logEvent);
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
	
	public void buildUnit(CombatSide side, UnitType ut) {		
		if (side.coins>=ut.price()) {
			side.coins -= ut.price();
			addEvent(side.sideId, new EventCoins(side.coins), "coins "+side.sideId+" "+side.coins);
		} else {
			return;
		}
		
		boolean placeFound = false;
		int x=0, y=0;
		
		//Ищем место, куда выгрузить юнит. Ищем вокруг базы на соседних клетках
		for (int cX=-1; cX<=1; cX++) {
			for (int cY=-1; cY<=1; cY++) {				
				x = side.base.getX() + cX;
				y = side.base.getY() + cY;
				if ((x>=0) && (x<_map.getSizeX()) && (y>=0) && (y<_map.getSizeY()) && (_map.getObject(x, y)==null)) {
					placeFound = true;
					break;
				}
			}
			if (placeFound) break;
		}
		
		if (placeFound) {
			Unit u = new Unit(this, _unitIdFactory+1, x, y, side.sideId, ut);
			_unitIdFactory++;
			_map.placeObject(u);
		
			_allUnits.put(u.getId(), u);
			_squads.get(u.getSide()).put(u.getId(), u);
			
			addEvent(u.getSide(), new EventUnitBuild(u.getId(), u.getType().role(), x, y), "create "+u.getId()+" "+u.getType().id()+" "+x+" "+y+" "+side.sideId);
			
			updateVisibility(u);			
		} else {
			Utils.log("Cant find place");
		}
	}
	
	public void updateVisibility(Unit u) {	
		for (int side = 0; side<_sidesCount; side++) {
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

							addEvent(u.getSide(), new EventUnitHide(eu.getId()), "hide "+u.getSide()+" "+eu.getId());
							//log("Hide unit " + eu.getId());
						}
					} else { //Этот юнит вижу
						if (!_visibility.get(u.getSide()).containsKey(eu.getId())) { //А раньше не видел
							_visibility.get(u.getSide()).put(eu.getId(), eu);
							
							addEvent(u.getSide(), new EventUnitShow(eu.getId(), eu.getType().role(), eu.getSide(), eu.getX(), eu.getY(), eu.isArmed(), eu.isMobile()), "show "+u.getSide()+" "+eu.getSide()+" "+eu.getId()+" "+eu.getType().id()+" "+eu.getX()+" "+eu.getY()+" "+eu.isArmed()+" "+eu.isMobile());
							//log("Show unit " + eu.getId());							
						}						
					}
				}
								
				if (u.getLookingSize(side)==0) { //Меня не видят
					//log("Меня не видят");
					if (_visibility.get(side).containsKey(u.getId())) { //А раньше видели
						_visibility.get(side).remove(u.getId());
						//TODO:: Отправить врагу пакет "скрыть юнит"
						addEvent(side, new EventUnitHide(u.getId()), "hide "+u.getSide()+" "+u.getId());
						//log("Hide unit " + u.getId());
						//_sides_players.get(side).send(Protocol.snd_Combat_HideUnit(u));
					}
				} else {  //Меня видят
					//log("Меня видят");
					if (!_visibility.get(side).containsKey(u.getId())) { //А раньше не видели
						_visibility.get(side).put(u.getId(), u);
						//TODO:: Отправить врагу пакет "показать юнит"
						addEvent(side, new EventUnitShow(u.getId(), u.getType().role(), u.getSide(), u.getX(), u.getY(), u.isArmed(), u.isMobile()), "show "+side+" "+u.getSide()+" "+u.getId()+" "+u.getType().id()+" "+u.getX()+" "+u.getY()+" "+u.isArmed()+" "+u.isMobile());
						//log("Show unit " + u.getId());						
						//_sides_players.get(side).send(Protocol.snd_Combat_ShowUnit(u));
					}					
				}
			}			
		}		
	}

	/*
	 * Кого из врагов я вижу
	 */
	public HashMap<Integer, Unit> getMyVisibility(int side) {
		return _visibility.get(side);
	}
	
	public void sendToChannel(Channel ch, String data) {
		Utils.log("SRV > "+data);
		ch.write(data+"\0");
	}	
	
    private class readyChecker extends Thread {
        public void run() {
        	while (_tickNumber<_maxTicks) {
        		try {
        			sleep(25);

        			if (allReady()) {
        				nextTick();
        			}
        		} catch (Exception e) {
        			e.printStackTrace();
        		}
        	}
        	
        	nextTick();
        }
    }		
	
	public void stop() {
		_log.close();
		_tickNumber = _maxTicks+1;
	}
}
