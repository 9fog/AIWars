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
		//apture [recieverID] [flagID] [ownerID] [state] //state = 0...10
		override public function parse(space:Space):String
		{
			var s:String = data.replace(name + " ", "");
			var temp:Array = s.split(" ");
			var recieverID:int = temp[0];
			var item:FlagVO = space.getFlagById(temp[1]);
			item.userId = temp[2];
			item.progress = temp[3];
			item.color = space.getUserById(item.userId).color;
			return data;
		}
	}
}