package model.parser.commands
{
	import model.parser.PC;
	
	public class CHide extends PC
	{
		public static var NAME:String = "hide"; 
		
		public function CHide(data:String)
		{
			super(NAME, data);
		}
	}
}