package Sandbox;

import java.util.ArrayList;

public abstract class AbstractBot {
	public abstract void processInit(Simulator sim, 
			                int mapSizeX, int mapSizeY, 
			                ArrayList<ObjectRock> rocks, ArrayList<ObjectFlag> flags, ArrayList<ObjectUnit> units);
	public abstract void processTick(ArrayList<Object> events);
}