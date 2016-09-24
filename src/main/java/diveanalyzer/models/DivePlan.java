package diveanalyzer.models;

import java.util.LinkedList;
import java.util.List;

public class DivePlan {

    private LinkedList<DivePoint> theDive = new LinkedList<>();
    private LinkedList<OverSatPoint> distanceToOverSaturation = new LinkedList<>();

    public DivePlan() {
    }

    public void addPoint(double _depth, double _date){
        DivePoint dp = new DivePoint(_depth, _date);
        theDive.add(dp);
    }

    public void addPoint(DivePoint _point){
        theDive.add(_point);
    }
    public void setDistToOverSaturation(List<Double> _distances, double _samplingPeriod){
        double date=0;
        for(Double dist : _distances) {
            distanceToOverSaturation.add(new OverSatPoint(dist, date));
            date+=_samplingPeriod;
        }
    }

    public LinkedList<DivePoint> getTheDive() {
        return theDive;
    }

    public LinkedList<OverSatPoint> getDistanceToOverSaturation() {
        return distanceToOverSaturation;
    }
}
