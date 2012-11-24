package model.parser.commands
{
	import model.Space;
	import model.parser.PC;
	import model.vo.BoomVO;
	
	public class CBoom extends PC
	{
		public static var NAME:String = "boom";  
		
		public function CBoom(data:String)
		{
			super(NAME, data);
		}
		//boom [recieverID] [x] [y] [power] //Boom in this point
		override public function parse(space:Space):String
		{
			var s:String = data.replace(name + " ", "");
			var temp:Array = s.split(" ");
			
			var recieverID:int = temp[0];
			var item:BoomVO = new BoomVO();
			item.x =  temp[1];
			item.y =  temp[2];
			item.power =  temp[3];
			item.id = space.nextBoomId;
				
			space.removeBoom(item.x, item.y);
			space.all.push(item);
			return data;
		}
	}
}