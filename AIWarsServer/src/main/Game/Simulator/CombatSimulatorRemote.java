package main.Game.Simulator;

import java.util.ArrayList;

import main.Game.Combat;
import main.Game.Main;
import net.minidev.json.JSONObject;

import org.jboss.netty.channel.Channel;

import core.Utils;

public class CombatSimulatorRemote extends CombatSimulator {
	private Main _parent;
	private Channel _channel;

	public CombatSimulatorRemote(Main parent, Channel ch, ArrayList<String> botNames, int maxTicks, String mapName) {
		_parent = parent;
		_channel = ch;
		
		try {
			_combat = new Combat(this, botNames, maxTicks, mapName);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		_combat.startCombat();
	}

	public void send(JSONObject cmd) {
		Utils.log("SRV > "+cmd.toJSONString());
		_channel.write(cmd.toJSONString()+"\0");	
	}
	
	public Combat getCombat() {
		return _combat;
	}
	
	@Override
	public void reportFinish() {
		_parent.reportFinish(this);
	}		
	
	public void stopCombat() {
		_combat.stop();
	}
}
