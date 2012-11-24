package model.vo
{
	public class UserVO extends AItemVO
	{
		public function UserVO()
		{
			super();
		}
		
		public var name:String;
		
		public var color:int;
		public var coins:int;
		
		public function setColor():int
		{
			switch(id)
			{
				
				case 0:
					return 0x000080;
				case 1:
					return 0x990000;
				case 2:
					return 0x006699;
				case 3:
					return 0x996600;
				case 4:
					return 0x009900;
				case 5:
					return 0xCC0033;
				case 6:
					return 0x00CCCC;
				case 7:
					return 0xCCCC00;
				case 8:
					return 0x666600;
			}
			return 0xffffff;
		}
		
		override public function get clone():AItemVO
		{
			var item:UserVO = new UserVO();
			item.id = id;
			item.x = x;
			item.y = y;
			item.name = name;
			item.color = color;
			item.coins = coins;
			return item;
		}
	}
}