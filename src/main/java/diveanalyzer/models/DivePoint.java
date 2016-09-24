package diveanalyzer.models;

public class DivePoint {
    private double depth;
    private double date;

    public DivePoint() {
        depth = 0;
        date = 0;
    }

    public DivePoint(double _depth, double _date) {
        depth = _depth;
        date = _date;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getDate() {
        return date;
    }

    public void setDate(double date) {
        this.date = date;
    }
}
