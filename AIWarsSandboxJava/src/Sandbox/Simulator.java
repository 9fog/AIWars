package sandbox;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

public class Simulator
{
  private Connector _connector;
  protected ArrayList<AbstractBot> _sides;
  private String _logUrl;
  
  public Simulator() {}

  public Simulator(Connector c)
  {
    this._connector = c;

    this._sides = new ArrayList();

    for (String botName : Config.BOTS) {
      Connector.log("Creating bot [" + botName + "]");
      File jarFile = new File(botName+"/MyBot");
      try
      {
        URL jarURL = jarFile.toURI().toURL();
        URLClassLoader classLoader = new URLClassLoader(new URL[] { jarURL });

        Class pl = classLoader.loadClass(botName+".MyBot");
        AbstractBot b = (AbstractBot)pl.getConstructor(new Class[0]).newInstance(new Object[0]);

        this._sides.add(b);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public int getBotsCount() { return this._sides.size(); }
  
  public int getMySide(AbstractBot me) {return _sides.indexOf(me);}

  //==== Приказы от ботов =====================================================
  public void sendOrderMove(AbstractBot b, int unitId, int toX, int toY) {
	  int side = _sides.indexOf(b);
	  
	  JSONObject cmd = new JSONObject();
	  cmd.put("_op", "orderMove");
	  cmd.put("_side", side);
	  cmd.put("unitId", unitId);
	  cmd.put("toX", toX);
	  cmd.put("toY", toY);
	  
	  _connector.send(cmd.toJSONString());
  }
  
  public void sendOrderAttack(AbstractBot b, int unitId, int targetId) {
	  int side = _sides.indexOf(b);
	  
	  JSONObject cmd = new JSONObject();
	  cmd.put("_op", "orderAttack");
	  cmd.put("_side", side);
	  cmd.put("unitId", unitId);
	  cmd.put("targetId", targetId);
	  
	  _connector.send(cmd.toJSONString());
  }  
  
  public void sendOrderBuild(AbstractBot b, String type) {
	  int side = _sides.indexOf(b);
	  
	  JSONObject cmd = new JSONObject();
	  cmd.put("_op", "orderBuild");
	  cmd.put("_side", side);
	  cmd.put("unitType", type);
	  
	  _connector.send(cmd.toJSONString());	  
  }
  
  private void sendReady(int side) {
	  JSONObject cmd = new JSONObject();
	  cmd.put("_op", "Ready");
	  cmd.put("_side", side);
	  _connector.send(cmd.toJSONString());
  }  
  //==========================================================================
  

  public void processServerCommand(String msg) {
		JSONObject cmd;
		try {
	        cmd=(JSONObject)JSONValue.parse(msg);
	        
			try {
				Method method = getClass().getMethod("process_"+cmd.get("_op"), JSONObject.class);
				method.invoke(this, cmd); 
			} catch (Exception e) {
				e.printStackTrace();
			}			  
		} catch (Exception e) { //Иногда запрос полиси приходит сюда. Видно полиси-сервер не справляется с нагрузкой			
			e.printStackTrace();			
		}						
  }

  
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
	  
	  _logUrl = cmd.get("logUrl")+"";
	  
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
	  System.out.println("=== Check log at "+_logUrl);	  
  }
}
