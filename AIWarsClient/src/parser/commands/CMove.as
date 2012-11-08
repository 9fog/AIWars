package parser.commands
{
	import parser.PC;
	
	public class CMove extends PC
	{
		public static var NAME:String = "move";  
		
		public function CMove(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse():void
		{
		}
	}
}