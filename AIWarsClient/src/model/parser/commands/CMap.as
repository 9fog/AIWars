package model.parser.commands
{
	import controller.EngineEvent;
	
	import flash.geom.Point;
	
	import model.Space;
	import model.parser.PC;
	import model.vo.MapVO;
	
	import mx.states.OverrideBase;

	public class CMap extends PC
	{
		public static var NAME:String = "map";  
		
		public function CMap(data:String)
		{
			super(NAME, data);
		}
		
		override public function parse(space:Space):String
		{
				var s:String = data.replace(name + " ", "");
				
				var temp:Array = s.split(" ");
				var w:int = int(temp[0]);
				var h:int = int(temp[1]);
				var points:Array = String(temp[2]).split(";");
				var rocks:Array = [];
				
				
				var out:Array = [];
				for (var i:int = 0;i < h; i++)
				{
					var line:Array = [];
					for (var j:int = 0;j < w; j++)
					{
						line[j] = 0;
					}
					out[i] = line;
				}

				for each(var xy:String in points)
				{
					var pare:Array = (xy as String).split(":");
					var x:int = pare[0];
					var y:int = pare[1];
					out[y][x] = 1;
				}
				
				
				space.map = new MapVO(w, h, out);
			return data;
		}
	}
}