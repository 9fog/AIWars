package view.items
{
	import model.vo.AItemVO;
	
	import spark.components.Group;
	
	import view.SpaceMediator;
	
	public class AView extends Group
	{
		protected var _vo:AItemVO;
		protected var _color:int;
		
		public function get vo():AItemVO
		{
			return _vo;
		}
		
		public function set vo(value:AItemVO):void
		{
			_vo = value;
		}
		
		public function update(value:AItemVO):void
		{
			_vo = value;
		}
		
		public function set color(value:int):void
		{
			_color = value;
		}
		
		public function AView()
		{
			width = 50;
			height = 50;
			alpha = 1;
			super();
		}
	}
}