package main.Game.DataTables;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import main.Game.CombatData.UnitType;

import core.*;

/**
 * Desc: Game Proto : Server : 
 * Coder: elGringo
 * Date: 21.09.2010 11:47:15
 * Copyright elGringo (c) 2010
 */
public class UnitTypesTable {
    private static UnitTypesTable _instance;
	private final HashMap<Integer, UnitType> _data;

	public UnitTypesTable() {
		_data = new HashMap<Integer, UnitType>();
		
		_data.put(1, new UnitType(1,"recon",750,300,750,1,5,12,0,6,25,1));
		_data.put(2, new UnitType(2,"attack",1250,600,2000,1,10,7,0,10,70,3));
		_data.put(3, new UnitType(3,"artillery",2250,1500,4000,2,25,5,4,20,50,2));
		
		_data.put(4, new UnitType(4,"base",0,0,1500,1,7,7,0,9,150,0));
		
		Utils.log("   UnitsTable: "+_data.size()+" items loaded");
	}
	
	public static UnitTypesTable getInstance() {
		if (_instance == null) {
			_instance = new UnitTypesTable();
		}
		return _instance;
	}
	
	public UnitType getType(int id) {
		if (_data.containsKey(id)) {
			return _data.get(id);
		}else {			
			return null;
		}
	}
	
	public HashMap<Integer, UnitType> getAll(){
		return _data;
	}
}
