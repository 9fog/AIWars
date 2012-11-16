package controller
{
	import flash.events.Event;
	
	import model.Space;
	import model.vo.AItemVO;
	
	public class EngineEvent extends Event
	{
		public static const UPDATE:String = "UPDATE";
		public static const UPDATE_LOG:String = "UPDATE_LOG";
		public static const END:String = "END";
		
		
		public var space:Space;
		
		public function EngineEvent(type:String, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
		}
	}
}