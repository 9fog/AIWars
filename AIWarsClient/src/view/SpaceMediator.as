package view
{
	import com.greensock.TweenLite;
	import com.greensock.easing.Linear;
	
	import controller.Engine;
	import controller.EngineEvent;
	
	import flash.display.DisplayObject;
	import flash.display.MovieClip;
	import flash.events.Event;
	
	import model.Space;
	import model.vo.FlagVO;
	import model.vo.MapVO;
	import model.vo.UnitVO;
	import model.vo.UserVO;
	
	import mx.events.ResizeEvent;
	import mx.olap.aggregators.MinAggregator;
	
	import spark.components.BorderContainer;
	
	import view.items.AView;
	import view.items.FlagView;
	import view.items.MapCellView;
	import view.items.UnitView;
	
	public class SpaceMediator
	{
		public static const SIZE:int = 50;
		
		public var _view:SpaceView;
		public var _controller:Engine;
		
		/*
		public function get space():Space
		{
			return Space.instance;
		}
		*/
		
		
		public function SpaceMediator(view:SpaceView, controller:Engine)
		{
			_view = view;
			_controller = controller; 
			_view.addEventListener(ActionEvent.ACTION, onAction);
			_controller.addEventListener(EngineEvent.UPDATE, updateView);
			_controller.addEventListener(EngineEvent.END, onEnd);
			
		}
		
		public function onAction(event:ActionEvent):void
		{
			switch(event.data)
			{
				case ActionEvent.PAUSE:
				{
					_controller.stop();
					break;
				}
				case ActionEvent.PLAY:
				{
					_controller.play();
					break;
				}
				case ActionEvent.RESTART:
				{
					_view.clear();
					_controller.restart();
					break;
				}
			}
		}
		
		private function updateView(e:EngineEvent):void
		{
			trace("update",e.space.turnId);
			var space:Space = e.space;
			
			_view.txtLog.text = space.log;
			
			if (space.turnId == 0)
				createMap(space);
			
			for each (var flag:FlagVO in space.flags)
			{
				if (findFlag(flag.id))
				{
					updateFlag(flag);
				}
				else
				{
					createFlag(flag);
				}
			}
			
			for each (var unit:UnitVO in space.units)
			{
				if (findUnit(unit.id))
				{
					moveUnit(unit);
				}
				else
				{
					createUnit(unit, space.getUserById(unit.userId));
				}
			}
			
			checkRemoveUnits(space);
			_view.playerPanel.update(space.users);
		}
		
		/**
		 * createMap
		 */ 
		
		public function createMap(space:Space):void
		{
			var conteiner:BorderContainer = _view.conteiner;
			var lenW:Number = conteiner.width/space.map.width;
			var lenH:Number = conteiner.height/space.map.height;
			var len:Number = SIZE;//Math.min(lenH, lenW);
			
			for (var i:int = 0; i < space.map.width;i++)
			{
				for (var j:int = 0; j < space.map.height;j++)
				{
					var cell:MapCellView = new MapCellView();
					cell.width = len;
					cell.height = len;
					cell.x = i * len;
					cell.y = j * len;
					cell.empty = space.map.array[i][j] == MapVO.EMPTY;
					conteiner.addElement(cell);
				}
				conteiner.width = space.map.width * len+2;
				conteiner.height = space.map.height * len+2;
				_view.onResize(null);
			}
		}
		
		/**
		 * createFlag
		 */
		
		public function createFlag(item:FlagVO):void
		{
			var len:Number = SIZE;
			var cell:FlagView = new FlagView();
			cell.width = len;
			cell.height = len;
			cell.x = item.x * len;
			cell.y = item.y * len;
			cell.rotation = 0;
			cell.vo = item.clone;
			_view.conteiner.addElement(cell);
		}
		
		/**
		 * createUnit
		 */
		
		public function createUnit(item:UnitVO, player:UserVO):void
		{
			var len:Number = SIZE;
			var cell:UnitView  = new UnitView();
			cell.width = len;
			cell.height = len;
			cell.x = item.x * len+len/2;
			cell.y = item.y * len+len/2;
			cell.color = player.color;
			cell.vo = item.clone;
			
			_view.conteiner.addElement(cell);
		}
		
		/**
		 * moveUnit
		 */
		
		public function moveUnit(item:UnitVO):void
		{
			var len:Number = SIZE;
			var cell:UnitView  = findUnit(item.id);
			if (cell)
			{
				var x:Number = item.x * len+len/2;
				var y:Number = item.y * len+len/2;
				cell.update(item);
				if ((int(y)-int(cell.y)) != 0 || (int(x)-int(cell.x)) != 0)
					cell.rotation = 180 * Math.atan2(y-cell.y, x-cell.x) / Math.PI;
				TweenLite.to(cell,1,{x:x, y:y, ease:Linear.easeNone});
			}
		}
		
		/**
		 * update flag
		 */ 
		public function updateFlag(item:FlagVO):void
		{
			var cell:FlagView  = findFlag(item.id);
			cell.update(item);
		}
				
		/**
		 * finish animation
		 */ 
		public function onEnd(event:EngineEvent):void
		{
			_view.txtLog.text = ("\n----------------------\nend\n----------------------\n") + _view.txtLog.text;
			_view.currentState = "end";
		}
		
		/**
		 * find
		 */ 
		public function findUnit(unitId:int):UnitView
		{
			return find(UnitView, unitId) as UnitView;
		}
		
		public function findFlag(unitId:int):FlagView
		{
			return find(FlagView, unitId) as FlagView;
		}
		
		public function find(c:Class, unitId:int):AView
		{
			var conteiner:BorderContainer = _view.conteiner;
			for(var i:int = 0; i < conteiner.numElements; i++)
			{
				var unit:* = conteiner.getElementAt(i);
				if (unit is c)
				{
					if ((unit as AView).vo.id == unitId) 
						return unit;
				}
			}
			return null;
		}
		
		public function checkRemoveUnits(space:Space):void
		{
			var conteiner:BorderContainer = _view.conteiner;
			for(var i:int = 0; i < conteiner.numElements; i++)
			{
				var unit:* = conteiner.getElementAt(i);
				if (unit is UnitView)
				{
					if (space.getUnitById((unit as AView).vo.id) == null) 
						_view.conteiner.removeElement(unit);	
				}
			}
		}
		
	}
}