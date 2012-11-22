package model.parser.commands
{
	import model.parser.PC;
	
	public class CTurret extends PC
	{
		public static var NAME:String = "turret";  
		
		public function CTurret(data:String)
		{
			super(NAME, data);
		}
	}
}