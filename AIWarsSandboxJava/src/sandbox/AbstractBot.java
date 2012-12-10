package sandbox;

import java.util.ArrayList;

public abstract class AbstractBot {
	public String name;
	
	public void setName(String name) {
		this.name = name;
	}
	
    public abstract void processInit(Simulator sim,
    								 int mySide,
                                     int mapSizeX,
                                     int mapSizeY,
                                     ArrayList<ObjectRock> rocks,
                                     ArrayList<ObjectFlag> flags,
                                     ArrayList<ObjectUnit> units);

    public abstract void processTick(WorldTick worldObject);
}