package model.parser
{
	import model.Space;

	public class PC
	{
		public var name:String = "ParserCommand";
		public var data:String = "";
		
		public function PC(name:String, data:String)
		{
			this.name = name;
			this.data = data;
		}
		/*
		public function get space():Space
		{
			return Space.instance;
		}
		*/
		
		public function parse(space:Space):String
		{
		//this is abstract do no use it
			return "";
		}
	}
}