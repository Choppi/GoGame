package com.example.alexis.gogame;

import java.util.List;

/**
 * Created by alexis on 30/11/2016.
 */

public class Eye {
    private Circle eye;
    private List<Blockchain> surrounders;

    public Eye(Circle eye, List<Blockchain> surrounders)
    {
        this.eye = eye;
        this.surrounders = surrounders;
    }

    public Eye(Eye eye)
    {
        this.eye = eye.getEye();
        this.surrounders = eye.getSurrounders();
    }

    public Circle getEye() {
        return eye;
    }

    public void setEye(Circle eye) {
        this.eye = eye;
    }

    public List<Blockchain> getSurrounders() {
        return surrounders;
    }



    public void setSurrounders(List<Blockchain> surrounders) {
        this.surrounders = surrounders;
    }

    public int blockchainNumber() { return surrounders.size(); }
}
