package model.parser.commands
{
	import model.Space;
	import model.parser.PC;
	import model.vo.UnitVO;
	
	public class CRemove extends PC
	{
		public static var NAME:String = "remove";  
		
		public function CRemove(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse(space:Space):String
		{
			var s:String = data.replace(name + " ", "");
			var temp:Array = s.split(" ");
			
			space.removeUnitById(temp[0]);
			return data;
		}
	}
}