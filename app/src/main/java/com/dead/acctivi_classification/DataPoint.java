package com.dead.acctivi_classification;

public class DataPoint {

    private double mY,vY,sdY, mZ, vZ, sdZ;
    private Category category;

    public DataPoint(double mY, double vY, double sdY,double mZ, double vZ, double sdZ, Category category){
        this.mY =mY ;
        this.vY = vY;
        this.sdY = sdY;
        this.mZ =mZ ;
        this.vZ = vZ;
        this.sdZ = sdZ;
        this.category = category;
    }

    public DataPoint(DataPoint dataPoint){
        this.mY = dataPoint.getMY();
        this.mZ = dataPoint.getMZ();
        this.vY = dataPoint.getVY();
        this.vZ = dataPoint.getVZ();
        this.sdY = dataPoint.getSDY();
        this.sdZ = dataPoint.getSDZ();
        this.category = dataPoint.getCategory();
    }


    public double getVZ() {
        return vZ;
    }

    public double getSDZ() {
        return sdZ;
    }

    public double getVY() {
        return vY;
    }

    public double getMZ() {
        return mZ;
    }

    public double getMY() {
        return mY;
    }

    public double getSDY() {
        return sdY;
    }

    public void setMY(double my){
        this.mY = my;
    }
    public void setVY(double vy) {
        this.vY = vy;
    }
    public void setSDY(double sdy) {
        this.sdY = sdy;
    }
    public void setMZ(double mz){
        this.mZ = mz;
    }
    public void setVZ(double vz) {
        this.vZ = vz;
    }
    public void setSDZ(double sdz) {
        this.sdZ = sdz;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
