package com.example.alexis.gogame;


import android.graphics.Color;
import android.graphics.Paint;

public class Circle {
    private int posX;
    private int posY;
    private int radius;
    private int liberties;
    private Paint paint;
    private int territoriesIndex;

    public Circle(int posX, int posY,Paint paint) {
        this.posX = posX;
        this.posY = posY;
        this.radius = 0;
        this.liberties = 0;
        this.paint = paint;


    }
    public Circle(Circle c)
    {
        this.posX = c.getPosX();
        this.posY = c.getPosY();
        this.radius = c.getRadius();
        this.liberties = c.getLiberties();
        this.paint = c.getPaint();
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getLiberties() {
        return liberties;
    }

    public void setLiberties(int liberties) {
        this.liberties = liberties;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setColor(Paint paint) {
        this.paint = paint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Circle circle = (Circle) o;

        if (getPosX() != circle.getPosX()) return false;
        if (getPosY() != circle.getPosY()) return false;
        if (getRadius() != circle.getRadius()) return false;
        return getPaint().equals(circle.getPaint());

    }

    public int getTerritoriesIndex() {
        return territoriesIndex;
    }

    public void setTerritoriesIndex(int territoriesIndex) {
        //0 is null
        //1 is black
        //2 is white
        //3 is shared
        this.territoriesIndex = territoriesIndex;
    }
}
