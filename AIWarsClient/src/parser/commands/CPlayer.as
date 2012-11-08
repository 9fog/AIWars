package parser.commands
{
	import parser.PC;
	
	public class CPlayer extends PC
	{

		public static var NAME:String = "player";  
		
		public function CPlayer(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse():void
		{
		}
	}
}