package view
{
	import com.greensock.TweenLite;
	import com.greensock.easing.Back;
	import com.greensock.easing.Linear;
	
	import controller.Engine;
	import controller.EngineEvent;
	
	import flash.display.DisplayObject;
	import flash.display.MovieClip;
	import flash.events.Event;
	
	import model.Space;
	import model.vo.BoomVO;
	import model.vo.FlagVO;
	import model.vo.MapVO;
	import model.vo.UnitVO;
	import model.vo.UserVO;
	
	import mx.events.ResizeEvent;
	import mx.olap.aggregators.MinAggregator;
	
	import spark.components.BorderContainer;
	
	import view.components.MapPanel;
	import view.items.AView;
	import view.items.BoomView;
	import view.items.FlagView;
	import view.items.MapCellView;
	import view.items.UnitView;
	
	public class SpaceMediator
	{
		public static const SIZE:int = 50;
		
		private var _view:SpaceView;
		private var _controller:Engine;
		private var _maskList:Array=[];
		
		
		public function get showViewDistanceCircle():Boolean
		{
			if (_view)
				return _view.cbView.selected;
			return false;
		}
		
		public function get showFireDistanceCircle():Boolean
		{
			if (_view)
				return _view.cbFire.selected;
			return false;
		}
		
		public function SpaceMediator(view:SpaceView, controller:Engine)
		{
			_view = view;
			_controller = controller; 
			_view.addEventListener(ActionEvent.ACTION, onAction);
			_controller.addEventListener(EngineEvent.UPDATE, updateView);
			_controller.addEventListener(EngineEvent.END, onEnd);
			_view.stepNumber = _controller.steps.length;
			
			
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
					
				case ActionEvent.SLIDE:
				{
					removeView();
					_view.txtLog.text = "";
					_controller.currentStep = _view.step;
					break;
				}
					
				case ActionEvent.CHANGE_VIEW:
				{
					_view.map.showGrid = _view.cbGrid.selected;
					
					break;
				}
					
				case ActionEvent.CHANGE_DG_VIEW:
				{
					if ((event.obj.user as UserVO).show)
						_maskList.push(event.obj.user as UserVO);
					else
						_maskList = removeUserFromMask(_maskList, event.obj.user as UserVO);
					break;
				}
			}
		}
		
		public function findUser(maskList:Array, id:int):Boolean
		{
			for each (var i:UserVO in maskList) 
			{
				if (i.id == id) return true;
			}
			return false;
		}
		
		private function removeUserFromMask(arr:Array, user:UserVO):Array
		{
			var out:Array = [];
			for each (var i:UserVO in arr) 
			{
				if (i.id != user.id)
					out.push(i);
			}
			return out;
		}
		
		private function getLog(turnId:int):String
		{
			var out:String = "";
			for each(var space:Space in _controller.spaces)
			{
				out = space.log + out;
				if (space.turnId == turnId)
					return out;
			}
			return out;
		}
		
		private function updateView(e:EngineEvent):void
		{
			
			
			
		//	trace("update",e.space.turnId);
			var space:Space = e.space;
			
			if (space.turnId == 0)
			{
				createMap(space);
				for each (var i:UserVO in space.users) 
				{
					_maskList.push(i);	
				}
			}
			
			for each (var i:UserVO in space.users) 
			{
				i.show = findUser(_maskList,i.id);	
			}
			
			
			_view.maskLayer.update(space, SIZE, _maskList);
			
			if (_view.txtLog.text.length>1000)
				_view.txtLog.text = "";
			_view.txtLog.text = space.log + _view.txtLog.text;
			_view.step = space.turnId;
			
			
			
			//flags
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
			
			//uniits
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
			
			removeDeadUnits(space);
			
			//booms
			for each (var boom:BoomVO in space.booms)
			{
				if (!findBoom(boom.id))
				{
					createBoom(boom);
				}
			}
			
			removeDeadBooms(space);
			
			
			//player
			_view.playerPanel.update(space.users);
		}
		
		/**
		 * createMap
		 */ 
		
		public function createMap(space:Space):void
		{
			
			_view.map.init(space, SIZE);
			var conteiner:BorderContainer = _view.conteiner;
			
				conteiner.width = space.map.width * SIZE+2;
				conteiner.height = space.map.height * SIZE+2;
				_view.onResize(null);
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
			_view.map.addChild(cell);
		}
		
		/**
		 * createBoom
		 */
		
		public function createBoom(item:BoomVO):void
		{
			var len:Number = SIZE;
			var cell:BoomView  = new BoomView();
			cell.width = len;
			cell.height = len;
			cell.x = item.x * len+len/2;
			cell.y = item.y * len+len/2;
			cell.vo = item.clone;
			var lastIndex:int = _view.unitConteiner.numElements; 
			_view.unitConteiner.addElementAt(cell,0);
			cell.play();
		}
		
		/**
		 * remove boom
		 */ 
		public function removeDeadBooms(space:Space):void
		{
			var conteiner:BorderContainer = _view.unitConteiner;
			for(var i:int = conteiner.numElements-1; i >= 0; i--)
			{
				var unit:* = conteiner.getElementAt(i);
				if (unit is BoomView)
				{
					if (space.getBoomById((unit as AView).vo.id) == null) 
						_view.unitConteiner.removeElement(unit);	
				}
			}
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
			
			_view.unitConteiner.addElement(cell);
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
				
				//if ((int(y)-int(cell.y)) != 0 || (int(x)-int(cell.x)) != 0)
					cell.rotation = 180 * Math.atan2(y-cell.y, x-cell.x) / Math.PI;
				
				cell.update(item, this);
				
				TweenLite.to(cell,1,{x:x, y:y, ease:Linear.easeNone});
			}
		}
		
		/**
		 * remove unit
		 */ 
		public function removeDeadUnits(space:Space):void
		{
			var conteiner:BorderContainer = _view.unitConteiner;
			for(var i:int = conteiner.numElements-1; i >= 0; i--)
			{
				var unit:* = conteiner.getElementAt(i);
				if (unit is UnitView)
				{
					if (space.getUnitById((unit as AView).vo.id) == null) 
						_view.unitConteiner.removeElement(unit);	
				}
			}
		}
		
		/**
		 * update flag
		 */ 
		public function updateFlag(item:FlagVO):void
		{
			var cell:FlagView  = findFlag(item.id);
			cell.update(item, this);
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
		public function findBoom(boomId:int):BoomView
		{
			return find(BoomView, boomId) as BoomView;
		}
		
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
			var conteiner:BorderContainer = _view.unitConteiner;
			for(var i:int = conteiner.numElements-1; i >= 0; i--)
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
		

		
			
		public function scroll(delta:int):void
		{
			_view.scaling(delta);
		}
		
		public function removeView():void
		{
			var conteiner:BorderContainer = _view.unitConteiner;
			for(var i:int = conteiner.numElements - 1; i >= 0 ; i--)
			{
				var unit:* = conteiner.getElementAt(i);
				if (unit is UnitView || unit is BoomView)
				{
						_view.unitConteiner.removeElement(unit);	
				}
			}
		}
		
	}
}