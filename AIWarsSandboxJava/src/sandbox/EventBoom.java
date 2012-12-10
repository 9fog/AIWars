package sandbox;

public class EventBoom {
	public static String EVENT = "Boom";
				
	private final int x;
	private final int y;
	
	public EventBoom(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}		
}
