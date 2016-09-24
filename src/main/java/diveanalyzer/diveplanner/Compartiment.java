package diveanalyzer.diveplanner;

import java.util.LinkedList;
import java.util.List;

public class Compartiment {
	private double m_period;
	private double m_pp_n2_at_1bar;
	private double m_current_pp_n2;
	private double m_critcal_saturation;
	private double m_current_gradiant_pp_n2;
	private double m_current_depth;
	private double m_current_date;
	private List<Double> m_all_saturations = new LinkedList<Double>();
	private List<Double> m_all_gradiants = new LinkedList<Double>();

	private double m_pushed_pp_n2;
	private double m_pushed_gradiant_pp_n2;
	private List<Double> m_pushed_all_saturations = new LinkedList<Double>();
	private List<Double> m_pushed_all_gradiants = new LinkedList<Double>();
	private boolean m_pushed;
	public Compartiment(double _period, double _critcal_saturation) {
		m_period = _period;
		m_current_date = 0;
		m_pushed = false;
		m_critcal_saturation = _critcal_saturation;
		m_pp_n2_at_1bar = .8;
		m_current_pp_n2 = m_pp_n2_at_1bar;
		m_all_saturations.add(m_current_pp_n2);
		m_all_gradiants.add((double) 0);
	}
	public double computeSaturation(double _depth, double _duration) {
		// Equation de John Scott Haldane :
		m_current_date += _duration;
		m_current_depth = _depth;
		double pp_n2_at_depth = m_pp_n2_at_1bar*(1+_depth/10.);
		m_current_gradiant_pp_n2 = (pp_n2_at_depth-m_current_pp_n2);
		m_current_pp_n2 += m_current_gradiant_pp_n2*(1-Math.pow(.5, _duration/m_period));
		m_all_saturations.add(m_current_pp_n2);
		m_all_gradiants.add(m_current_gradiant_pp_n2);
		return m_current_pp_n2;
	}
	public double computeSaturation(double _start_depth, double _end_depth, double _duration, double _dt) {
		// sample hold sur l'equation de JS Haldane :
		for(double t = 0 ; t < _duration ; t+=_dt)
		{
			double depth = _start_depth+t*(_end_depth-_start_depth)/_duration;
			computeSaturation(depth, _dt);
		}
		return m_current_pp_n2;
	}
	public double getNextExactDepthStop() {
		return (m_current_pp_n2/m_critcal_saturation-1)*10;
	}
	public double getNextDepthStop() {
		double exact_depth = getNextExactDepthStop();
		return Math.ceil(exact_depth/3)*3;
	}

	/*
	 * _speed : En mètre par minute
	 */
	public List<Stop> computeStopsToDepth(double _from_depth, double _to_depth, double _speed) {
		// On est à _from_depth mètres
		List<Stop> stops = new LinkedList<Stop>();
		double nextStop = getNextDepthStop();
		// on regarde à quelle profondeur est le prochain palier (nextStop)
		// on remonte à _speed m/minutes à la profondeur de palier :
		computeSaturation(_from_depth, nextStop, (_from_depth-nextStop)/_speed, 1./60.);
		// on regarde si il faut toujours faire le palier :
		nextStop = getNextDepthStop();
		if(nextStop > 0)
		{
			Stop currStop = new Stop(nextStop, 1, m_current_date);
			stops.add(currStop);
			while(nextStop > _to_depth) {
				// tant qu'il y a un palier entre nous est _to_depth :
				if(nextStop < currStop.getDepth())
				{
					// si on doit remonter au palier suivant, on remonte en une minute :
					computeSaturation(currStop.getDepth(), nextStop, 1., 1./60.);
					currStop = new Stop(nextStop, 1, m_current_date);
					stops.add(currStop);
				}
				// on fait une minute de palier :
				computeSaturation(currStop.getDepth(), 1);
				// on regarde si il faut tojours faire un palier :
				nextStop = getNextDepthStop();
			}
		}
		return stops;
	}
	public List<Double> getAllSaturations() {
		return m_all_saturations;
	}
	public List<Double> getAllGradients() {
		return m_all_gradiants;
	}
	
	/*!
	 * Si cette distance passe positive, alors il faut faire un palier. Tant qu'elle est négative, on peut remonter
	 */
	public double getDistanceToCriticalSaturation() {
		// Il s'agit de la tension du tissus divisé par la pression ambiante moins la saturation critique.
		return (m_current_pp_n2/(1+m_current_depth/10)-m_critcal_saturation);
	}
	
	public void pushState() {
		m_pushed = true;
		m_pushed_gradiant_pp_n2 = m_current_gradiant_pp_n2;
		m_pushed_pp_n2 = m_current_pp_n2;
		m_pushed_all_gradiants = new LinkedList<Double>(m_all_gradiants);
		m_pushed_all_saturations = new LinkedList<Double>(m_all_saturations);
	}
	
	public void popState() {
		assert(m_pushed == true);
		m_current_gradiant_pp_n2 = m_pushed_gradiant_pp_n2;
		m_current_pp_n2 = m_pushed_pp_n2;
		m_all_gradiants = new LinkedList<Double>(m_pushed_all_gradiants);
		m_all_saturations = new LinkedList<Double>(m_pushed_all_saturations);
	}
}
