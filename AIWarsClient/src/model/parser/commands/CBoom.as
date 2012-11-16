package model.parser.commands
{
	import model.Space;
	import model.parser.PC;
	
	public class CBoom extends PC
	{
		public static var NAME:String = "boom";  
		
		public function CBoom(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse(space:Space):String
		{
			return data;
		}
	}
}