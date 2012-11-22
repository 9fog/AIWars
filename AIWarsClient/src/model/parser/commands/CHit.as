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
		//hit [recieverID] [unitID] [maxHP] [curHP] [isArmed] [isMobile] [isAlive] //Unit hit
		override public function parse(space:Space):String
		{
			var s:String = data.replace(name + " ", "");
			var temp:Array = s.split(" ");
			
			var recieverID:int = temp[0];
			var item:UnitVO = space.getUnitById(temp[1]);
			item.hpMax = temp[2];
			item.hp = temp[3];
			item.isArmed = toBool(temp[4]);
			item.isMobile = toBool(temp[5]);
			item.isAlive = toBool(temp[6]);
			
			item.hit = true;
			return data;
		}
		public function toBool(s:String):Boolean
		{
			return s == "true"; 
		}
	}
}