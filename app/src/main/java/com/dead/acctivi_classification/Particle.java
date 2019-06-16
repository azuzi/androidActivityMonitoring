package com.dead.acctivi_classification;

import android.graphics.PointF;

import java.util.Random;

public class Particle {

    public PointF[] landmarks;
    public int worldWidth;
    public float orientation;
    public int worldHeight;
    public float x;
    public float y;
    public Random random;
    public double probability = 0;

    public Particle(PointF[] landmarks, int width, int height) {
        this.landmarks = landmarks;
        this.worldWidth = width;
        this.worldHeight = height;
        random = new Random();
        x = random.nextFloat() * width;
        y = random.nextFloat() * (height )  ;
        y = y + 5.98f ;
        orientation = random.nextFloat() * 2f * ((float)Math.PI);

    }

    public void move(float turn, float forward)throws Exception {
        if(forward < 0) {
            throw new Exception("cannot move backwards");
        }
        orientation = orientation + turn + (float)random.nextGaussian();
        orientation = circle(orientation, 2f * (float)Math.PI);

        double dist = forward + random.nextGaussian() ;

        x += Math.cos(orientation) * dist;
        y += Math.sin(orientation) * dist;
        x = circle(x, worldWidth);
        y = circle(y, worldHeight);
    }

    private float circle(float num, float length) {
        while(num > length - 1) num -= length;
        while(num < 0) num += length;
        return num;
    }



}