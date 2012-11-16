package model.vo
{
	import flash.utils.setTimeout;
	
	import model.Space;
	import model.parser.PC;
	
	public class TurnVO
	{
		public function TurnVO()
		{
		}
		public var turnId:int;
		public var commands:Vector.<PC> = new Vector.<PC>();
		private var commandIndex:int = 0;
		
		public function interprete(prevSpace:Space = null):Space
		{
			var currentSpace:Space;
			if (prevSpace == null)
				currentSpace = new Space();
			else
				currentSpace = prevSpace.clone;
			return step(currentSpace);
		}
		
		private function step(currentSpace:Space):Space
		{
			var log:String = "-------------------\n"+"step "+turnId+"\n-------------------\n";
			for each(var command:PC in commands)
			{
				log = command.parse(currentSpace) + log;
			}
			currentSpace.turnId = turnId;
			currentSpace.log = log + currentSpace.log; 
			return currentSpace;
		}
	}
}