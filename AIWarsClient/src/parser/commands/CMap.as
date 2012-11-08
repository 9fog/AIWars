package parser.commands
{
	import mx.states.OverrideBase;
	import parser.PC;

	public class CMap extends PC
	{
		public static var NAME:String = "map";  
		
		public function CMap(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse():void
		{
		}
	}
}