package view.utils
{
	
	
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.DisplayObject;
	import flash.display.DisplayObjectContainer;
	import flash.display.Sprite;
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.geom.Matrix;
	import flash.geom.Rectangle;
	import flash.net.FileReference;
	import flash.ui.Keyboard;
	import flash.utils.ByteArray;
	
	public class ImageUtil extends Sprite
	{
		private static var _saveHandlerComplete:Function;
		
		public function ImageUtil()
		{
		}
		
		public static function copy(mc:DisplayObject, w:int = 0, h:int = 0):Bitmap
		{
			var bitmap:Bitmap = new Bitmap();
			try
			{
				if (w == 0)
					w = mc.width;
				if (h == 0)
					h = mc.height;
				var bitmapData:BitmapData = new BitmapData(w, h, true, 0xffffff);
				bitmapData.draw(mc);
				bitmap.bitmapData = bitmapData;
			}
			catch(e:*)
			{
				//	CommonManager.log(e.toString(), true, LogErrorType.BITMAP_ERROR);
			}
			
			bitmap.width = w;
			bitmap.height = h;
			return bitmap;
		}	
	}
}