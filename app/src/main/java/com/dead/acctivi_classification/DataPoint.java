package com.dead.acctivi_classification;

public class DataPoint {

    private double mX, vX, sdX, mY,vY,sdY, mZ, vZ, sdZ;
    private Category category;

    public DataPoint(double vX, double sdX, double sdY,double vY, double vZ, double sdZ, Category category){
        this.vX=vX ;
        this.sdX=sdX;
        this.sdY = sdY;
        this.vY =vY ;
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
        this.mX=dataPoint.getMX();

        this.vX = dataPoint.getVX();
        this.sdX=dataPoint.getSDX();
        this.category = dataPoint.getCategory();
    }

    public double getMX() {return mX;
    }

    public double getSDX() {return sdX;
    }

    public double getVX() {return vX;
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
    public void setMX(double mx){
        this.mX = mx;
    }
    public void setVX(double vx) {
        this.vX = vx;
    }
    public void setSDX(double sdx) {
        this.sdX = sdx;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
