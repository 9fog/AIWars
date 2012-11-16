package model.parser.commands
{
	import model.Space;
	import model.parser.PC;
	import model.vo.UnitVO;
	
	public class CShot extends PC
	{
		public static var NAME:String = "shot";  
		
		public function CShot(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse(space:Space):String
		{
			var s:String = data.replace(name + " ", "");
			var temp:Array = s.split(" ");
			
			var item:UnitVO = space.getUnitById(temp[0]);
			var target:UnitVO = space.getUnitById(temp[1]);
			
			item.cannonRotation = 180 * Math.atan2(target.y-item.y, target.x-item.x) / Math.PI;
			//if (item.cannonRotation < 0) item.cannonRotation = 270 - item.cannonRotation;
			item.fire = true; 
			return data;
		}
	}
}