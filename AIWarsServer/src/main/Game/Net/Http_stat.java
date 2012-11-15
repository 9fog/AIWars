package main.Game.Net;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import main.Game.PlayersList;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Http_stat implements HttpHandler {
	private final static String STAT_ONLINE = "online"; 
	private final static String STAT_THREADS = "threads";
	
	/*
	private Main _combat;
	
	public void setCombat(Main combat) {
		_combat = combat;
	}
	*/
	
    public void handle(HttpExchange t) throws IOException {
        String[] queryStr = t.getRequestURI().getQuery().split("&");
        HashMap<String, String> query = new HashMap<String, String>();
        for (String var : queryStr) {
        	String[] v = var.split("=");
        	if (v.length==2) {
        		query.put(v[0], v[1]);
        	}
        }
        
        String stat = query.get("stat");
        String response = "";
        
        if (stat.equalsIgnoreCase(STAT_ONLINE)) {
        	response = PlayersList.getInstance().getCount()+"";
        } else
        	
        if (stat.equalsIgnoreCase(STAT_THREADS)) {
        	//response = ((Main)combatServer.getInstance().getCombat("PiratQuest.Main")).getThreadsCount()+"";
        	response = Thread.activeCount()+"";
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
