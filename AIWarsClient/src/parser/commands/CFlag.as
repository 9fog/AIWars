package parser.commands
{
	import parser.PC;
	
	public class CFlag extends PC
	{
		
		public static var NAME:String = "flag";  
		
		public function CFlag(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse():void
		{
		}
		
	}
}