package main.Game;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

import org.jboss.netty.channel.Channel;

import com.sun.net.httpserver.HttpServer;

import main.Game.DataTables.UnitTypesTable;
import main.Game.Net.CommandContext;
import main.Game.Net.HttpController;
import main.Game.Net.JSONCommand;
import main.Game.Simulator.CombatSimulatorRemote;
import main.Stats.StatVars;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import core.TCombat;
import core.Utils;
import core.attributeException;
import core.NettyServer.NetContext;

public class Main extends TCombat {
	private HashMap<Integer, CombatSimulatorRemote> _combatsList;

	public Main(String pack, String cType) {
		super(pack, cType);
		
		//Config.getInstance();
		UnitTypesTable.getInstance();
		
		PlayersList.getInstance();
		
		_combatsList = new HashMap<Integer, CombatSimulatorRemote>();
							
		Thread _shotDownHook = new Thread() {
            public void run() {
            	log("ShotDown hook!");            	
            	//PlayersList.getInstance().saveAll();
            	StatVars.getInstance().saveVars();            	
            	log("Shoting down done!");
            }
        };
        _shotDownHook.setName("shotDownHook");		
        Runtime.getRuntime().addShutdownHook(_shotDownHook);
        
        try {
        	int port = 8765;            	
        	HttpServer server = HttpServer.create(new InetSocketAddress(port), port);
        	HttpController http = new HttpController();
        	server.createContext("/control", http);
        	server.setExecutor(null); // creates a default executor
        	server.start();
        	Utils.log("HttpController started on "+port);
        }catch (Exception e){
        	e.printStackTrace();
        }                 
	}	
	
	@Override 
	public void onDisconnect(Object ch) {
		//Player p = PlayersList.getInstance().getPlayer(((Channel)ch).getId());
		//p.storeDB();
		//PlayersList.getInstance().removePlayer(p.getUid());
		int id = ((Channel)ch).getId();
		if (_combatsList.containsKey(id)) {
			CombatSimulatorRemote c = _combatsList.get(id);
			c.stopCombat();
		}
		_combatsList.remove(id);
	}
	
	public void reportFinish(CombatSimulatorRemote c) {
		_combatsList.remove(c);
		
		log("combatsList.size="+_combatsList.size());
	}
	
	
	@Override
	public String processCommand(Object context) {
		NetContext nCtx = (NetContext)context;

		CommandContext cCtx;
		
		JSONObject cmd;
		try {
	        Object obj=JSONValue.parse(nCtx.message);
	        cmd=(JSONObject)obj;
	        cCtx = new CommandContext(nCtx.channel, cmd);
		} catch (Exception e) { //Иногда запрос полиси приходит сюда. Видно полиси-сервер не справляется с нагрузкой			
			if (nCtx.message.equalsIgnoreCase("<policy-file-request/>")) {
				Utils.log("Policy request");
				String mess="<cross-domain-policy>" +
                	"<allow-access-from domain=\"*\" to-ports=\"*\" />" +
                	"</cross-domain-policy>";
				return mess;
			} else {			
				e.printStackTrace();
				Utils.log("Error 000");
				Utils.log("Error 000. cmd = "+nCtx.message);
				return new JSONCommand(e.getMessage()).toJSONString();				
			}
		}		
		
		/*
		try {
			if ((!(cmd.get("_op")+"").equalsIgnoreCase("testFight"))&&(cCtx.player==null)) {
				return new JSONCommand(cmd, "User is not authorized").toJSONString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new JSONCommand(cmd, "Unknown authorization error. "+e.getMessage()).toJSONString();
		}	
		*/	
		
	    Method method;		
		try {
			method = getClass().getMethod("process_"+cmd.get("_op"), CommandContext.class);
			JSONObject res = (JSONObject)method.invoke(this, cCtx); 
			if (res!=null) {
				return res.toJSONString();
			} else {
				return "";
			}
		} catch (InvocationTargetException e) {
			Utils.log("Error 002");
			e.printStackTrace();
			e.getTargetException().printStackTrace();
			if (e.getTargetException() instanceof attributeException) {
				return new JSONCommand(e.getTargetException().getMessage()).toJSONString();
			} else
			if (e.getTargetException() instanceof NullPointerException) { 			
				return new JSONCommand(e.getTargetException().toString()).toJSONString();				
			} else {				
				return new JSONCommand(e.toString()).toJSONString();
			}
		} catch (Exception e) {
			Utils.log("Error 003");
			e.printStackTrace();
			return new JSONCommand(e.toString()).toJSONString();
		}		
	}
	
	
	public JSONObject process_testFight(CommandContext ctx) throws Exception {
		ArrayList<String> botNames = new ArrayList<String>();
		JSONArray namesArray = (JSONArray)ctx.cmd.get("botNames");
		for (Object jo : namesArray) {
			botNames.add(jo+"");
		}
		
		CombatSimulatorRemote csr = new CombatSimulatorRemote(this, ctx.channel, botNames, Integer.parseInt(ctx.cmd.get("maxTicks")+""), ctx.cmd.get("mapName")+"");
		//Combat c = new Combat(this, ctx.channel, botNames, Integer.parseInt(ctx.cmd.get("maxTicks")+""), ctx.cmd.get("mapName")+"");	
		_combatsList.put(ctx.channel.getId(), csr);
		
		return null;
	}

	public JSONObject process_Ready(CommandContext ctx) throws Exception {
		_combatsList.get(ctx.channel.getId()).getCombat().processReady(Integer.parseInt(ctx.cmd.get("_side")+""));
		return null;
	}	
	
	public JSONObject process_orderMove(CommandContext ctx) throws Exception {
		_combatsList.get(ctx.channel.getId()).getCombat().processOrderMove(Integer.parseInt(ctx.cmd.get("_side")+""), Integer.parseInt(ctx.cmd.get("unitId")+""), Integer.parseInt(ctx.cmd.get("toX")+""), Integer.parseInt(ctx.cmd.get("toY")+""));
		return null;
	}	
	
	public JSONObject process_orderAttack(CommandContext ctx) throws Exception {
		_combatsList.get(ctx.channel.getId()).getCombat().processOrderAttack(Integer.parseInt(ctx.cmd.get("_side")+""), Integer.parseInt(ctx.cmd.get("unitId")+""), Integer.parseInt(ctx.cmd.get("targetId")+""));
		return null;
	}		
	
	public JSONObject process_orderBuild(CommandContext ctx) throws Exception {
		_combatsList.get(ctx.channel.getId()).getCombat().processOrderBuild(Integer.parseInt(ctx.cmd.get("_side")+""), ctx.cmd.get("unitType")+"");
		return null;
	}	
	
	//======================== CHEATS ===========================================================

	
	//==========================================================================================	
}
