package edu.boscotech.techlib.vision;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

public class AutoCenterPipeline extends BrightHullPipeline {
    private double centerX = 0.0;
    private int MINIMUM_ROWS = 100;
    private int MINIMUM_POINTS = 70;

    @Override
    public void process(Mat source0) {
        super.process(source0);
        int[] pointsPerRow = new int[source0.height()];
        double[] averagePerRow = new double[source0.height()];

        for (MatOfPoint hull : convexHullsOutput()) {
            for (int pointIndex = 0; pointIndex < hull.width(); pointIndex++) {
                double[] point = hull.get(pointIndex, 0);
                int yInt = (int) point[1];
                pointsPerRow[yInt]++;
                averagePerRow[yInt] += point[0];
            }
        }

        int totalPoints = 0;
        double finalAverage = 0.0;
        for (int y = source0.height() - 1; y >= 0; y--) {
            totalPoints += pointsPerRow[y];
            finalAverage += averagePerRow[y];
            if (
                source0.height() - y >= MINIMUM_ROWS 
                && totalPoints >= MINIMUM_POINTS
            ) {
                break;
            }
        }
        finalAverage /= totalPoints;

        centerX = finalAverage / source0.width() * 2.0 - 1.0;
    }

    /**
     * @return the centerX calculated from the pipeline.
     */
    public double getCenterX() {
        return centerX;
    }
}