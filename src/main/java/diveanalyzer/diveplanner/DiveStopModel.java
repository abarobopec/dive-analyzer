package diveanalyzer.diveplanner;

import java.util.LinkedList;
import java.util.List;

public class DiveStopModel {
	private double m_current_depth;
	private double m_current_date;
	private final double m_verical_up_speed = 15.;	// en mètres par minutes
	private final double m_sampling_period = 1./60.;
	private List<Compartiment> m_all_compartiments;
	private List<Double> m_directedDistancesToCriticalSaturation;
	private Compartiment m_director;
	private List<Double> m_directedDistancesToCriticalSaturation_save;	
	private double m_depth_before;
	public double getVerticalSpeedUp(){return m_verical_up_speed;}
	public List<Double> getDirectorDistances(){return m_directedDistancesToCriticalSaturation;}
	
	public DiveStopModel() {
		m_current_date = 0;
		m_directedDistancesToCriticalSaturation = new LinkedList<Double>();
		m_all_compartiments = new LinkedList<Compartiment>();
		m_all_compartiments.add(new Compartiment(5, 2.72));
		m_all_compartiments.add(new Compartiment(7, 2.54));
		m_all_compartiments.add(new Compartiment(10, 2.38));
		m_all_compartiments.add(new Compartiment(15, 2.20));
		m_all_compartiments.add(new Compartiment(20, 2.04));
		m_all_compartiments.add(new Compartiment(30, 1.82));
		m_all_compartiments.add(new Compartiment(40, 1.68));
		m_all_compartiments.add(new Compartiment(50, 1.61));
		m_all_compartiments.add(new Compartiment(60, 1.58));
		m_all_compartiments.add(new Compartiment(80, 1.56));
		m_all_compartiments.add(new Compartiment(100,1.55));
		m_all_compartiments.add(new Compartiment(120,1.54));
	}
	
	public double getSamplingPeriod(){
		return m_sampling_period;
	}
	
	private void pushState() {
		m_depth_before = m_current_depth;
		m_directedDistancesToCriticalSaturation_save = new LinkedList<Double>(m_directedDistancesToCriticalSaturation);
		for(Compartiment compartiment : m_all_compartiments) {
			compartiment.pushState();
		}
	}
	
	private void popState() {
		m_current_depth = m_depth_before;
		m_directedDistancesToCriticalSaturation = new LinkedList<Double>(m_directedDistancesToCriticalSaturation_save);
		for(Compartiment compartiment : m_all_compartiments) {
			compartiment.popState();
		}
	}
	
	public double getCurrentDepth() {
		return m_current_depth;
	}
	public Compartiment getDirector(){
		return m_director;
	}
	public void setCompartiments(List<Compartiment> _allCompartiments) {
		m_all_compartiments = _allCompartiments;
	}

	public void computeSaturation(double _from_depth, double _to_depth, double _duration) {
		// sample hold sur l'equation de JS Haldane :
		if(_duration <= 2*m_sampling_period) {
			// si la durrée est inférieure à la période d'échantillonage, ce n'est pas tout à fait la même chose :
			m_current_depth = _to_depth;			
			for(Compartiment compartiment : m_all_compartiments) {
				compartiment.computeSaturation(m_current_depth, _duration);
			}
			storeDirectorDistanceToCriticalSaturation();
		}else
		{
			for(double t = 0 ; t < _duration ; t+=m_sampling_period) {
				m_current_depth = _from_depth+t*(_to_depth-_from_depth)/_duration;
				for(Compartiment compartiment : m_all_compartiments) {
					compartiment.computeSaturation(m_current_depth, 1./60.);
				}
				storeDirectorDistanceToCriticalSaturation();
			}
		}
		m_current_date += _duration;
	}

	private void storeDirectorDistanceToCriticalSaturation() {
		double maxSaturation = m_all_compartiments.get(0).getDistanceToCriticalSaturation();
		for(Compartiment compartiment : m_all_compartiments) {
			if(compartiment.getDistanceToCriticalSaturation() > maxSaturation) {
				maxSaturation = compartiment.getDistanceToCriticalSaturation();
				m_director = compartiment;
			}
		}
		m_directedDistancesToCriticalSaturation.add(maxSaturation);
	}

	public List<Double> getDirectorDistancesToCriticalSaturation() {
		return m_directedDistancesToCriticalSaturation;
	}

	/*!
	 * détermine si un palier est nécessaire pour aller à la profondeur _to_depth. 
	 * Si un palier est nécessaire, alors l'état des compartiment n'est pas touché, si non, on met 
	 * à jour l'état des compartiments et la profondeur actuelle sera _to_depth
	 */
	public boolean stopsAreNeeded(double _to_depth) {			
		// On cherche à monter à la prochaine profondeur où un palier est possible (facteur de 3) :
		double depth = getNextStopableDepth();
		while(depth >= _to_depth) {
			// On sauvegarde l'état des compartiments et du modèle :
			pushState();
			// tant que la prochaine profondeur où un palier est possibles et > à la profondeur objectif :
			// On calcule la saturation de tous les compartiments à une vitesse de remontée de 12 m / min :
			computeSaturation(m_current_depth, depth,  (m_current_depth-depth)/m_verical_up_speed);
			// Après cette remontée, on regarde si on est passé en sur saturation sur le compartiment directeur :
			if(m_director.getDistanceToCriticalSaturation() >= 0)
			{
				// si c'est le cas alors un palier est nécessaire, donc on remet tout l'état du système comme
				// avant de rentrer dans la fonction :
				popState();
				return true;
			}
			depth = getNextStopableDepth();
		}
		// Si non, pas de palier à faire, donc le système est remonté à _to_depth
		return false;
	}

	private double getNextStopableDepth() {
		return Math.ceil((m_current_depth-3)/3)*3;
	}

	public List<Stop> computeStopsForGoingTo(double _depth) {
		List<Stop> stops = new LinkedList<Stop>();
		while(stopsAreNeeded(_depth)) {
			// Tant qu'il y a des palier à faire : 
			// on regarde si la profondeur actuelle est celle du palier en cours :
			//  si c'est le cas on ajoute une minute de palier, 
			//  si non (ou si la liste est vide), on crée un nouveau palier avec une minute dedans :
			if(stops.isEmpty()) {
				stops.add(new Stop(m_current_depth, 1, m_current_date));
				computeSaturation(m_current_depth, m_current_depth, 1);
			}else if(Math.abs(stops.get(stops.size()-1).getDepth()-m_current_depth) < .1) {
				// même profondeur, on ajoute une minute au palier :
				computeSaturation(m_current_depth, m_current_depth, 1);
				stops.get(stops.size()-1).addOneMinute();
			}else {
				stops.add(new Stop(m_current_depth, 1, m_current_date));
				computeSaturation(m_current_depth, m_current_depth, 1);				
			}
		}
		return stops;
	}
}
