package model.parser.commands
{
	import model.Space;
	import model.parser.PC;
	import model.vo.UnitVO;
	
	public class CHit extends PC
	{
		public static var NAME:String = "hit";  
		
		public function CHit(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse(space:Space):String
		{
			var s:String = data.replace(name + " ", "");
			var temp:Array = s.split(" ");
			
			var item:UnitVO = space.getUnitById(temp[0]);
			item.hpGear = temp[1];
			item.hpCannon = temp[2];
			item.hit = true;
			return data;
		}
	}
}