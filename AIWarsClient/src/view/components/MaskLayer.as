package view.components
{
	import flash.display.GradientType;
	import flash.display.Sprite;
	import flash.geom.Matrix;
	
	import model.Space;
	import model.vo.UnitVO;
	import model.vo.UserVO;
	
	import mx.core.UIComponent;
	
	public class MaskLayer extends UIComponent
	{
		
		public var mymask:Sprite = new Sprite();
		
		public function MaskLayer()
		{
			super();
			addChild(mymask);
		}
		
		public function update(space:Space, len:int, maskList:Array):void
		{
			mymask.graphics.clear();
			if (space)
				for each (var unit:UnitVO in space.units)
				{
					if (find(maskList,unit.userId))
						addCircle(unit.x * len+len/2, unit.y * len+len/2, unit.type.lookRange*len);
				}
		}
		
		public function find(maskList:Array, id:int):Boolean
		{
			for each (var i:UserVO in maskList) 
			{
				if (i.id == id) return true;
			}
			return false;
		}
		
		private function addCircle(x:int,y:int,r:int):void
		{
			var matrix:Matrix = new Matrix();
			matrix.createGradientBox(2*r,2*r,0,-r+x,-r+y);
			mymask.graphics.lineStyle();
			mymask.graphics.beginGradientFill(GradientType.RADIAL,[0xFFFFFF,0xFFFFFF],[1,0],[125,255],matrix);
			mymask.graphics.drawCircle(x,y,r);
			mymask.graphics.endFill();
		}
	}
}