package model
{
	public class Config
	{
		private var conf:XML;
			
		private static var _instanse:Config;
		public static function get instanse():Config
		{
			if (_instanse == null) _instanse = new Config();
			return _instanse; 
		}
		
		public function Config()
		{
			
		}
		
		public var stepInterval:Number = 500;
		
		public function getUnitTypeField(id:int, field:String):int
		{
			var list:XMLList = conf.units;
			for (var propertyName:String in list.children()) 
			{
				var item:XMLList = list.child(propertyName);
				if (id == int(item.attribute("type")))
				{
					return item.attribute(field);
				}
			}
			return 0;
		}
		
		public function init(s:String):void
		{
			conf = new XML(s);
		}
	}
}