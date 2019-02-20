package com.ngm.main;

import java.util.ArrayList;

public class Swarm {
    protected static final int PARTICLE_COUNT = 10; //Number of particles (or candidates).
    protected static final int V_WEIGHT = 2;   //weight to calculate velocity
                                            //Range: 0 <= vWeight < particleCount.
    protected static final int MAX_ITERATIONS = 10000;
    protected static final int MAX_LAPSE = 100;
    protected static ArrayList<Particle> particles = new ArrayList<>(); //list of particles.
}
