package com.ngm.main;

import com.ngm.tsp.CitySwarm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) {
        // write your code here
        BufferedReader br = null;
        FileReader fr = null;
        ArrayList<Integer> aX = new ArrayList<>(); //X-coordinate list.
        ArrayList<Integer> aY = new ArrayList<>(); //Y-coordinate list.

        try {
            fr = new FileReader("input\\uuuu.tsp");
            br = new BufferedReader(fr);

            int count = 0;
            String sCurrentLine;
            while (!(sCurrentLine = br.readLine()).equals("EOF")) {
                if(count < 6) {
                    count++;
                    continue;
                }
                StringTokenizer st = new StringTokenizer(sCurrentLine);
                int tokenCount = 0;
                while(st.hasMoreTokens()) {
                    if(tokenCount == 0) st.nextToken(" ");
                    else if(tokenCount == 1) aX.add(Integer.parseInt(st.nextToken(" ")));
                    else if(tokenCount == 2) aY.add(Integer.parseInt(st.nextToken(" ")));
                    tokenCount++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        CitySwarm.initializeMap(aX, aY);
        CitySwarm.printMap();
        long startTime = System.nanoTime();
        CitySwarm.PSOAlgorithm();
        CitySwarm.printBestSolution();
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Elapse time: " + (double)totalTime/1000000000 + "s");
    }
}
