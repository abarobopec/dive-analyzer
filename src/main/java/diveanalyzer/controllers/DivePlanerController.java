package diveanalyzer.controllers;

import diveanalyzer.diveplanner.DiveStopModel;
import diveanalyzer.diveplanner.Stop;
import diveanalyzer.models.DivePlan;
import diveanalyzer.models.DivePoint;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DivePlanerController {

    @CrossOrigin(value="*")
    @ResponseBody
    @RequestMapping(value="/dive-plan", method= RequestMethod.POST, produces="application/json")
    DivePlan computeDivePlan(@RequestBody DivePoint[] dive){
        DivePlan res = new DivePlan();
        DiveStopModel model = new DiveStopModel();
        for(DivePoint point: dive){
            res.addPoint(point);
        }
        for(int i = 1 ; i < res.getTheDive().size() ; i++) {
            double fromDepth = res.getTheDive().get(i-1).getDepth();
            double toDepth = res.getTheDive().get(i).getDepth();
            double duration = res.getTheDive().get(i).getDate()-res.getTheDive().get(i-1).getDate();
            model.computeSaturation(fromDepth, toDepth, duration);
        }

        List<Stop> stops = model.computeStopsForGoingTo(0.);
        if(stops.size() > 0)
        {
            for(Stop stop : stops) {
                res.getTheDive().add(new DivePoint(stop.getDepth(), stop.getDateBegin()));
                res.getTheDive().add(new DivePoint(stop.getDepth(), stop.getDateBegin()+stop.getDuration()));
            }
            // On fait les dernier 3 mètres en 30 secondes :
            res.getTheDive().add(new DivePoint(0, res.getTheDive().getLast().getDate()+0.5));
        }else {
            // pas de paliers :
            double upTime = model.getVerticalSpeedUp()/res.getTheDive().getLast().getDepth();
            res.getTheDive().add(new DivePoint(0, res.getTheDive().getLast().getDepth()+upTime));
        }
        res.setDistToOverSaturation(model.getDirectorDistancesToCriticalSaturation(), model.getSamplingPeriod());

        return res;
    }
}
