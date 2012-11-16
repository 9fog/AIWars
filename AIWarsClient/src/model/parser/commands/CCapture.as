package model.parser.commands
{
	import model.Space;
	import model.parser.PC;
	import model.vo.FlagVO;
	import model.vo.UserVO;
	
	public class CCapture extends PC
	{
		public static var NAME:String = "capture";  
		
		public function CCapture(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse(space:Space):String
		{
			var s:String = data.replace(name + " ", "");
			var temp:Array = s.split(" ");
			var item:FlagVO = space.getFlagById(temp[0]);
			item.userId = temp[1];
			item.progress = temp[2];
			item.color = space.getUserById(item.userId).color;
			return data;
		}
	}
}