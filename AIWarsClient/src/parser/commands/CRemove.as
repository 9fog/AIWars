package parser.commands
{
	import parser.PC;
	
	public class CRemove extends PC
	{
		public static var NAME:String = "remove";  
		
		public function CRemove(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse():void
		{
		}
	}
}