package model.parser.commands
{
	import controller.EngineEvent;
	
	import model.Space;
	import model.parser.PC;
	import model.vo.FlagVO;
	
	public class CFlag extends PC
	{
		
		public static var NAME:String = "flag";  
		
		public function CFlag(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse(space:Space):String
		{
			
			var s:String = data.replace(name + " ", "");
			var temp:Array = s.split(" ");
			
			var item:FlagVO = new FlagVO();
			item.id = int(temp[0]);
			item.x = temp[1];
			item.y = temp[2];
			space.all.push(item);
			
			return data;
		}
		
	}
}