package sandbox;

public class EventFlag {
	public static String EVENT = "Flag";
	
	private final int flagId;
	private final int ownerSide;
	private final int flagState;
	
	public EventFlag(int flagId, int ownerSide, int flagState) {
		this.flagId = flagId;
		this.ownerSide = ownerSide;
		this.flagState = flagState;
	}

	public int getFlagId() {
		return flagId;
	}

	public int getOwnerSide() {
		return ownerSide;
	}

	public int getFlagState() {
		return flagState;
	} 		
}
