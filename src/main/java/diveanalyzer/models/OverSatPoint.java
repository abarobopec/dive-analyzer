package diveanalyzer.models;

public class OverSatPoint {
    private double dist;
    private double date;

    public OverSatPoint(){
        dist = 0;
        date = 0;
    }
    public OverSatPoint(double _dist, double _date) {
        dist = _dist;
        date = _date;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public double getDate() {
        return date;
    }

    public void setDate(double date) {
        this.date = date;
    }
}
