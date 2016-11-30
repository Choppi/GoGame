package com.example.alexis.gogame;


import android.graphics.Paint;

public class Circle {
    private int posX;
    private int posY;
    private int radius;
    private int liberties;
    private Paint paint;

    public Circle(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.radius = 0;
        this.liberties = 0;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public Circle(Circle c)
    {
        this.posX = c.posX;
        this.posY = c.posY;
        this.radius = c.radius;
        this.liberties = c.liberties;

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
}
