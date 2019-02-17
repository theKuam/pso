package com.ngm.main;

import com.ngm.tsp.CitySwarm;

public class Main {
    public static void main(String[] args) {
        // write your code here
        CitySwarm.initializeMap();
        CitySwarm.printMap();
        CitySwarm.PSOAlgorithm();
        CitySwarm.printBestSolution();
    }
}
