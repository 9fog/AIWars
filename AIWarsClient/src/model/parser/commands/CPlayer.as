package model.parser.commands
{
	import controller.EngineEvent;
	
	import flashx.textLayout.events.UpdateCompleteEvent;
	
	import model.Space;
	import model.parser.PC;
	import model.vo.UserVO;
	
	import org.osmf.events.PlayEvent;
	
	public class CPlayer extends PC
	{

		public static var NAME:String = "player";  
		
		public function CPlayer(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse(space:Space):String
		{
			var s:String = data.replace(name + " ", "");
			var temp:Array = s.split(" ");
			
			var player:UserVO = new UserVO();
			player.id = int(temp[0]);
			player.name = temp[1];
			player.color = player.setColor();
			space.users.push(player);
			
			return data;
		}
	}
}