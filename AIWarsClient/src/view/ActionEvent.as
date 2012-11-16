package view
{
	import flash.events.Event;
	
	public class ActionEvent extends Event
	{
		public static var ACTION:String = "ACTION";
		public static var PLAY:String = "PLAY";
		public static var RESTART:String = "RESTART";
		public static var PAUSE:String = "PAUSE";
		
		public var data:String;
		
		public function ActionEvent(data:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			this.data = data;
			super(ACTION, bubbles, cancelable);
		}
	}
}