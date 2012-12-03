package model.parser.commands
{
	import model.Space;
	import model.parser.PC;
	import model.vo.UnitVO;
	
	public class CFire extends PC
	{
		public static var NAME:String = "fire";  
		
		public function CFire(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse(space:Space):String
		{
			var s:String = data.replace(name + " ", "");
			var temp:Array = s.split(" ");
			
			var recieverID:int = temp[0];
			var item:UnitVO = space.getUnitById(temp[1]);
			var target:UnitVO = space.getUnitById(temp[2]);
			
			item.cannonRotation = 180 * Math.atan2(target.y-item.y, target.x-item.x) / Math.PI;
			item.fire = true; 
			return data;
		}
	}
}