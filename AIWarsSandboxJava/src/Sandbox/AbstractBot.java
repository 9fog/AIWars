package Sandbox;

import java.util.ArrayList;

public abstract class AbstractBot {
	public abstract void processInit(Simulator sim);
	public abstract void processTick(ArrayList<Object> events);
}