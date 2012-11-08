package parser.commands
{
	import parser.PC;
	
	public class CHit extends PC
	{
		public static var NAME:String = "hit";  
		
		public function CHit(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse():void
		{
		}
	}
}