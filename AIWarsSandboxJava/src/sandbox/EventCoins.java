package sandbox;

public class EventCoins {
	public static String EVENT = "Coins";
	
	private final int coins;

	public EventCoins(int coins) {
		this.coins = coins;
	}

	public int getCoins() {
		return coins;
	}		
}
