package model.parser.commands
{
	import model.Space;
	import model.parser.PC;
	
	public class CCoins extends PC
	{
		public static var NAME:String = "capture";  
		
		public function CCoins(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse(space:Space):String
		{
			return data;
		}
	}
}