package com.dead.acctivi_classification;

import android.graphics.Point;

import java.util.Random;

public class ParticleFilter {
    static Particle[] particles;
    int numParticles = 0;
    Random gen = new Random();

    public ParticleFilter(int numParticles, Point[] landmarks, int width, int height){

        this.numParticles = numParticles;

        particles = new Particle[numParticles];
        for (int i = 0; i < numParticles; i++) {
            particles[i] = new Particle(landmarks, width, height);
        }
        // generateInitialParticles
    }
}
