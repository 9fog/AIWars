package controller
{
	import flash.events.EventDispatcher;
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	import flash.utils.setTimeout;
	
	import model.Config;
	import model.Space;
	import model.parser.PC;
	import model.vo.AItemVO;
	import model.vo.TurnVO;
	
	public class Engine extends EventDispatcher
	{
		public function Engine(steps:Vector.<TurnVO>)
		{
			super();
			this.steps = steps;
		}
		
		public var interval:int = Config.instanse.stepInterval;
		
		public  var steps:Vector.<TurnVO>;
		public  var spaces:Vector.<Space> = new Vector.<Space>();
		
		public var timer:Timer = new Timer(interval);
		public var currentStep:int = -1;
		private var isPause:Boolean = false;
		
		public function start():void
		{
			timer.addEventListener(TimerEvent.TIMER, onStep);
			timer.start();
		}
		
		public function init():void
		{
			spaces.push((steps[0] as TurnVO).interprete());
			for(var i:int=1; i < steps.length; i++)
			{
				var currentSpace:Space = (steps[i] as TurnVO).interprete(spaces[i-1] as Space);
				spaces.push(currentSpace);
				
				for each (var item:AItemVO in currentSpace.all)
				{
					item.calculateStep();
				}
			}
		}
				
		public function onStep(event:TimerEvent):void
		{
			timer.stop();
			if (isPause) return;
			currentStep += 1;
			if (currentStep < spaces.length)
			{
				update(spaces[currentStep]);
				timer.start();
			}
			else
			{
				timer.stop();
				var e:EngineEvent = new EngineEvent(EngineEvent.END, true);
				dispatchEvent(e);
			}
		}
		
		public function update(space:Space):void
		{
			var e:EngineEvent = new EngineEvent(EngineEvent.UPDATE, true);
			e.space = space;
			dispatchEvent(e);
		}
		
		public function stop():void
		{
			isPause = true;
			timer.stop();
		}
		
		public function play():void
		{
			isPause = false;
			timer.start();
		}
		
		public function restart():void
		{
			currentStep = -1;
			timer.start();
		}
	}
}