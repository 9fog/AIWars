package main.Stats;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import core.Sql;
import core.Utils;

public class StatVars {
	private static String _table = "stat_vars";
	private static StatVars _instance;
	
	private HashMap<String, StatVar> _vars = new HashMap<String, StatVar>(); 
	private String _upDate = "";
	
	private boolean _isBusy = false;
	
	
	public static StatVars getInstance() {
		if (_instance == null) {
			_instance = new StatVars();
		}
		return _instance;
	}
	
	
	public void addValue(String varName, double val) {
		if (_isBusy) return;
		
		String date = Utils.getDate(0);
		
		if (!date.equalsIgnoreCase(_upDate)) {
			log("Starting to write stats...");
			_isBusy = true;
			saveVars();
			_isBusy = false;
			log("Finishing to write stats...");
		}
		
		StatVar v;
		if (!_vars.containsKey(varName)) {
			v = new StatVar(varName);
			synchronized (_vars) {
				_vars.put(varName, v);
			}
		}else {
			v = _vars.get(varName);			
		}
		
		synchronized (v) {
			v.addValue(val);
		}
		
		//saveVars(); //DEBUG
		//log("Updating '"+varName+"'="+val+"; Sum="+v.getSum()+"; Avg="+v.getAvg()+"; Cnt="+v.getCount());
	}
		
	
	public void saveVars() {		
		if (_upDate=="") {
			_upDate = Utils.getDate(0);
			//loadVars();
			return;
		}
		/*
		try {
			Statement stmt = Sql.getInstance().newStatement();
			stmt.executeUpdate("DELETE FROM "+_table+" WHERE postdate='"+_upDate+"'");
			stmt.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
		Connection c = null;
		Statement stmt = null;
		try {
			c = Sql.getInstance().getPrivilegedConnection();
			stmt = c.createStatement();
			for (StatVar v : _vars.values()) {
			//log("Saving var "+v.getName());
				//Statement stmt = Sql.getInstance().newStatement();
				try {
					ResultSet rs = stmt.executeQuery("SELECT * FROM "+_table+" WHERE postdate='"+_upDate+"' AND varName='"+v.getName()+"'");
					if (rs.next()) {
						double min = (rs.getDouble("min")>v.getMin())?v.getMin():rs.getDouble("min");
						double max = (rs.getDouble("max")<v.getMax())?v.getMax():rs.getDouble("max");
						double avg = (v.getSum()+rs.getDouble("sum"))/(v.getCount()+rs.getLong("count"));
						stmt.executeUpdate("UPDATE "+_table+" SET sum=sum+"+v.getSum()+", count=count+"+v.getCount()+", min="+min+", max="+max+", avg="+avg+" WHERE postdate='"+_upDate+"' AND varName='"+v.getName()+"'");
					} else {
						stmt.executeUpdate("INSERT INTO "+_table+" SET postdate='"+_upDate+"', " +
								"varName='"+v.getName()+"', sum='"+v.getSum()+"', count='"+v.getCount()+"', avg='"+v.getAvg()+"', min='"+v.getMin()+"', max='"+v.getMax()+"'");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//stmt.close();
			}
			stmt.close();
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if ((stmt!=null)&&(!stmt.isClosed())) {
					stmt.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				if ((c!=null)&&(!c.isClosed())) {
					c.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		
		_upDate = Utils.getDate(0);
		synchronized (_vars) {
			_vars = new HashMap<String, StatVar>();
		}
	}
		
	/*
	public void loadVars() {				
		_vars = new HashMap<String, StatVar>();
		
		try {
			Statement stmt = Sql.getInstance().newStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM "+_table+" WHERE postdate='"+_upDate+"'"); //, " +;
					//"varName='"+v.getName()+"', sum='"+v.getSum()+"', count='"+v.getCount()+"', avg='"+v.getAvg()+"', max='"+v.getMax()+"'");
			
			while (rs.next()) {
				StatVar v = new StatVar(rs.getString("varName"), rs.getDouble("sum"), rs.getDouble("avg"), rs.getDouble("min"), rs.getDouble("max"), rs.getInt("count"));
				_vars.put(v.getName(), v);
			}
			
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		log(_vars.size()+" variables restored");
	}
	*/	
	
    public void log(String txt){
    	Utils.log("   StatVars : "+txt);    	
    }		
}
