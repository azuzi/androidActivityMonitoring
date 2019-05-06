package com.dead.acctivi_classification.distanceAlgorithm;

public class EuclideanDistance implements DistanceAlgorithm {
    @Override
    public double calculateDistance(double mY1, double vY1, double sdY1,double mZ1, double vZ1, double sdZ1,
                                     double mY2, double vY2, double sdY2,double mZ2, double vZ2, double sdZ2) {
        double mySquare = Math.pow(mY1 - mY2, 2);
        double vySquare = Math.pow(vY1 - vY2, 2);
        double sdySquare = Math.pow(sdY1 - sdY2, 2);

        double mzSquare = Math.pow(mZ1 - mZ2, 2);
        double vzSquare = Math.pow(vZ1 - vZ2, 2);
        double sdzSquare = Math.pow(sdZ1 - sdZ2, 2);

        double distance = Math.sqrt(mySquare + vySquare+ sdySquare + mzSquare + vzSquare+ sdzSquare);
        return distance;
    }
}
