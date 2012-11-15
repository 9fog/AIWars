package main.Game.CombatData.CombatMap;

public class UnitPointer extends ObjectPointer{
	public final int id;
	public final int typeId;
	public final int side;
	public final int x, y;
	
	public UnitPointer(int id, int typeId, int side, int x, int y) {
		this.id = id;
		this.typeId = typeId;
		this.side = side;
		this.x = x;
		this.y = y;
	}
}
