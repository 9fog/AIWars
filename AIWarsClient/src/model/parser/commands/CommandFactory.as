package model.parser.commands
{
	import model.parser.PC;

	public class CommandFactory
	{
		
		//all parsing command
		public static var COMMAND_CLASSES:Array = 
			[
				CBoom, 
				CCapture, 
				CCoins, 
				CCreate, 
				CFlag, 
				CHit, 
				CMap, 
				CMove, 
				CPlayer, 
				CFire,
				CHide,
				CShow,
				CTurret
			];
		
		
		public function CommandFactory()
		{
		}
		
		/**
		 * command factory return Command object
		 */ 
		public static function getCommand(string:String):PC
		{
			if (string.length < 2) return null;
			var end:int = string.indexOf(" ");
			var name:String = string.substring(0,end);
			return findCommand(name, string);
		}
		
		private static function findCommand(name:String, data:String):PC
		{trace(data);
			for each(var c:Class in COMMAND_CLASSES)
			{
				var command:PC = new c(data);
				if (command.name == name) return command;
			}
			return null;
		}
		
	}
}