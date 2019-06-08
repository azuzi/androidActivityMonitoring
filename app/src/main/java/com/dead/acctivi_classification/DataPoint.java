package com.dead.acctivi_classification;

public class DataPoint {

    private double vX,vY,vZ, sdX, sdY,sdZ;
    private Category category;

    public DataPoint(double vX, double vY, double vZ,double sdX, double sdY, double sdZ, Category category){
        this.vX =vX ;
        this.vY = vY;
        this.vZ = vZ;
        this.sdX =sdX ;
        this.sdY = sdY;
        this.sdZ = sdZ;
        this.category = category;
    }

    public DataPoint(DataPoint dataPoint){

        this.vX = dataPoint.getVX();
        this.vY = dataPoint.getVY();
        this.vZ = dataPoint.getVZ();

        this.sdX = dataPoint.getSDX();
        this.sdY = dataPoint.getSDY();
        this.sdZ = dataPoint.getSDZ();
        this.category = dataPoint.getCategory();
    }


    public double getVX() {
        return vX;
    }
    public double getVY() {return vY; }
    public double getVZ() {
        return vZ;
    }

    public double getSDX() {
        return sdX;
    }
    public double getSDY() {
        return sdY;
    }
    public double getSDZ() {
        return sdZ;
    }

    public void setVX(double vx){
        this.vX = vx;
    }
    public void setVY(double vy) { this.vY = vy;  }
    public void setVZ(double vz) {
        this.vZ = vz;
    }

    public void setSDX(double sdx){
        this.sdX = sdx;
    }
    public void setSDY(double sdy) {
        this.sdY = sdy;
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
