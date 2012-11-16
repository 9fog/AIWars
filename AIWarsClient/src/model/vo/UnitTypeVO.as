package model.vo
{
	import model.Config;

	public class UnitTypeVO
	{
		public function UnitTypeVO(id:int)
		{
			typeId = id;
			lookRange = Config.instanse.getUnitTypeField(id,"look_range");
			shotRangeMin = Config.instanse.getUnitTypeField(id,"shot_range_min");
			shotRangeMax = Config.instanse.getUnitTypeField(id,"shot_range_max");
			hpCannonMax = Config.instanse.getUnitTypeField(id,"hp_cannon_max");
			hpGearMax = Config.instanse.getUnitTypeField(id,"hp_gear_max");
		}
		
		public var typeId:int;
		public var lookRange:int;
		public var shotRangeMin:int;
		public var shotRangeMax:int;
		public var hpCannonMax:int;
		public var hpGearMax:int;
		
		public function get clone():UnitTypeVO
		{
			return new UnitTypeVO(typeId);
		}
	}
}