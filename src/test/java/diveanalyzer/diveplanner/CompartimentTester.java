package diveanalyzer.diveplanner;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class CompartimentTester{
	@Test
	public void saturationIsCoherentAfterAPerfectlySquaredDive() {
		Compartiment compartiment = new Compartiment(7., 2.54);
		// depth, and duration :
		double saturation = compartiment.computeSaturation(40, 21);
		double expected = 3.6;
		Assert.assertEquals(saturation, expected, 0.0005);
	}
	@Test
	public void someStopsAreNeededForABigDive() {
		Compartiment compartiment = new Compartiment(7., 2.54);
		compartiment.computeSaturation(40, 21);
		double depth = compartiment.getNextExactDepthStop();
		Assert.assertEquals(depth > 3, true);
		Assert.assertEquals(depth < 6, true);
	}
	@Test
	public void someStopDepthIsTheGoodFactorOf3() {
		Compartiment compartiment = new Compartiment(7., 2.54);
		compartiment.computeSaturation(40, 21);
		double stop_depth = compartiment.getNextDepthStop();
		Assert.assertEquals(stop_depth, 6, 0.0005);
	}
	@Test
	public void ifWeGoDownSlowlyWeAreLessSaturated() {
		Compartiment compartiment1 = new Compartiment(7., 2.54);
		double quick_saturation = compartiment1.computeSaturation(40, 21);
		
		Compartiment compartiment2 = new Compartiment(7., 2.54);
		double slow_saturation = compartiment2.computeSaturation(0, 40, 21, 1./60.);	
		System.out.println("Slow saturation = " + slow_saturation);
		System.out.println("Quick saturation = " + quick_saturation);
		Assert.assertEquals(slow_saturation<quick_saturation, true);
	}
	@Test
	public void whenGoUpStopTimesAreSafe() {
		Compartiment compartiment1 = new Compartiment(7., 2.54);
		compartiment1.computeSaturation(40, 21);
		List<Stop> stops = compartiment1.computeStopsToDepth(40, 0, 12);
		List<Double> allGrad = compartiment1.getAllGradients();
		for(Double saturation : allGrad) {
			Assert.assertEquals(saturation.doubleValue() > -2.54, true);
		}
		for(Stop stop : stops) {
			System.out.println("Stop à -" + stop.getDepth() + "m : " + stop.getDuration() + " minutes" );
		}
	}
	@Test
	public void whenGoUpToFastWeAreOverStaturated(){
		Compartiment compartiment1 = new Compartiment(7., 2.54);
		compartiment1.computeSaturation(40, 21);
		compartiment1.computeSaturation(0., 0.01);
		Assert.assertEquals(compartiment1.getDistanceToCriticalSaturation()>0, true);
		Assert.assertEquals(compartiment1.getDistanceToCriticalSaturation(), 3.6-2.54, 0.1);
	}
}
