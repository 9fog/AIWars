package view.components
{
	import com.greensock.layout.ScaleMode;
	
	import flash.display.MovieClip;
	import flash.display.Sprite;
	
	import model.Space;
	import model.vo.MapVO;
	
	import mx.core.UIComponent;
	
	import spark.components.Image;
	
	import view.items.MapCellView;
	
	
	public class MapPanel extends UIComponent
	{
		
		public var _grid:Sprite = new Sprite();
		public var _image:Image = new Image();
		
		public function set showGrid(value:Boolean):void
		{
			_grid.visible = value;
		}
		
		public function MapPanel()
		{
			super();
		}
		
		public function init(space:Space, len:Number):void
		{
			_image.width = space.map.width *len;
			_image.height = space.map.height *len;
			_image.source = "fon.png";
			_image.scaleMode = ScaleMode.STRETCH;
			_image.smooth = true;
			addChild(_image);
			
			_grid.graphics.lineStyle(1, 0xc0c0c0, 0.2);
			for (var i:int = 0; i <= space.map.width;i++)
			{
				_grid.graphics.moveTo(i * len,0);
				_grid.graphics.lineTo(i * len,space.map.height *len);
			}
			for (var j:int = 0; j <= space.map.height;j++)
			{
				_grid.graphics.moveTo(0, j * len);
				_grid.graphics.lineTo(space.map.width *len, j * len);
			}
			
			
			addChild(_grid);
			
			for ( i = 0; i < space.map.width;i++)
			{
				for ( j = 0; j < space.map.height;j++)
				{
					var cell:MapCellView = new MapCellView();
					cell.width = len;
					cell.height = len;
					cell.x = i * len;
					cell.y = j * len;
					
					if(space.map.array[j][i] != MapVO.EMPTY)
					{
						cell.empty = false;
						addChild(cell);
					}
				}
			}
		}
	}
}