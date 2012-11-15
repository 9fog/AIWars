package main.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import main.Game.DataTables.Config;
import net.minidev.json.JSONArray;

import org.jboss.netty.channel.Channel;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import core.Utils;
import core.coreConfig;
import core.myMongo;

public class Player {
	private final String _id;
	private Channel _ch;	
	private String _sessionKey; 
	
	//Сложные параметры игрока ===

	
	//Простые параметры игрока ===
			
	
	private boolean _debug = coreConfig.getInstance().get("debug").equalsIgnoreCase("1");

	
	public Player(String uid, Channel ch, String sessionKey) {
		_id = uid;
		_ch = ch;
		_sessionKey = sessionKey;
		
		long now = Utils.getTimeStamp(); 
		
		/*
 	    try {
 	    	if (dbObject==null) { //Создаем нового

				
 	    		storeDB(true);
 	    	} else { //Юзаем уже созданного
    		
 	    	} 	    	
        } catch (Exception e) {
        	e.printStackTrace();	        	
        }
        */
	}
	
	public String getUid() { return _id;}	
	public Channel getChannel() { return _ch;}
	public String getSessionKey() { return _sessionKey;}
	
	
	public void storeDB() {
		storeDB(false);
	}
	
	
	private void storeDB(boolean isNew) {
	}
	
	
	public void vipe() {
	}
	
	
	public void send(String msg) {
		_ch.write(msg);		
		if (_debug) {
			Utils.log("SRV ("+_ch.getId()+")> "+msg);				
		}
	}
}
