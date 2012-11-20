package model.parser.commands
{
	import controller.EngineEvent;
	
	import model.Config;
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
			
			var recieverID:int = temp[0];
			var item:UnitVO = space.getUnitById(temp[1]);
			
			item.xf = temp[2];
			item.yf = temp[3];
			item.vx = (item.xf-item.x)/(temp[4]/Config.instanse.stepInterval);
			item.vy = (item.yf-item.y)/(temp[4]/Config.instanse.stepInterval);
			return data;
		}
	}
}