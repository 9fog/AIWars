package parser.commands
{
	import parser.PC;
	
	public class CCapture extends PC
	{
		public static var NAME:String = "capture";  
		
		public function CCapture(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse():void
		{
		}
	}
}