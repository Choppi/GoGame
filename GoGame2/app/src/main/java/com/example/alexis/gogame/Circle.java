package com.example.alexis.gogame;


import android.graphics.Paint;

public class Circle {
    private int posX;
    private int posY;
    private int radius;
    private int liberties;
    private Paint color;

    public Circle(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.radius = 0;
        this.liberties = 0;
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

    public Paint getColor() {
        return color;
    }

    public void setColor(Paint color) {
        this.color = color;
    }
}
