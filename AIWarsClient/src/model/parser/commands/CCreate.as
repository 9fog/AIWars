package model.parser.commands
{
	import controller.EngineEvent;
	
	import model.Space;
	import model.parser.PC;
	import model.vo.FlagVO;
	import model.vo.UnitTypeVO;
	import model.vo.UnitVO;
	
	public class CCreate extends PC
	{
	
		public static var NAME:String = "create";  
		
		public function CCreate(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse(space:Space):String
		{
			
			var s:String = data.replace(name + " ", "");
			var temp:Array = s.split(" ");
			
			var item:UnitVO = new UnitVO();
			item.id = int(temp[0]);
			item.type = new UnitTypeVO(int(temp[1]));
			item.x = temp[2];
			item.y = temp[3];
			item.xf = item.x;
			item.yf = item.y;
			item.vx = 0;
			item.vy = 0;
			item.userId = temp[4];
			item.hpCannon = item.type.hpCannonMax;
			item.hpGear = item.type.hpGearMax;
			item.cannonRotation = 0;
			space.all.push(item);
			
			return data;
		}
	}
}