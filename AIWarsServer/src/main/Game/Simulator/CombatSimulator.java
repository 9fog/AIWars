package main.Game.Simulator;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import core.Utils;

import main.Game.BotData;
import main.Game.Combat;
import main.Game.DataTables.UsersTable;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import sandbox.AbstractBot;
import sandbox.Connector;
import sandbox.EventBoom;
import sandbox.EventCoins;
import sandbox.EventFlag;
import sandbox.EventUnitBuild;
import sandbox.EventUnitFire;
import sandbox.EventUnitHide;
import sandbox.EventUnitHit;
import sandbox.EventUnitMove;
import sandbox.EventUnitRotateTurret;
import sandbox.EventUnitShow;
import sandbox.ObjectFlag;
import sandbox.ObjectRock;
import sandbox.ObjectUnit;
import sandbox.Simulator;
import sandbox.WorldTick;

//Версия для симуляции боя между ботами, лежащими на сервере в папке bots
public class CombatSimulator extends Simulator{
	public Combat _combat;
	
	public String startCombat(ArrayList<String> botIds, int maxTicks, String mapName) {				
		_sides = new ArrayList<AbstractBot>();
		
		ArrayList<String> botNames = new ArrayList<String>();
		
		for (String botId : botIds) {
			//Utils.log("Loading botData for [" + botId + "]");
			
			BotData botData = null;
			try {
				botData = UsersTable.getBotData(botId);
			} catch (Exception e) {
				e.printStackTrace();
				return "Error1: "+e.getMessage();
			}
			
			if (botData==null) {
				return "Error2: bot not found";
			}
			
			Utils.log("Creating bot [" + botData.botName + "]");
			File jarFile = new File("bots/user"+botId+"/"+botData.jarName);

			try {
				URL jarURL = jarFile.toURI().toURL();
				URLClassLoader classLoader = new URLClassLoader(new URL[] { jarURL });
				
				Class pl = classLoader.loadClass(botData.jarName.replaceAll(".jar", "")+".MyBot");
				AbstractBot b = (AbstractBot)pl.getConstructor(new Class[0]).newInstance(new Object[0]);

				_sides.add(b);
			} catch (Exception e) {
				e.printStackTrace();
				return "Error3: "+e.getMessage();
			}
			
			botNames.add(botData.botName);
			//Utils.log("Added bot "+botData.botName);
		}
		
		try {
			_combat = new Combat(this, botNames, maxTicks, mapName);
		} catch (Exception e) {
			e.printStackTrace();
			return "Error4: "+e.getMessage();
		}		
		
		_combat.startCombat();
		return "";
	}
	
	public String getLogUrl() {
		return Combat.LOG_URL+_combat.getLog().getFileName();
	}
	
	public void reportFinish() {
	}		
		
	public void send(JSONObject cmd) {
		try {
			Method method = getClass().getMethod("process_"+cmd.get("_op"), JSONObject.class);
			method.invoke(this, cmd); 
		} catch (Exception e) {
			e.printStackTrace();
		}			  
	}
	
	//==== Приказы от ботов =====================================================
	@Override
	public void sendOrderMove(AbstractBot b, int unitId, int toX, int toY) {
		int side = _sides.indexOf(b);		
		_combat.processOrderMove(side, unitId, toX, toY);
	}

	@Override
	public void sendOrderAttack(AbstractBot b, int unitId, int targetId) {
		int side = _sides.indexOf(b);
		_combat.processOrderAttack(side, unitId, targetId);		
	}  
	  
	@Override
	public void sendOrderBuild(AbstractBot b, String type) {
		int side = _sides.indexOf(b);
		_combat.processOrderBuild(side, type);
	}

	public void sendReady(int side) {
		_combat.processReady(side);
	}  
	//==========================================================================
	
	
	public void process_init(JSONObject cmd) {
		int side = Integer.parseInt(cmd.get("_side")+"");

		ArrayList<ObjectRock> rocks = new ArrayList<ObjectRock>();
		ArrayList<ObjectFlag> flags = new ArrayList<ObjectFlag>();
		ArrayList<ObjectUnit> units = new ArrayList<ObjectUnit>();

		//Rocks
		JSONArray ja = (JSONArray)cmd.get("mapDump");
		for (Object o : ja) {
			JSONObject jo = (JSONObject)o;
			rocks.add(new ObjectRock(Integer.parseInt(jo.get("x")+""), Integer.parseInt(jo.get("y")+"")));
		}

		//Flags
		ja = (JSONArray)cmd.get("flags");
		for (Object o : ja) {
			JSONObject jo = (JSONObject)o;
			flags.add(new ObjectFlag(Integer.parseInt(jo.get("id")+""), Integer.parseInt(jo.get("x")+""), Integer.parseInt(jo.get("y")+"")));
		}

		//Units
		ja = (JSONArray)cmd.get("units");
		for (Object o : ja) {
			JSONObject jo = (JSONObject)o;
			units.add(new ObjectUnit(Integer.parseInt(jo.get("id")+""), jo.get("type")+"", Integer.parseInt(jo.get("x")+""), Integer.parseInt(jo.get("y")+"")));
		}

		_sides.get(side).processInit(this, side, Integer.parseInt(cmd.get("mapSizeX")+""), Integer.parseInt(cmd.get("mapSizeY")+""), rocks, flags, units);

		sendReady(side);
	}
	  
	public void process_tick(JSONObject cmd) {
		  int side = Integer.parseInt(cmd.get("_side")+"");

		  ArrayList<EventBoom> eventBoomList = new ArrayList<EventBoom>();
		  ArrayList<EventCoins> eventCoinsList = new ArrayList<EventCoins>();
		  ArrayList<EventFlag> eventFlagList = new ArrayList<EventFlag>();
		  ArrayList<EventUnitBuild> eventUnitBuildList = new ArrayList<EventUnitBuild>();	
		  ArrayList<EventUnitFire> eventUnitFireList = new ArrayList<EventUnitFire>();	
		  ArrayList<EventUnitHide> eventUnitHideList = new ArrayList<EventUnitHide>();	
		  ArrayList<EventUnitHit> eventUnitHitList = new ArrayList<EventUnitHit>();
		  ArrayList<EventUnitMove> eventUnitMoveList = new ArrayList<EventUnitMove>();
		  ArrayList<EventUnitRotateTurret> eventUnitRotateTurrerList = new ArrayList<EventUnitRotateTurret>();
		  ArrayList<EventUnitShow> eventUnitShowList = new ArrayList<EventUnitShow>();
		  	  
		  JSONArray eventsJ = (JSONArray)cmd.get("events");	  
		  for (Object o : eventsJ) {
			  JSONObject jo = (JSONObject)o;
			  String event = jo.get("event")+"";
			  
			  if (event.equals(EventUnitMove.EVENT)) {
				  eventUnitMoveList.add(new EventUnitMove(Integer.parseInt(jo.get("unitId")+""), Integer.parseInt(jo.get("toX")+""), Integer.parseInt(jo.get("toY")+"")));
			  } else
				  
		      if (event.equals(EventUnitShow.EVENT)) {
		    	  eventUnitShowList.add(new EventUnitShow(Integer.parseInt(jo.get("unitId")+""), Integer.parseInt(jo.get("side")+""), jo.get("type")+"",  
						                      Integer.parseInt(jo.get("x")+""), Integer.parseInt(jo.get("y")+""), 
						                      Boolean.parseBoolean(jo.get("isArmed")+""), Boolean.parseBoolean(jo.get("isMobile")+"")));	      
		      } else
		    	  
		      if (event.equals(EventUnitHide.EVENT)) {
		    	  eventUnitHideList.add(new EventUnitHide(Integer.parseInt(jo.get("unitId")+"")));
		      } else
		    	  
			  if (event.equals(EventUnitFire.EVENT)) {
				  eventUnitFireList.add(new EventUnitFire(Integer.parseInt(jo.get("unitId")+"")));			  
			  } else
		    	  
		      if (event.equals(EventBoom.EVENT)) {	    	  
		    	  eventBoomList.add(new EventBoom(Integer.parseInt(jo.get("x")+""), Integer.parseInt(jo.get("y")+"")));			  
			  } else
				  
			  if (event.equals(EventUnitRotateTurret.EVENT)) {	    	  
				  eventUnitRotateTurrerList.add(new EventUnitRotateTurret(Integer.parseInt(jo.get("unitId")+""), Integer.parseInt(jo.get("turretLook")+"")));			  
			  } else
				  
			  if (event.equals(EventUnitHit.EVENT)) {	    	  
				  eventUnitHitList.add(new EventUnitHit(Integer.parseInt(jo.get("unitId")+""), Boolean.parseBoolean(jo.get("isArmed")+""), Boolean.parseBoolean(jo.get("isMobile")+""), Boolean.parseBoolean(jo.get("isAlive")+"")));			  
			  } else
				  
			  if (event.equals(EventFlag.EVENT)) {	    	  
				  eventFlagList.add(new EventFlag(Integer.parseInt(jo.get("id")+""), Integer.parseInt(jo.get("side")+""), Integer.parseInt(jo.get("state")+"")));			  
			  } else
			  
			  if (event.equals(EventCoins.EVENT)) {	    	  
				  eventCoinsList.add(new EventCoins(Integer.parseInt(jo.get("coins")+"")));			  
			  } else
				  
			  if (event.equals(EventUnitBuild.EVENT)) {	    	  
				  eventUnitBuildList.add(new EventUnitBuild(Integer.parseInt(jo.get("unitId")+""), jo.get("type")+"", Integer.parseInt(jo.get("x")+""), Integer.parseInt(jo.get("y")+"")));			  
			  } 
		  }
		  	  	  
		  _sides.get(side).processTick(new WorldTick(side, eventBoomList, eventCoinsList, eventFlagList, eventUnitBuildList,
				  										eventUnitFireList, eventUnitHideList, eventUnitHitList, eventUnitMoveList,
				  										eventUnitRotateTurrerList, eventUnitShowList));
		  
		  sendReady(side);		  
	}

	public void process_finish(JSONObject cmd) {
		System.out.println("=== Simulation finished");	  
		System.out.println("=== Check log at "+Combat.LOG_URL+_combat.getLog().getFileName());	  
	}	
}
