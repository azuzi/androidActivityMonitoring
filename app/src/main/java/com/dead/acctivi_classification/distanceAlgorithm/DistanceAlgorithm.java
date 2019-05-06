package com.dead.acctivi_classification.distanceAlgorithm;

public interface DistanceAlgorithm {
    public double calculateDistance(double mY1, double vY1, double sdY1,double mZ1, double vZ1, double sdZ1,
                                    double mY2, double vY2, double sdY2,double mZ2, double vZ2, double sdZ2);
}