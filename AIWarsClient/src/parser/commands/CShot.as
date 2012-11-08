package parser.commands
{
	import parser.PC;
	
	public class CShot extends PC
	{
		public static var NAME:String = "shot";  
		
		public function CShot(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse():void
		{
		}
	}
}