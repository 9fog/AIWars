package parser
{
	import flash.external.ExternalInterface;
	
	import parser.commands.CBoom;
	import parser.commands.CCapture;
	import parser.commands.CCoins;
	import parser.commands.CCreate;
	import parser.commands.CFlag;
	import parser.commands.CHit;
	import parser.commands.CMap;
	import parser.commands.CMove;
	import parser.commands.CPlayer;
	import parser.commands.CRemove;
	import parser.commands.CShot;
	
	import vo.TurnVO;
	
	public class FileParser extends Object
	{
		
		public  var steps:Vector.<TurnVO>;
		public static const INIT:String = "init";
		public static const END:String = "end";
		public static const TURN:String = "turn";
		
		public function FileParser()
		{
			
		}
		
		public function parse(data:String):void
		{
			steps = new Vector.<TurnVO>();
			
			var start:int = data.indexOf(INIT);
			var end:int = data.indexOf(END);
			if (start>=0 && end >=0)
			{
				var init:TurnVO = parseStep(data.substring(start,end));
				if (init != null)
					steps.push(init);
				
				data = data.substring(end + END.length);
				
				while (data.length > 0)
				{
					start = data.indexOf(TURN);
					end = data.indexOf(END);
					if (start>=0 && end >=0)
					{
						var turn:TurnVO = parseStep(data.substring(start,end));
						if (turn != null)
							steps.push(turn);
						data = data.substring(end + END.length);
					}
				}
			}
			
		}
		
		private function getCommand(string:String):PC
		{
			var end:int = string.indexOf(" ");
			var name:String = string.substring(0,end);
			switch(name)
			{
				case CBoom.NAME:
					return new CBoom(string);
				case CCapture.NAME:
					return new CCapture(string);
				case CCoins.NAME:
					return new CCoins(string);
				case CCreate.NAME:
					return new CCreate(string);
				case CFlag.NAME:
					return new CFlag(string);
				case CHit.NAME:
					return new CHit(string);
				case CMap.NAME:
					return new CMap(string);
				case CMove.NAME:
					return new CMove(string);
				case CPlayer.NAME:
					return new CPlayer(string);
				case CRemove.NAME:
					return new CRemove(string);
				case CShot.NAME:
					return new CShot(string);
			}
				
			return null;
		}
		
		private  function parseStep(data:String):TurnVO
		{
			var turn:TurnVO = new TurnVO();
			var arr:Array = data.split("\n");
			for(var i:int = 1; i < arr.length; i++)
			{
				var command:PC = getCommand(arr[i]);
				if (command != null)
					turn.commands.push(command);
			}
			return turn;
		}
	}
}