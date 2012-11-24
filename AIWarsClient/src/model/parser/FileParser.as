package model.parser
{
	import flash.external.ExternalInterface;
	
	import model.parser.commands.CBoom;
	import model.parser.commands.CCapture;
	import model.parser.commands.CCoins;
	import model.parser.commands.CCreate;
	import model.parser.commands.CFlag;
	import model.parser.commands.CHit;
	import model.parser.commands.CMap;
	import model.parser.commands.CMove;
	import model.parser.commands.CPlayer;
	import model.parser.commands.CRemove;
	import model.parser.commands.CFire;
	import model.parser.commands.CommandFactory;
	
	import model.vo.TurnVO;
	
	public class FileParser extends Object
	{
		
		public  var steps:Vector.<TurnVO>;
		//init key
		public static const INIT:String = "init";
		//end key
		public static const END:String = "end";
		//turn key
		public static const TURN:String = "turn";
		
		public function FileParser(){}
		
		/**
		 * split log file by turns
		 */ 
		public function parse(data:String):Vector.<TurnVO>
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
				
				while (data.length > 0 && start>=0 && end >=0)
				{
					start = data.indexOf(TURN);
					end = data.indexOf(END);
					if (start>=0 && end >=0)
					{
						var turn:TurnVO = parseStep(data.substring(start,end));
						turn.turnId += 1;
						if (turn != null)
							steps.push(turn);
						data = data.substring(end + END.length);
					}
					
				}
			}
			return steps;
		}
		
		/**
		 * split turn by commands
		 */ 
		private  function parseStep(data:String):TurnVO
		{
			var turn:TurnVO = new TurnVO();
			var arr:Array = data.split("\n");
			
			turn.turnId = int((arr[0] as String).replace(TURN+" ",""));
			//trace(turn.turnId);
			for(var i:int = 1; i < arr.length; i++)
			{
				var command:PC = CommandFactory.getCommand(arr[i]);
				if (command != null)
					turn.commands.push(command);
			}
			return turn;
		}
	}
}