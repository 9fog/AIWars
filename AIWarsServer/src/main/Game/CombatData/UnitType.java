package main.Game.CombatData;

public class UnitType {
	private final int _id;
	private final String _role;
	private final int _moving_speed,                         //Сколько милисекунд на одну клетку
    				  _rotate_speed,                          //Сколько милисукунд на поворот башни и корпуса на 1 сектор
                      _fire_rate,                             //Время перезарядки (мс)
                      _fire_disperce,                         //Разброс меткости в клетках
                      _fire_power,                            //Мощность оружия       
                      _look_range,     _look_range2,          //Дальность зрения 
                      _shot_range_min,     _shot_range_min2,  //Минимальная дальность стрельбы
                      _shot_range_max,      _shot_range_max2, //Максимальная дальность стрельбы	             
                      _max_hp,                                 //Запас жизни
                      
                      _price;                                 //Цена (в монетах)
	
	public UnitType(int id, String role, int moving_speed, int rotate_speed, int fire_rate, int fire_disperce, int fire_power,
			        int look_range, int shot_range_min, int shot_range_max, int max_hp,
			        int price) {
		_id              = id;
		_role            = role;
		_moving_speed    = moving_speed;
		_rotate_speed    = rotate_speed;
		_fire_rate       = fire_rate;
		_fire_disperce   = fire_disperce;
		_fire_power      = fire_power;
		_look_range      = look_range;
		_look_range2     = _look_range*_look_range;
		_shot_range_min  = shot_range_min;
		_shot_range_min2 = _shot_range_min*_shot_range_min;
		_shot_range_max  = shot_range_max;
		_shot_range_max2 = _shot_range_max*_shot_range_max;
		_max_hp  		 = max_hp;
		_price           = price;
	}
	
	public int id() {return _id;}
	public String role() {return _role;}
	public int moving_speed() {return _moving_speed;}
	public int rotate_speed() {return _rotate_speed;}
	public int fire_rate() {return _fire_rate;}
	public int fire_disperce() {return _fire_disperce;}
	public int fire_power() {return _fire_power;}
	public int look_range() {return _look_range;}
	public int look_range2() {return _look_range2;}
	public int shot_range_min() {return _shot_range_min;}
	public int shot_range_min2() {return _shot_range_min2;}
	public int shot_range_max() {return _shot_range_max;}
	public int shot_range_max2() {return _shot_range_max2;}
	public int max_hp() {return _max_hp;}
	public int price() {return _price;}
}
