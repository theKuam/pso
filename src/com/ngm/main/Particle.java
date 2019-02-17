package com.ngm.main;

import java.util.ArrayList;

public class Particle implements Comparable<Particle> {
    private ArrayList<Integer> data = new ArrayList<>(); //Particle data.
    private double pBest  = 0; //Best individual particle position.
    private double velocity = 0.0; //Particle velocity (how particle changes its position).

    public Particle() {
        this.pBest = 0;
        this.velocity = 0.0;
    }

    public int getData(final int index) {
        return this.data.get(index);
    }

    public void setData(final int index, final int value) {
        if(index > data.size() - 1) this.data.add(value);
        else this.data.set(index, value);
    }

    public double getpBest() {
        return pBest;
    }

    public void setpBest(final double value) {
        this.pBest = value;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(final double velocityScore) {
        this.velocity += velocityScore;
    }

    //Compare the old particle pBest to the new particle pBest.
    @Override
    public int compareTo(Particle that) {
        return Double.compare(this.getpBest(), that.getpBest());
        //this < that ? -1 : this > that ? 1 : 0.
    }
}
