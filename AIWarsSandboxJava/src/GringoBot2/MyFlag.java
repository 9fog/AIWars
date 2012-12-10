package GringoBot2;

public class MyFlag implements Comparable<MyFlag>{
	public int id;
	public int x;
	public int y;
	public int range;
	public boolean isMine = false;
	
	public MyFlag(int id, int x, int y, int range) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.range = range;
	}	
	
	public int compareTo(MyFlag tmp) {
	    if(this.range < tmp.range) {
	      return -1;
	    } else {
	      return 1;
	    }
	}
}
