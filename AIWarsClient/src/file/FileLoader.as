package file 
{
	import flash.display.MovieClip;
	import flash.errors.IOError;
	import flash.events.ErrorEvent;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	
	
	public class FileLoader
	{
		public function FileLoader()
		{}
		
		/**
		 * data
		 */ 
		public var data:String = "";
		
		/**
		 * loader
		 */ 
		private var loader:URLLoader = new URLLoader();
		
		/**
		 * call back function
		 */ 
		private var _callBackHandler:Function;
		
		/**
		 * load file by url
		 */ 
		public function loadData(url:String, callBackHandler:Function = null):void
		{
			try
			{
				_callBackHandler = callBackHandler;
				var request:URLRequest = new URLRequest(url);
				loader.addEventListener(Event.COMPLETE, onLoadComplete);
				loader.addEventListener(IOErrorEvent.IO_ERROR, showError);
				loader.load(request);
			}
			catch(e:*)
			{
				showError(e);
			}
		}
		
		/**
		 * on loading complete
		 */ 
		private function onLoadComplete(e:Event):void
		{
			try
			{
				loader.removeEventListener(Event.COMPLETE, onLoadComplete);
				data = e.target.data.toString();
				loader.close();

				if (_callBackHandler != null)
					_callBackHandler(data);
			}
			catch(e:*)
			{
				showError(e);
			}
		}
		
		/**
		 * on error
		 */ 
		private function showError(e:*):void
		{
			trace(e);
		}
		
	}
}