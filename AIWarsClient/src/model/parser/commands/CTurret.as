package model.parser.commands
{
	import model.Space;
	import model.parser.PC;
	import model.vo.UnitVO;
	
	public class CTurret extends PC
	{
		public static var NAME:String = "turret";  
		
		/*
		turret [recieverID] [unitID] [turretLook] //Unit rotates turret
		int[][] DIRECTIONS =
			{{7, 0, 1}, 
				{6, 0, 2},
				{5, 4, 3}};
		*/
		public function CTurret(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse(space:Space):String
		{
			var s:String = data.replace(name + " ", "");
			var temp:Array = s.split(" ");
			
			var recieverID:int = temp[0];
			var item:UnitVO = space.getUnitById(temp[1]);
			
			var sector:int = int(temp[2]);
			item.cannonRotation = sector !=7 ? -90 + 45*int(temp[2]) : -135;
			item.initTurrt = true;
			return data;
		}
	}
}