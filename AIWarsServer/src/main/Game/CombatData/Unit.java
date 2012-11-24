package main.Game.CombatData;

import java.util.ArrayList;

import main.Game.Combat;
import main.Game.CombatData.Orders.Order;
import main.Game.CombatData.Orders.OrderIdle;
import main.Game.CombatData.Orders.OrderMove;

import core.*;

public class Unit extends MapObject {
	public static final String ROLE_RECON     = "recon";
	public static final String ROLE_ATTACK    = "attack";
	public static final String ROLE_ARTILLERY = "artillery";
	public static final String ROLE_BASE      = "base";
	
	private int _id, _side;
	private UnitType _type;
	private Combat _combat;
	private ArrayList<ArrayList<Unit>> _iSeeThem;
	private ArrayList<ArrayList<Unit>> _theySeeMe;	
	private ArrayList<Unit> _theyAllSeeMe;
	
	private Timer _reloadTimer = new Timer("", 0);  //Таймер перезарядки орудия
	
	private int _moving_speed = 250,    //Сколько милисекунд на одну клетку
	            _rotate_speed = 500,    //Сколько милисукунд на поворот башни и корпуса на 1 сектор
	            _fire_rate = 2000, 
	            _fire_disperce = 1, 
	            _fire_power = 10,
	            _look_range = 6, _look_range2,
	            _shot_range_min = 0, _shot_range_min2, 
	            _shot_range_max = 10, _shot_range_max2,	             
	            _hp, _maxHP = 50,
	            
	            _gear_look, _turret_look;
	
	private boolean _isArmed = true, _isMobile = true;
	
	
	private Order _order = null, _order2 = null, _order3 = null;
	
	public Unit(Combat c, int id, int x, int y, int side, UnitType type) {
		_combat = c;
		
		_id   = id;
		setXY(x, y);
		_side = side;
		_type = type;	
		
		_moving_speed    = type.moving_speed();
		_rotate_speed    = type.rotate_speed();
		_fire_rate       = type.fire_rate();
		_fire_disperce   = type.fire_disperce();
		_fire_power      = type.fire_power();
		_look_range      = type.look_range();		
		_look_range2     = type.look_range2();
		_shot_range_min  = type.shot_range_min();
		_shot_range_min2 = type.shot_range_min2();
		_shot_range_max  = type.shot_range_max();
		_shot_range_max2 = type.shot_range_max2();
		
		_maxHP = type.max_hp();
		_hp = type.max_hp();
		
		_iSeeThem = new ArrayList<ArrayList<Unit>>();		
		_theySeeMe = new ArrayList<ArrayList<Unit>>();
		_theyAllSeeMe = new ArrayList<Unit>();
		
		for (int i=0; i<_combat.getSidesCount(); i++) {
			_iSeeThem.add(new ArrayList<Unit>());
			_theySeeMe.add(new ArrayList<Unit>());
		}
		
		_order2 = new OrderIdle(this);
	}
	
	public int getId() {return _id;}
	public int getSide() {return _side;}
	public UnitType getType() {return _type;}
	public Combat getCombat() {return _combat;}
	
	public int getMovingSpeed() {return _moving_speed;}
	public int getRotateSpeed() {return _rotate_speed;}
		
	public void setOrder(Order ord) {_order = ord;}
	public Order getOrder() {return _order;}
	
	public void setOrder2(Order ord) {_order2 = ord;}
	public Order getOrder2() {return _order2;}
	
	public void setOrder3(Order ord) {_order3 = ord;}
	public Order getOrder3() {return _order3;}	
	
	public void setGearLook(int look) {_gear_look = look;}
	public int  getGearLook() {return _gear_look;}
	public void setTurretLook(int look) {
		_turret_look = look;
		if (_turret_look<0) {
			_turret_look += 8;
		}
		if (_turret_look>7) {
			_turret_look -= 8;
		}
	}
	public int  getTurretLook() {return _turret_look;}
	
	public int getLookRange() { return _look_range;}
	public int getLookRange2() { return _look_range2;}
	
	public int getShotRangeMin2() {return _shot_range_min2;}
	public int getShotRangeMax2() {return _shot_range_max2;}
	public int getShotRangeMax() {return _shot_range_max;}
	
	public int getFireRate() {return _fire_rate;}
	public int getFireDisperce() {return _fire_disperce;}
	public int getFirePower() {return _fire_power;}
	
	public Timer getReloadTimer() {return _reloadTimer;}
	
	public long getRange2(Unit anotherUnit) {
		return Utils.getRange2(getX(), getY(), anotherUnit.getX(), anotherUnit.getY());
	}
	public int getDirection(Unit anotherUnit) {
		int dir = 0;
		
		//TODO:: Надо бы тут сделать простой линейный расчет сектора.
		int angle = Utils.normalizeAngle(Utils.getDirection(getX(), getY(), anotherUnit.getX(), anotherUnit.getY())+90+23);
		dir = angle/45;		
		//log("angle="+angle+"; dir="+dir);
		
		return dir;
	}	
	
	public int getMaxHP() {return _maxHP;}
	public int getHP() {return _hp;}
	public void addHP(int plus) {
		if (_hp==0) return;
		
		_hp+=plus;
		if (_hp<0) {
			_hp = 0;
		}
		/*
		if (_hp==0) {
			_combat.notifyDeath(this);
		}
		*/
		if (_hp>_maxHP) {
			_hp = _maxHP;
		}
	}
	public void applyDamage(int damage) {
		addHP(-damage);
		
		//Сделаем, чтобы юнит сам пытался выйти из-под обстрела
		if ((getType().role()=="recon")||(getType().role()=="artillery")) {
			if (!(getOrder() instanceof OrderMove)&&(_hp<_maxHP*3/4)) {
				setOrder(new OrderMove(this, getX()-2+Utils.getRand(5), getY()-2+Utils.getRand(5)));			
			}
		}
		
		if ((_hp<_maxHP/2)&&(_hp>0)) {
			//Если повреждения уже больше половины - с заданной вероятностью поломаем либо гусеницы, либо пушку
			if (isArmed()&&isMobile()) {
				if (Utils.runProb(20)){
					if (Utils.runProb(50)) {
						_isArmed = false;
					} else {
						_isMobile = false;
					}
				}
			}
		} else 
		if (_hp==0) {
			_isArmed = false;
			_isMobile = false;
			setOrder(null);
			setOrder2(null);
			setOrder3(null);
			getCombat().updateVisibility(this);
			getCombat().notifyDeath(this);
		}
	}
	
	public boolean isArmed() {return _isArmed;}
	public boolean isMobile() {return _isMobile;}
	public boolean isAlive() {return _hp>0;}
	
	public boolean canSee(Unit anotherUnit) {
		if (isAlive()) {
			return Utils.getRange2(getX(), getY(), anotherUnit.getX(), anotherUnit.getY())<_look_range2;
		} else {
			//У раненного юнита дальность зрения в два раза ниже
			return Utils.getRange2(getX(), getY(), anotherUnit.getX(), anotherUnit.getY())<_look_range2/25;			
		}
	}
	
	//Меня видят
	synchronized public void addLookingUnit(Unit u) {
		if (_theySeeMe.get(u.getSide()).indexOf(u)==-1) {
			_theySeeMe.get(u.getSide()).add(u);
			_theyAllSeeMe.add(u);
		}		
	}
	synchronized public void removeLookingUnit(Unit u) {
		_theySeeMe.get(u.getSide()).remove(u);
		_theyAllSeeMe.remove(u);
	}
	public int getLookingSize(int side) {return _theySeeMe.get(side).size();}
	public ArrayList<Unit> getLookingUnits() {
		return _theyAllSeeMe;
	}
	
	//Я вижу
	synchronized public void addSeenUnit(Unit u) {
		if (_iSeeThem.get(u.getSide()).indexOf(u)==-1) {
			_iSeeThem.get(u.getSide()).add(u);
		}
	}
	synchronized public void removeSeenUnit(Unit u) {_iSeeThem.remove(u);}
	public int getSeenSize() {return _iSeeThem.size();}
	
	
	public void processTick(long timePoint) {
		if (!isAlive()) return;
		
		if (_order!=null) {
			_order.processTick(timePoint);
		}
		if (_order2!=null) {
			_order2.processTick(timePoint);
		}
		if (_order3!=null) {
			_order3.processTick(timePoint);
		}

	}
}
