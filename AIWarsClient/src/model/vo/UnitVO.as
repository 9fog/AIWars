package model.vo
{
	public class UnitVO extends AItemVO
	{
		public function UnitVO()
		{
			super();
		}
		
		public var type:UnitTypeVO;
		public var userId:int;
		
		public var xf:int;
		public var yf:int;
		public var vx:Number;
		public var vy:Number;
		public var fire:Boolean;
		public var cannonRotation:Number;
		public var hit:Boolean;
		public var hp:int;
		public var hpMax:int;
		public var isArmed:Boolean;
		public var isMobile:Boolean;
		public var isAlive:Boolean;
		
		
		override public function get clone():AItemVO
		{
			var item:UnitVO = new UnitVO();
			item.id = id;
			item.x = x;
			item.y = y;
			item.xf = xf;
			item.yf = yf;
			item.vx = vx;
			item.vy = vy;
			item.type = type;//typeId.clone оптимизация дабы не клонировать константу
			item.userId = userId;
			item.cannonRotation = cannonRotation;
			item.fire = false//fire; так надо
			item.hit = false;//hit; так надо
			item.hp = hp;
			item.hpMax = hpMax;
			item.isArmed = isArmed;
			item.isMobile = isMobile;
			item.isAlive = isAlive;
			return item;
		}
		
		
		override public function calculateStep():void
		{
			if (vx !=0 )
			if (x != xf)
				x = x + vx;
			else
				vx = 0;
			
			if (vy!=0)
			if (y != yf)
				y = y + vy;
			else
				vy = 0;
		}
		
	}
}