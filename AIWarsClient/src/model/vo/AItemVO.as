package model.vo
{
	public class AItemVO extends Object
	{
		public function AItemVO()
		{
			super();
		}
		
		public var id:int;
		public var x:Number;
		public var y:Number;
		
		public function get clone():AItemVO
		{
			return null;
		}
		
		public function calculateStep():void
		{}
		
		
	}
}