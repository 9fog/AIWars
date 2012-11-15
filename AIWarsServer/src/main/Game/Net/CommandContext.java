package main.Game.Net;

import main.Game.Player;
import main.Game.PlayersList;
import net.minidev.json.JSONObject;
import org.jboss.netty.channel.Channel;

public class CommandContext {
	public final Channel channel;
	public final JSONObject cmd;
	//public final Player player;
	
	public CommandContext(Channel channel, JSONObject cmd) {
		this.channel = channel;
		this.cmd = cmd;
		
		//player = PlayersList.getInstance().getPlayer(channel.getId());
	}
}
