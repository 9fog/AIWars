package main.Game.DataTables;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import core.*;
/**
 * Desc: Game Proto : Server : 
 * Coder: elGringo
 * Date: 21.09.2010 11:25:44
 * Copyright elGringo (c) 2010
 */
public class Config {
	private static String _table = "config_vars";
    private static Config _instance;
	private HashMap<String, String> _data;
	
	public Config() {
		_data = new HashMap<String, String>();
				
		Statement stmt = Sql.getInstance().newStatement();
		try {
			ResultSet rs = stmt.executeQuery("SELECT var, val FROM "+_table+"");
			while (rs.next()) {
				_data.put(rs.getString("var"), rs.getString("val"));
			}			
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		Utils.log("   Config: "+_data.size()+" vars loaded");		
	}
	
	public static Config getInstance() {
		if (_instance == null) {
			_instance = new Config();
		}
		return _instance;
	}
	
	public double getVar(String var) {
		if (_data.containsKey(var)) {
			return Double.parseDouble(_data.get(var));
		}else {			
			return 0.0;
		}
	}
	
	public Integer getVarInt(String var) {
		if (_data.containsKey(var)) {
			return Integer.parseInt(_data.get(var));
		}else {			
			return 0;
		}
	}
	
	public String getVarString(String var) {
		if (_data.containsKey(var)) {
			return _data.get(var);
		}else {			
			return "";
		}
	}	
	
	public void setVarString(String key, String val) {
		_data.put(key, val);
	}
}
