package com.example.alexis.gogame;

import android.util.Pair;

import java.util.ArrayList;

public class Blockchain {
    private int libertyNumber;
    private ArrayList<Circle> circleList;
    private ArrayList<Pair<Integer,Integer>> eyeList;
    private ArrayList<Circle> freeNeighborsCircles;

    public Blockchain(ArrayList<Circle> circleList) {
        //constructor needs a list of circle
        this.circleList = circleList;
        this.libertyNumber = 0;
        this.freeNeighborsCircles = new ArrayList<>();
        this.eyeList = new ArrayList<>();
    }

    public int getLibertyNumber() {
        return libertyNumber;
    }

    public void setLibertyNumber(int libertyNumber) {
        this.libertyNumber = libertyNumber;
    }

    public ArrayList<Circle> getCircleList() {
        return circleList;
    }

    public void setCircleList(ArrayList<Circle> circleList) {
        this.circleList = circleList;
    }

    public ArrayList<Pair<Integer, Integer>> getEyeList() {
        return eyeList;
    }

    public void setEyeList(ArrayList<Pair<Integer, Integer>> eyeList) {
        this.eyeList = eyeList;
    }

    public ArrayList<Circle> getNeighborsCircles() {
        return freeNeighborsCircles;
    }

    public void setNeighborsCircles(ArrayList<Circle> neighborsCircles) {
        this.freeNeighborsCircles = neighborsCircles;
    }

    public Boolean contains(Circle circle){
        //return true if the element is in the list
        for (Circle c : circleList){
            if(c.equals(circle)){
                return true;
            }
        }
        return false;
    }

    public int sizeCircleList(){
        //return the size of circleList
        return circleList.size();
    }

    public int sizeEyeList(){
        //return eyes number
        return eyeList.size();
    }
/*
    public void searchFreeNeighbors(){
        for(Circle c:circleList){
            int x=c.getPosX();
            int y=c.getPosY();

        }
    }*/
}
