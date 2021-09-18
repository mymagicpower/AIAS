package ai.djl.examples.detection.domain;

import ai.djl.modality.cv.output.Point;

import java.util.List;

public class Landmark {
    private List<Point> points;

    public Landmark(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
