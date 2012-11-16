package model.parser.commands
{
	import controller.EngineEvent;
	
	import model.Space;
	import model.parser.PC;
	import model.vo.UnitVO;
	
	public class CMove extends PC
	{
		public static var NAME:String = "move";  
		
		public function CMove(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse(space:Space):String
		{
			var s:String = data.replace(name + " ", "");
			var temp:Array = s.split(" ");
			
			var item:UnitVO = space.getUnitById(temp[0]);
			item.xf = temp[1];
			item.yf = temp[2];
			item.vx = (item.xf-item.x)/temp[3];
			item.vy = (item.yf-item.y)/temp[3];
			return data;
		}
	}
}