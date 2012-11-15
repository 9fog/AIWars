package Sandbox;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import net.minidev.json.JSONObject;

public class Simulator
{
  private Connector _connector;
  private ArrayList<AbstractBot> _sides;

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
  //==========================================================================
  

  public void processServerCommand(String cmd) {
	  
  }

  private void process_init() {
  }
}
