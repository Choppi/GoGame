package com.example.alexis.gogame;

import java.util.List;

/**
 * Created by alexis on 30/11/2016.
 */

public class Eye {
    private Circle eye;
    private Blockchain surrounders;

    public Eye(Circle eye, Blockchain surrounders)
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

    public Blockchain getSurrounders() {
        return surrounders;
    }
}
