package model.parser.commands
{
	import controller.EngineEvent;
	
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
				var content:Array = String(temp[2]).split("");
				
				var z:int = 0;
				var out:Array = [];
				for (var i:int = 0;i < w; i++)
				{
					var line:Array = [];
					for (var j:int = 0;j < h; j++)
					{
						line[j] = int(content[z]);
							z+=1;
					}
					out[i] = line;
				}
				
				space.map = new MapVO(w, h, out);
			return data;
		}
	}
}