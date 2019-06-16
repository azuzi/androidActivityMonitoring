package com.dead.acctivi_classification;

public class Particles {

    private Position lastPosition; // previous particle position
    private Position position; // particle position
    private double weight;

    public Particles(Position position, double strideLength, double weight) {
        this.lastPosition = new Position(position);
        this.position = new Position(position);
        this.weight = weight;
    }

    public Particles(Particles particle) {
        this.lastPosition = new Position(particle.lastPosition);
        this.position = new Position(particle.position);
        this.weight = particle.weight;
    }

    public Position getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(Position lastPosition) {
        this.lastPosition = new Position(lastPosition);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = new Position(position);
    }



    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public void updateLastPosition() {
        lastPosition = new Position(position);
    }
}
