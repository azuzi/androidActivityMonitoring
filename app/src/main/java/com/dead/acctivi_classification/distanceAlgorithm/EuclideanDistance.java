package com.dead.acctivi_classification.distanceAlgorithm;

public class EuclideanDistance implements DistanceAlgorithm {
    @Override
    public double calculateDistance(double vX1, double vY1, double vZ1,double sdX1, double sdY1, double sdZ1,
                                     double vX2, double vY2, double vZ2,double sdX2, double sdY2, double sdZ2) {

        double vxSquare = Math.pow(vX1 - vX2, 2);
        double vySquare = Math.pow(vY1 - vY2, 2);
        double vzSquare = Math.pow(vZ1 - vZ2, 2);
        double sdxSquare = Math.pow(sdX1 - sdX2, 2);
        double sdySquare = Math.pow(sdY1 - sdY2, 2);
        double sdzSquare = Math.pow(sdZ1 - sdZ2, 2);

        double distance = Math.sqrt(vxSquare + vySquare+ vzSquare + sdxSquare + sdySquare + sdzSquare);
        return distance;
    }
}
