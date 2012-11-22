package model.parser.commands
{
	import model.parser.PC;
	
	public class CShow extends PC
	{
		public static var NAME:String = "show"; 
		
		public function CShow(data:String)
		{
			super(NAME, data);
		}
	}
}