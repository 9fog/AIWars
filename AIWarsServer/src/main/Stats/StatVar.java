package main.Stats;

public class StatVar {
	private final String _varName;
	private double _sum, _avg, _max, _min;
	private int    _count;
	
	public StatVar(String name) {
		_varName = name;
		_sum     = 0.0;
		_avg     = 0.0;
		_max     = 0.0;
		_min     = Double.MAX_VALUE;
		_count   = 0;
	}
	
	public StatVar(String name, double sum, double avg, double min, double max, int count) {
		_varName = name;
		_sum     = sum;
		_avg     = avg;
		_min     = min;
		_max     = max;
		_count   = count;
	}
	
	public void addValue(double val) {
		_sum += val;
		_avg = (_avg*_count + val)/(_count+1);
		if (_min>val) {
			_min = val;
		}
		if (_max<val) {
			_max = val;
		}		
		_count++;
	}
	
	public String getName() {return _varName;}
	public double getSum() {return _sum;}
	public double getAvg() {return _avg;}
	public double getMin() {return _min;}
	public double getMax() {return _max;}
	public int getCount() { return _count;}
}
