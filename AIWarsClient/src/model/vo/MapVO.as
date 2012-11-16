package model.vo
{
	public class MapVO
	{
		
		public static const EMPTY:int = 0;
		
		//Array[width][height]
		public var array:Array;
		public var width:int;
		public var height:int;
		
		public function MapVO(width:int, height:int, array:Array)
		{
			this.array = array;
			this.height = height;
			this.width = width;
		}
		
					
	}
}