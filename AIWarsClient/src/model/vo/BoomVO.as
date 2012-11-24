package model.vo
{
	public class BoomVO extends AItemVO
	{
		public function BoomVO()
		{
			super();
		}
		
		
		public var power:int;
		
		override public function get clone():AItemVO
		{
			var item:BoomVO = new BoomVO();
			item.id = id;
			item.x = x;
			item.y = y;
			item.power = power;
			
			return item;
		}
	}
}