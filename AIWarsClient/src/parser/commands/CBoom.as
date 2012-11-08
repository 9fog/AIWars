package parser.commands
{
	import parser.PC;
	
	public class CBoom extends PC
	{
		public static var NAME:String = "boom";  
		
		public function CBoom(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse():void
		{
		}
	}
}