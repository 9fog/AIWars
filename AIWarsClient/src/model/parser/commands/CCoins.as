package model.parser.commands
{
	import model.Space;
	import model.parser.PC;
	import model.vo.UserVO;
	
	public class CCoins extends PC
	{
		public static var NAME:String = "coins";  
		
		public function CCoins(data:String)
		{
			super(NAME, data);
		}
		
		//coins [recieverID] [coinsCount]
		override public function parse(space:Space):String
		{
			var s:String = data.replace(name + " ", "");
			var temp:Array = s.split(" ");
			
			var user:UserVO = space.getUserById(temp[0]);
			user.coins = temp[1];
			
			return data;
		}
	}
}