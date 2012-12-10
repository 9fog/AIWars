package sandbox;

/**
 * Класс содержит в себе все события очередного тика боя  
 * 
 * @author 9fog
 *
 */

import java.util.ArrayList;

public class WorldTick {
	private int side;
	
	//Взрывы
	private ArrayList<EventBoom> eventBoomList;
	
	//Изменения количества монет
	private ArrayList<EventCoins> eventCoinsList;
	
	//Изменение состояний флагов
	private ArrayList<EventFlag> eventFlagList;
	
	//Создание (производство) новых юнитов
	private ArrayList<EventUnitBuild> eventUnitBuildList;	

	//Выстрелы
	private ArrayList<EventUnitFire> eventUnitFireList;	
	
	//Уход вражеских юнитов из поля зрения
	private ArrayList<EventUnitHide> eventUnitHideList;	
	
	//Попадания по юнитам (нанесение повреждений)
	private ArrayList<EventUnitHit> eventUnitHitList;
	
	//Перемешение юнитов
	private ArrayList<EventUnitMove> eventUnitMoveList;
	
	//Вращение башен юнитов
	private ArrayList<EventUnitRotateTurret> eventUnitRotateTurrerList;
	
	//Появление новых вражеских юнитов в поле зрения
	private ArrayList<EventUnitShow> eventUnitShowList;
	
	
	public WorldTick(int side, ArrayList<EventBoom> eventBoomList,
			ArrayList<EventCoins> eventCoinsList,
			ArrayList<EventFlag> eventFlagList,
			ArrayList<EventUnitBuild> eventUnitBuildList,
			ArrayList<EventUnitFire> eventUnitFireList,
			ArrayList<EventUnitHide> eventUnitHideList,
			ArrayList<EventUnitHit> eventUnitHitList,
			ArrayList<EventUnitMove> eventUnitMoveList,
			ArrayList<EventUnitRotateTurret> eventUnitRotateTurrerList,
			ArrayList<EventUnitShow> eventUnitShowList) {
		this.side = side;
		this.eventBoomList = eventBoomList;
		this.eventCoinsList = eventCoinsList;
		this.eventFlagList = eventFlagList;
		this.eventUnitBuildList = eventUnitBuildList;
		this.eventUnitFireList = eventUnitFireList;
		this.eventUnitHideList = eventUnitHideList;
		this.eventUnitHitList = eventUnitHitList;
		this.eventUnitMoveList = eventUnitMoveList;
		this.eventUnitRotateTurrerList = eventUnitRotateTurrerList;
		this.eventUnitShowList = eventUnitShowList;
	}

	
	public int getSide() {
		return side;
	}
	
	
	public ArrayList<EventBoom> getEventBoomList() {
		return eventBoomList;
	}


	public ArrayList<EventCoins> getEventCoinsList() {
		return eventCoinsList;
	}


	public ArrayList<EventFlag> getEventFlagList() {
		return eventFlagList;
	}


	public ArrayList<EventUnitBuild> getEventUnitBuildList() {
		return eventUnitBuildList;
	}


	public ArrayList<EventUnitFire> getEventUnitFireList() {
		return eventUnitFireList;
	}


	public ArrayList<EventUnitHide> getEventUnitHideList() {
		return eventUnitHideList;
	}


	public ArrayList<EventUnitHit> getEventUnitHitList() {
		return eventUnitHitList;
	}


	public ArrayList<EventUnitMove> getEventUnitMoveList() {
		return eventUnitMoveList;
	}


	public ArrayList<EventUnitRotateTurret> getEventUnitRotateTurrerList() {
		return eventUnitRotateTurrerList;
	}


	public ArrayList<EventUnitShow> getEventUnitShowList() {
		return eventUnitShowList;
	}
}
