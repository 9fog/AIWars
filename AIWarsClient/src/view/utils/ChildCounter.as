package view.utils
{
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	import flash.display.MovieClip;
	import flash.events.Event;
	import flash.text.TextField;
	import flash.text.TextFieldAutoSize;
	import flash.utils.getTimer;
	
	import mx.core.UIComponent;
	
	public class ChildCounter extends UIComponent
	{
		
		
		private var tf:TextField;
		private var _conteiner:DisplayObjectContainer;
		public function ChildCounter( xPos:int=0, yPos:int=0, color:uint=0xFF6600, fillBackground:Boolean=false, backgroundColor:uint=0xffffff)
		{
			super();
			
			x = xPos;
			y = yPos;
			tf = new TextField();
			tf.textColor = color;
			tf.text = "";
			tf.selectable = false;
			tf.background = fillBackground;
			tf.backgroundColor = backgroundColor;
			tf.autoSize = TextFieldAutoSize.LEFT;
			addChild(tf);
			width = tf.textWidth;
			height = tf.textHeight;
			addEventListener(Event.ENTER_FRAME, tick);
		}
		
		public function set conteiner(value:DisplayObjectContainer):void
		{
			_conteiner = value;
		}
		
		public function tick(evt:Event):void 
		{
			if (_conteiner)	
			
				tf.text = count(_conteiner).toString() + " mcs";
		}
		
		private function count (conteiner:*):int
		{
			if(conteiner && conteiner.hasOwnProperty("numChildren"))
			{
				var sum:int = 0;
				for(var i:int=0;i<conteiner.numChildren;i++)
				{
					sum += count(conteiner.getChildAt(i));
				}
				return sum;
			}
			
			return 1;
		}
	}
}