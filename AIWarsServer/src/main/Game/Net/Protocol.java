package main.Game.Net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import main.Game.CombatData.Unit;


public final class Protocol {
	
	public static String snd_Sys_Hi(String uid) {
		return "<hi/>";
	}
	
	
	/**
	 * Юзер вошел в бой
	 */
	/*
	public static String snd_Combat_StartInfo(int side, CombatMap map, HashMap<Integer, Unit> squad, ArrayList<Flag> flags) {
		Document c = DocumentHelper.createDocument();
		Element e = c.addElement("combat")
		             .addElement("startinfo").addAttribute("side", side + "");
		
		e.addElement("map")
			.addAttribute("sizex", map.getSizeX() + "")
			.addAttribute("sizey", map.getSizeY() + "")
			.addAttribute("dump", map.getDump());
		
		Element eu = e.addElement("units");
		for (Unit u : squad.values()) {
			eu.addElement("unit")
				.addAttribute("id", u.getId() + "")
				.addAttribute("type", u.getType().id() + "")
				.addAttribute("x", u.getX() + "")
				.addAttribute("y", u.getY() + "")
				.addAttribute("look_range", u.getLookRange() + "")
				.addAttribute("fire_range", u.getShotRangeMax() + "")
				;
		}
		
		eu = e.addElement("flags");
		for (Flag f : flags) {
			eu.addElement("flag")
				.addAttribute("id", f.getId() + "")
				.addAttribute("x", f.getX() + "")
				.addAttribute("y", f.getY() + "");
		}
		
		return c.asXML();
	}
	*/	
		
	/**
	 * Бой начался
	 */
	public static String snd_Combat_Start() {
		return "<start/>";
	}
	
	/**
	 * Бой закончился
	 */
	public static String snd_Combat_Final(int winside) {
		return "<final winside=\""+winside+"\"/>";
	}
	
	/**
	 * Юнит движется
	 */
	public static String snd_Combat_UnitMoving(int unitId, Integer toX, Integer toY, int gear_look, long time) {
		return "<M id=\""+unitId+"\" tx=\""+toX+"\" ty=\""+toY+"\" gl=\""+gear_look+"\" tm=\""+time+"\"/>";
	}
	
	/**
	 * Показать юнит противника
	 */	
	public static String snd_Combat_ShowUnit(Unit u) {
		/*
		Document c = DocumentHelper.createDocument();
		c.addElement("combat").addElement("showunit")
			.addAttribute("id", u.getId() + "")
			.addAttribute("type", u.getType().id() + "")			
			.addAttribute("x", u.getX() + "")
			.addAttribute("y", u.getY() + "")
			.addAttribute("armed", u.isArmed() + "")
			.addAttribute("mobile", u.isMobile() + "")
			.addAttribute("alive", u.isAlive() + "")
			.addAttribute("gear_look", u.getGearLook() + "")
			.addAttribute("turret_look", u.getTurretLook() + "");
		return c.asXML();
		*/
		return "";
	}
	
	/**
	 * Показать юнит противника
	 */	
	public static String snd_Combat_HideUnit(Unit u) {
		return "<HU id=\""+u.getId()+"\"/>";		
	}	
	
	/**
	 * Юнит повернул башню
	 */
	public static String snd_Combat_UnitRotateTurret(Unit u) {
		return "<RT id=\""+u.getId()+"\" tl=\""+u.getTurretLook()+"\"/>";				
	}
	
	/**
	 * Юнит выстрелил
	 */
	public static String snd_Combat_UnitFire(Unit u, int x, int y, int flyTime) {
		return "<F id=\""+u.getId()+"\" tx=\""+x+"\" ty=\""+y+"\" ft=\""+flyTime+"\"/>";				
	}	
	
	/**
	 * Взрыв
	 */	
	public static String snd_Combat_Boom(int x, int y, int power, ArrayList<HashMap<String, String>> victims) {
		/*
		Document c = DocumentHelper.createDocument();
		Element e = c.addElement("combat").addElement("boom")
						.addAttribute("x", x + "")
						.addAttribute("y", y + "")
						.addAttribute("power", power + "")
						;
		if (victims.size()>0) {
			Element e2 = e.addElement("victims");
			for (HashMap<String, String> vic : victims) {
				Element e3 = e2.addElement("victim"); 
				for (Entry<String, String> data : vic.entrySet()) {
					e3.addAttribute(data.getKey(), data.getValue());
				}
			}
		}
		return c.asXML();
		*/
		return "";
	}	
	
	/**
	 * Изменение состояния флага
	 */
	public static String snd_Combat_Flag(int id, int side, int state) {
		return "<FS id=\""+id+"\" sd=\""+side+"\" st=\""+state+"\"/>";				
	}	
}
