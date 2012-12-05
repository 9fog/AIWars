package main.Game.DataTables;

import java.sql.ResultSet;
import java.sql.Statement;

import main.Game.BotData;

import core.Sql;

public class UsersTable {
	private static final String _name = "users";
	
	public static BotData getBotData(String uid) throws Exception{
		BotData res = null;
		
		Statement st = Sql.getInstance().newStatement();
		ResultSet rs = st.executeQuery("SELECT * FROM "+_name+" WHERE id='"+uid+"'");
		
		if (rs.next()) {
			res = new BotData(uid, rs.getString("uname"), rs.getString("jarName"));
		}
		
		rs.close();
		st.close();
		
		return res;
	}
}
