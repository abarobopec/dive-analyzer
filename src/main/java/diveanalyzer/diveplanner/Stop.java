package diveanalyzer.diveplanner;

public class Stop {
	private double m_depth;
	private double m_date_begin_min;
	private double m_duration_min;
	
	public Stop(double _depth, double _duration_min, double _date_begin){
		m_depth = _depth;
		m_duration_min = _duration_min;
		m_date_begin_min = _date_begin;
	}
	public double getDepth(){return m_depth;}
	public double getDateBegin(){return m_date_begin_min;}
	public double getDuration(){return m_duration_min;}
	public void addOneMinute() {
		m_duration_min++;
	}
}
