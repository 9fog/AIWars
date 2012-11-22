package Sandbox;

public class EventFlag {
	public static String EVENT = "Flag";
	
	public final int flagId;
	public final int ownerSide;
	public final int flagState;
	
	public EventFlag(int flagId, int ownerSide, int flagState) {
		this.flagId = flagId;
		this.ownerSide = ownerSide;
		this.flagState = flagState;
	} 		
}
