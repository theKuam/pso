package com.ngm.tsp;

import com.ngm.main.Particle;
import com.ngm.main.Swarm;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class CitySwarm extends Swarm {
    private static ArrayList<City> map = new ArrayList<>();
    private static final int DOMAIN = 100; //Coordinates domain.
    private static final double TARGET = 0.0;
    private static double globalBest = Double.MAX_VALUE;
    private static int cityCount = 10; //Number of cities.

    private static ArrayList<Integer> aX = new ArrayList<>(); //X-coordinate list.
    private static ArrayList<Integer> aY = new ArrayList<>(); //Y-coordinate list.

    //Initialize a map with cities.
    public static void initializeMap() {
        System.out.print("Number of Cities: ");
        Scanner sc = new Scanner(System.in);
        cityCount = sc.nextInt();
        int randomX, randomY;

        //Create city list without concurrent points.
        boolean duplicate;
        while(aX.size() < cityCount) {
            do {
                randomX = new Random().nextInt(DOMAIN);
                if(aX.indexOf(randomX) == -1) {
                    duplicate = false;
                    aX.add(randomX);
                    randomY = new Random().nextInt(DOMAIN);
                    aY.add(randomY);
                }
                else duplicate = true;
            } while(duplicate);
        }

        //Add city into the map.
        for (int i = 0; i < cityCount; i++) {
            City city = new City();
            city.setcX(aX.get(i));
            city.setcY(aY.get(i));
            map.add(city);
        }
    }

    //Show location of cities (their coordinates).
    public static void printMap() {
        for (int i = 0; i < cityCount; i++) {
            System.out.print("City " + i + ": [" + map.get(i).getcX()
                                        + ", " + map.get(i).getcY() + "] \n");
        }
    }

    //Initialize Particle Swarm.
    private static void initializeSwarm() {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            Particle newParticle = new Particle();
            //We set start candidate is {0 .. cityCount-1}.
            for (int j = 0; j < cityCount; j++) {
                newParticle.setData(j, j);
            }
            //add it into particle list.
            particles.add(newParticle);
            //Rearranging cities to create new candidate.
            for (int j = 0; j < PARTICLE_COUNT; j++) {
                randomlyArrange(particles.indexOf(newParticle));
            }
            //Not just total distance of the map, but also particle pBest.
            getTotalDistance(particles.indexOf(newParticle));
        }
    }

    //Swapping two random cities to create new candidate.
    private static void randomlyArrange(final int index) {
        int firstCity = new Random().nextInt(cityCount);
        int secondCity = 0;
        boolean done = false;
        //Must be two different cities.
        while(!done) {
            secondCity = new Random().nextInt(cityCount);
            if(secondCity != firstCity) {
                done = true;
            }
        }
        //Swapping..
        switchValues(index, secondCity, firstCity);
    }

    //Calculate pBest of a particle.
    private static void getTotalDistance(final int index) {
        Particle thisParticle = particles.get(index);
        thisParticle.setpBest(0.0);

        //Calculate total distance of a trip.
        for(int i = 0; i < cityCount; i++) {
            if(i == cityCount -1)
                thisParticle.setpBest(thisParticle.getpBest() +
                        getDistance(thisParticle.getData(cityCount - 1),
                                 thisParticle.getData(0))); //Back to the first city. Complete trip.
            else
                thisParticle.setpBest(thisParticle.getpBest() +
                        getDistance(thisParticle.getData(i),
                                thisParticle.getData(i+1)));
        }
    }

    //Calculate distance between two cities
    private static double getDistance(final int firstCity, final int secondCity) {
        City cityA = map.get(firstCity);
        City cityB = map.get(secondCity);
        return Math.sqrt(
                        Math.pow(Math.abs(cityA.getcX()
                                - cityB.getcX()), 2)
                        + Math.pow(Math.abs(cityA.getcY()
                                - cityB.getcY()), 2)
        ); //Distance between two points formula
    }

    //PSO Algorithm TSP version
    public static void PSOAlgorithm() {
        Particle aParticle;
        int iteration = 0;
        boolean done = false;
        int counter = 0;
        double tempGBest = globalBest;
        //First, Initialize Particle Swarm.
        initializeSwarm();
        //Then, find the best solution using loop.
        while(!done) {
            /*
            Three condition can end this loop:
                If the maximum number of iterations has been reached, or,
                If the target value has been found, or,
                If the global best is not changed for a long time (eg. five iterations).
            */
            if(iteration < MAX_ITERATIONS) {
                for (int i = 0; i < PARTICLE_COUNT; i++) {
                    //For each iteration, show a trip and its total distance.
                    aParticle = particles.get(i);
                    System.out.print("Route: ");
                    for (int j = 0; j < cityCount; j++) {
                        System.out.print(aParticle.getData(j) + ", ");
                        if(j == cityCount - 1)
                            System.out.print(aParticle.getData(0) + ", ");
                    } //A complete trip.
                    getTotalDistance(i);
                    System.out.print("Distance: " + aParticle.getpBest() + "\n");
                    //The cost must not be greater than the target.
                    if(aParticle.getpBest() <= TARGET) {
                        //The target value has been found.
                        done = true;
                    }
                }

                //Sort particles by their pBest scores, best to worst.
                mergeSort(0, PARTICLE_COUNT - 1);
                //Calculate particle velocity.
                getVelocity();
                //Update particle position.
                updateParticles();
                //Step #
                System.out.println("Step: " + iteration);
                iteration++;
                counter++;
                if(Double.compare(tempGBest, globalBest) != 0) {
                    tempGBest = globalBest;
                    counter = 0;
                }
                //The global best is not changed for a long time.
                if(counter == MAX_LAPSE) {
                    done = true;
                }
            }
            else {
                //The maximum number of iterations has been reached.
                done = true;
            }
        }
    }

    //Merge sort, for particle list.
    private static void mergeSort(int p, int r) {
        if(p < r) {
            int  q = Math.floorDiv((p + r), 2);
            mergeSort(p, q);
            mergeSort(q + 1, r);
            merge(p, q, r);
        }
    }

    private static void merge(int p, int q, int r) {
        Particle maxParticle = new Particle();
        maxParticle.setpBest(Integer.MAX_VALUE);
        ArrayList<Particle> lParticles
                = new ArrayList<>(particles.subList(p, q + 1));
        lParticles.add(maxParticle);
        ArrayList<Particle> rParticles
                = new ArrayList<>(particles.subList(q + 1, r + 1));
        rParticles.add(maxParticle);
        int i = 0;
        int j = 0;
        for (int k = p; k < r + 1; k++) {
            if(lParticles.get(i).compareTo(rParticles.get(j)) - 1 != 0) {
                particles.set(k, lParticles.get(i));
                i++;
            }
            else {
                particles.set(k, rParticles.get(j));
                j++;
            }
        }
    }

    //Calculate particle velocity
    private static void getVelocity() {
        double gBest; //gBest.
        double addVelocity; //Additional velocity.

        //After sorting, gBest will be the first in list.
        gBest = particles.get(0).getpBest();
        globalBest = gBest;
        //for each particles, calculate new velocity.
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            addVelocity = V_WEIGHT*(new Random()).nextDouble()*(gBest - particles.get(i).getpBest());
            particles.get(i).setVelocity(addVelocity);
        }
    }

    private static void updateParticles() {
        //Best is at index 0, so we start update from the second best.
        for (int i = 1; i < PARTICLE_COUNT; i++) {
            // The higher velocity score, the more changes it will need.
            int changes = (int) Math.floor(Math.abs(particles.get(i).getVelocity()));
            System.out.println("Changes for particle" + i + ": " + changes);
            for(int j = 0; j < changes; j++) {
                randomlyArrange(i);
                //Push it closer to it's neighbor.
                copyFromParticle(i - 1, i);
            }
            //Update pBest value.
            getTotalDistance(i);
        }
    }

    private static void copyFromParticle(final int src, final int dest) {
        //Push destination's data points closer to source's data points.
        Particle best = particles.get(src);
        int firstTarget = new Random().nextInt(cityCount); //Source's city to target.
        int secondTarget = 0, fIndex = 0, sIndex = 0, tempIndex;

        //secondTarget will be source's neighbor immediately succeeding firstTarget (circular).
        for (int i = 0; i < cityCount; i++) {
            if(best.getData(i) == firstTarget) {
                //If end of array, take from beginning.
                if(i == cityCount - 1) secondTarget = best.getData(0);
                else secondTarget = best.getData(i + 1);
                break;
            }
        }
        //Move secondTarget next to firstTarget by switching values.
        for (int j = 0; j < cityCount; j++) {
            if(particles.get(dest).getData(j) == firstTarget) fIndex = j;
            if(particles.get(dest).getData(j) == secondTarget) sIndex = j;
        }
        //Get tempIndex succeeding fIndex.
        if(fIndex == cityCount - 1) tempIndex = 0;
        else tempIndex = fIndex + 1;
        //Switch sIndex value with tempIndex value.
        switchValues(dest, sIndex, tempIndex);
    }

    private static void switchValues(int dest, int sIndex, int tempIndex) {
        int temp = particles.get(dest).getData(tempIndex);
        particles.get(dest).setData(tempIndex, particles.get(dest).getData(sIndex));
        particles.get(dest).setData(sIndex, temp);
    }

    public static void printBestSolution()
    {
        if(particles.get(0).getpBest() <= TARGET){
            //Print it.
            System.out.println("Target reached.");
        }else{
            System.out.println("Target not reached");
        }
        System.out.print("Shortest Route: ");
        for(int j = 0; j < cityCount; j++)
        {
            System.out.print(particles.get(0).getData(j) + ", ");
            if(j == cityCount - 1)
                System.out.print(particles.get(0).getData(0) + ", ");
        }
        System.out.print("Distance: " + particles.get(0).getpBest() + "\n");
    }
}
