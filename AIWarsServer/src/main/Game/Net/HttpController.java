package main.Game.Net;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;


import main.Game.PlayersList;
import main.Game.Simulator.CombatSimulator;

// Usage: 
// http://IP:8765/control?cmd=newCombat&bot0=MyBot1&bot1=MyBot1&maxTicks=5&mapName=map1.map
public class HttpController implements HttpHandler {
	private final static String CMD_NEWCOMBAT = "newCombat"; 

	@Override
    public void handle(HttpExchange t) throws IOException {
        String[] queryStr = t.getRequestURI().getQuery().split("&");
        HashMap<String, String> query = new HashMap<String, String>();
        for (String var : queryStr) {
        	String[] v = var.split("=");
        	if (v.length==2) {
        		query.put(v[0], v[1]);
        	}
        }
        
        String cmd = query.get("cmd");
        String response = "";
        
        if (cmd.equalsIgnoreCase(CMD_NEWCOMBAT)) {
        	ArrayList<String> botNames = new ArrayList<String>();
        	for (int i=0; i<=3; i++) {
        		if (query.containsKey("bot"+i)) {
        			botNames.add(query.get("bot"+i));
        		}
        	}
        	int maxTicks = 100;
    		if (query.containsKey("maxTicks")) {
    			maxTicks = Integer.parseInt(query.get("maxTicks"));
    		}
    		String mapName = "";
    		if (query.containsKey("mapName")) {
    			mapName = query.get("mapName");
    		}
    		
        	CombatSimulator cs = new CombatSimulator();
        	cs.startCombat(botNames, maxTicks, mapName);
        	
        	response = cs.getLogUrl();
        }        	
        
    	t.getResponseHeaders().set("content-type", "text; charset=utf-8");    	
        t.sendResponseHeaders(200, response.length());
    	
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.flush();
        os.close();   
        t.close();        
    }
}
