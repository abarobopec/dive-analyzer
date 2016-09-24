package diveanalyzer.diveplanner;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class ModelTester {
	
	@Test
	public void onASafeDiveThereIsNoNeedToDoStops() {
		DiveStopModel model = new DiveStopModel();
		model.computeSaturation(0, 15, 0.5);	// On descend à 15 mètres en .5 minutes
		model.computeSaturation(15, 15, 20);	// On reste  20 minutes à 15 mètres		
		model.computeSaturation(15, 0, 1.5);	// on remonte à 0 mètres en 1.5 minutes.
		List<Double> distanceToMostCriticalSaturation = model.getDirectorDistancesToCriticalSaturation();
		for(Double distance : distanceToMostCriticalSaturation) {
			System.out.print(distance + " ");
			Assert.assertEquals(distance < 0, true);	// on vérifie que l'on a jamais été audelà de la saturation critique
		}		
	}
	@Test
	public void whenStopsAreRequiredModelDetectIt() {
		DiveStopModel model = new DiveStopModel();
		model.computeSaturation(0, 40, 0.5);	
		model.computeSaturation(40, 40, 20);			
		model.computeSaturation(40, 0, 3.);	
		
		List<Double> distanceToMostCriticalSaturation = model.getDirectorDistancesToCriticalSaturation();
		boolean stops_are_needed = false;
		for(Double distance : distanceToMostCriticalSaturation) {
			if(distance < 0) stops_are_needed = true;
		}
		Assert.assertEquals(stops_are_needed, true);
	}
	
	@Test
	public void directedCompartimentIsWellDetected() {
		DiveStopModel model = new DiveStopModel();
		Compartiment comp1 = mock(Compartiment.class);
		Compartiment comp2 = mock(Compartiment.class);
		Compartiment comp3 = mock(Compartiment.class);
		List<Compartiment> allComps = new LinkedList<Compartiment>();
		allComps.add(comp1); allComps.add(comp2); allComps.add(comp3);
		
		when(comp1.getDistanceToCriticalSaturation()).thenReturn(new Double(-1.0));
		when(comp2.getDistanceToCriticalSaturation()).thenReturn(new Double(-1.5));
		when(comp3.getDistanceToCriticalSaturation()).thenReturn(new Double(1.0));
		model.setCompartiments(allComps);
		model.computeSaturation(40, 40, 21);
		Assert.assertEquals(comp3, model.getDirector());
	}
	
	@Test
	public void whenGoUpAfterABigDiveStopsAreDetectedAtGoodDepth() {
		DiveStopModel model = new DiveStopModel();
		model.computeSaturation(40, 40, 25); 
		model.computeSaturation(40, 12, 2.);
		checkThatStopsWasNeeded(false, model);
		Assert.assertEquals(model.stopsAreNeeded(6.), false);
		Assert.assertEquals(model.getCurrentDepth(), 6, 0.0001);
		Assert.assertEquals(model.stopsAreNeeded(3.), true);	// on doit faire un stop à 6 mètres, donc quand on passe de 6 à 3
		Assert.assertEquals(model.getCurrentDepth(), 6, 0.0001);
	}
	
	private void checkThatStopsWasNeeded(boolean _needed, DiveStopModel _model) {
		boolean stops_are_needed = false;
		List<Double> distanceToMostCriticalSaturation = _model.getDirectorDistancesToCriticalSaturation();
		for(Double distance : distanceToMostCriticalSaturation) {
			if(distance > 0) stops_are_needed = true;
		}		
		Assert.assertEquals(stops_are_needed, _needed);		
	}
	@Test
	public void whenGoUpSaturationGradiantIsAlwaysSafe() {
		DiveStopModel model = new DiveStopModel();
		model.computeSaturation(0, 32, 1);		// On descend à 32 mètres en 1 minutes
		model.computeSaturation(32, 32, 39);	// On reste 39 minutes à 32 mètres		
		model.computeStopsForGoingTo(0);		// On remonte à 0 mètre
		List<Double> distanceToMostCriticalSaturation = model.getDirectorDistancesToCriticalSaturation();
		for(Double distance : distanceToMostCriticalSaturation) {
			Assert.assertEquals(distance < 0, true);	// on vérifie que l'on a jamais été audelà de la saturation critique
		}
	}
	@Test
	public void testModelMN90GenerateGoodStops() {
		DiveStopModel model = new DiveStopModel();
		model.computeSaturation(40, 40, 25);	
		List<Stop> stops = model.computeStopsForGoingTo(0.);	// Paliers après 25 minutes à 40 Mètres :
		Assert.assertEquals(stops.get(0).getDepth(), 6, 0.00005);
		Assert.assertEquals(stops.get(0).getDuration(), 2, 0.00005);
		Assert.assertEquals(stops.get(1).getDepth(), 3, 0.00005);
		Assert.assertEquals(stops.get(1).getDuration(), 19, 0.00005);
	}
}
