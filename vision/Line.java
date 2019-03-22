package edu.boscotech.techlib.vision;

public class Line {
    private double x1, x2, y1, y2;

    public Line(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
    }

    public Line(double m, double x1, double y1) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x1 + 10.0;
        this.y2 = this.x2 * m;
    }

    public Line(double m, double b) {
        this.x1 = 0.0;
        this.y1 = b;
        this.x2 = 10.0;
        this.y2 = this.x2 * m;
    }

    public double getSlope() {
        if (x2 == x1) return 1e10; // A really big number for a vertical line slope.
        return (y2 - y1) / (x2 - x1);
    }

    public double getYIntercept() {
        return y1 - getSlope() * x1;
    }

    public double distanceToPoint(double x, double y) {
        // Given line ax+by+c and point {s, t}, the distance is:
        // |a(s) + b(t) + c| / sqrt(a^2 + b^2)
        // Point-slope form is y = -ax - c, so a = -m, b = 1, c = -b (b as in intercept)
        // So final equation is:
        // |t -m(s)| / sqrt(m^2 + 1)
        double m = getSlope();
        return Math.abs(y - m * x) / Math.sqrt(m * m + 1);
    }

    public double distanceToPoint(double[] point) {
        return distanceToPoint(point[0], point[1]);
    }
}