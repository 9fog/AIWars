package model.vo
{
	public class FlagVO extends AItemVO
	{
		public function FlagVO()
		{
			super();
		}
		
		public var progress:int;
		public var userId:int;
		public var color:int;
		
		
		
		override public function get clone():AItemVO
		{
			var item:FlagVO = new FlagVO();
			item.id = id;
			item.x = x;
			item.y = y;
			item.progress = progress;
			item.userId = userId;
			item.color = color;
			return item;
		}
	}
}