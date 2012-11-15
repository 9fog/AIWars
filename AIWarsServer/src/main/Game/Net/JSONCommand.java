package main.Game.Net;

import net.minidev.json.JSONObject;

public class JSONCommand extends JSONObject {
	private static final long serialVersionUID = 1L;

	public JSONCommand(String status) {
		super();
		
		//put("_op", cmd.get("_op"));				
		put("status", status);
	}

}
