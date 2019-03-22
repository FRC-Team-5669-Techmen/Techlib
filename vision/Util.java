package edu.boscotech.techlib.vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.MatOfPoint;

public class Util {
    /**
     * Takes a list of hulls and figures out which one is the most line-shaped,
     * then returns that.
     */
    public static Line findLineFromHulls(List<MatOfPoint> hulls) {
        // Calculates lines for each hull, and figures out how line-ey each
        // hull is.
        if (hulls.size() == 0) {
            return null;
        }
        double[] penalties = new double[hulls.size()];
        Line[] lines = new Line[hulls.size()];
        for (int index = 0; index < hulls.size(); index++) {
            MatOfPoint hull = hulls.get(index);
            Line line = linearRegression(hull);
            lines[index] = line;
            
            double averageDistance = 0.0;
            double[] distances = new double[hull.width()];
            for (int pointIndex = 0; pointIndex < hull.width(); pointIndex++) {
                double distance = line.distanceToPoint(hull.get(pointIndex, 0));
                averageDistance += distance;
                distances[pointIndex] = distance;
            }
            averageDistance /= hull.width();
            double penalty = 0.0;
            for (int pointIndex = 0; pointIndex < hull.width(); pointIndex++) {
                penalty += Math.abs(distances[pointIndex] - averageDistance);
            }
            penalties[index] = penalty;
        }
        double minPenalty = penalties[0];
        int minPenaltyIndex = 0;
        for (int lineIndex = 1; lineIndex < lines.length; lineIndex++) {
            if (penalties[lineIndex] < minPenalty) {
                minPenalty = penalties[lineIndex];
                minPenaltyIndex = lineIndex;
            }
        }
        return lines[minPenaltyIndex];
    }

    /**
     * Finds the line of best fit for the data using 
     */
    public static Line linearRegression(MatOfPoint points) {
        double xMean = 0.0, yMean = 0.0;
        for (int index = 0; index < points.width(); index++) {
            double[] point = points.get(index, 0);
            xMean += point[0];
            yMean += point[1];
        }
        xMean /= points.width();
        yMean /= points.width();

        double xxSum = 0.0, xySum = 0.0;
        for (int index = 0; index < points.width(); index++) {
            double[] point = points.get(index, 0);
            point[0] -= xMean;
            point[1] -= yMean;
            xxSum += point[0] * point[0];
            xySum += point[0] * point[1];
        }

        double slope = xySum / xxSum;
        return new Line(slope, xMean, yMean);
    }
}