package model
{
	import controller.EngineEvent;
	
	import flash.events.Event;
	import flash.events.EventDispatcher;
	
	import model.vo.AItemVO;
	import model.vo.BoomVO;
	import model.vo.FlagVO;
	import model.vo.MapVO;
	import model.vo.UnitVO;
	import model.vo.UserVO;
	
	public class Space
	{
		
		public var all:Vector.<AItemVO> = new Vector.<AItemVO>();
		public var map:MapVO;
		public var users:Vector.<UserVO> = new Vector.<UserVO>();
		
		public var turnId:int;
		public var log:String="";
		
		public function Space(){}
		
		public function get clone():Space
		{
			var s:Space = new Space();
			for each(var item:AItemVO in all)
			{
				var obj:AItemVO = item.clone;
				if (obj)
					s.all.push(obj);
			}
			s.map = map;
			s.turnId = turnId;
			s.log = log.substr();
			
			for each(var user:UserVO in users)
			{
				var tempUser:UserVO = user.clone as UserVO;
				if (tempUser)
					s.users.push(tempUser);
			}
			
			return s;
		}
		
		
		/**
		 * unit
		 */ 
		public function getUnitById(id:int):UnitVO
		{
			return find(id, UnitVO) as UnitVO;
		}
		
		public function removeUnitById(id:int):void
		{
			var out:Vector.<AItemVO> = new Vector.<AItemVO>()
			for each(var item:AItemVO in all)
			{
				if (!(item is UnitVO && item.id == id))
				{
					out.push(item);
				}
			}
			all = out;
		}
		
		public function get units():Array
		{
			return getByType(UnitVO);
		}
		
		/**
		 * user
		 */ 
		public function getUserById(id:int):UserVO
		{
			for each(var item:UserVO in users)
			{
				if (item.id == id)
					return item;
			}
			return null;
		}
		
		/**
		 * flag
		 */				
		public function getFlagById(id:int):FlagVO
		{
			return find(id, FlagVO) as FlagVO;
		}
		
		public function get flags():Array
		{
			return getByType(FlagVO);
		}
		
		/**
		 * boom
		 */		
		
		public function getBoomById(id:int):BoomVO
		{
			return find(id, BoomVO) as BoomVO;
		}
		
		public function removeBoom(x:int,y:int):void
		{
			var out:Vector.<AItemVO> = new Vector.<AItemVO>()
			for each(var item:AItemVO in all)
			{
				if (!(item is BoomVO && item.x == x && item.y == y))
				{
					out.push(item);
				}
			}
			all = out;
		}
		
		private var _currentBoomId:int = 1;
		
		public function get nextBoomId():int
		{
			return _currentBoomId = _currentBoomId + 1;
		}
		
		public function get booms():Array
		{
			return getByType(BoomVO);
		}
		
		/**
		 * find items
		 */ 
		private function getByType(type:Class):Array
		{
			var out:Array = [];
			for each(var item:AItemVO in all)
			{
				if (item is type)
					out.push(item);
			}
			return out;
		}
		
		private function find(id:int, type:Class):AItemVO
		{
			for each(var item:AItemVO in all)
			{
				if (item is type && item.id == id)
					return item;
			}
			return null;
		}
		
	}
}