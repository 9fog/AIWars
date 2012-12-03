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
		public var initTurrt:Boolean;
		public var cannonRotation:Number;
		public var hit:Boolean;
		public var hp:int;
		public var hpMax:int;
		public var isArmed:Boolean=true;
		public var isMobile:Boolean=true;
		public var isAlive:Boolean=true;
		
		
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
			item.initTurrt = initTurrt;
			return item;
		}
		
		private var eps:Number = 0.5;
		
		override public function calculateStep():void
		{
			if (vx !=0 )
			{
				if (Math.abs(x - xf)<eps)
				{
					vx = 0;
					x = xf;
				}
				else
				{
					x = x + vx;
				}	
			}
			
			if (vy!=0)
			{
				if (Math.abs(y - yf)<eps)
				{
					vy = 0;
					y = yf;
				}
				else
				{
					y = y + vy;
				}
			}
			
		}
		
	}
}