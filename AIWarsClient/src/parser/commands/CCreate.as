package parser.commands
{
	import parser.PC;
	
	public class CCreate extends PC
	{
	
		public static var NAME:String = "create";  
		
		public function CCreate(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse():void
		{
		}
	}
}