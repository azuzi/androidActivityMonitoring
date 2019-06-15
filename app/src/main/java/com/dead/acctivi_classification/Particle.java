package com.dead.acctivi_classification;

import android.graphics.Point;

import java.util.Random;

public class Particle {

    public Point[] landmarks;
    public int worldWidth;
    public float orientation;
    public int worldHeight;
    public float x;
    public float y;
    public Random random;
    public double probability = 0;

    public Particle(Point[] landmarks, int width, int height) {
        this.landmarks = landmarks;
        this.worldWidth = width;
        this.worldHeight = height;
        random = new Random();
        x = random.nextFloat() * width;
        y = random.nextFloat() * height;
        orientation = random.nextFloat() * 2f * ((float)Math.PI);

    }
}
