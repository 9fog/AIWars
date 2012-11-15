package main.Game;

import java.util.HashMap;

import core.Utils;

public class PlayersList {
	private static PlayersList _instance;
		
	private HashMap<String, Player> _uidMap;	
	private HashMap<Integer, Player> _channelIdMap;
	
	
	public PlayersList() {
		_uidMap = new HashMap<String, Player>();	
		_channelIdMap = new HashMap<Integer, Player>();
	}
	
	public static PlayersList getInstance() {
		if (_instance == null) {
			_instance = new PlayersList();
		}
		
		return _instance;
	}
	
	
	public void addPlayer(Player p) {
		_uidMap.put(p.getUid(), p);
		_channelIdMap.put(p.getChannel().getId(), p);
		
		log(getCount()+" players online now");
	}	
	public void removePlayer(String uid) {
		//if (_list.containsKey(uid)) {
		_uidMap.remove(uid);
		//log(getCount()+" players online now");
		//}
	}	
	public Player getPlayer(String uid) { return _uidMap.get(uid); }	
	public boolean hasPlayer(String uid) { return _uidMap.containsKey(uid); }
	
	/*
	public void addPlayer(int cid, Player p) {
		_channelIdMap.put(cid, p);
		//log(getCount()+" players online now");
	}
	*/	
	public void removePlayer(int cid) {
		//if (_list.containsKey(uid)) {
		Player p = getPlayer(cid);
		_channelIdMap.remove(cid);
		if (p!=null) {
			removePlayer(p.getUid());
		}
		log(getCount()+" players online now");
		//}
	}	
	public Player getPlayer(int cid) { return _channelIdMap.get(cid); }	
	//public boolean hasPlayer(int cid) { return _channelIdMap.containsKey(cid); }
	
	
	public int getCount() {return _uidMap.size();}		
	
	private void log(String txt) {
		Utils.log("   PlayersList: "+txt);
	}
}
