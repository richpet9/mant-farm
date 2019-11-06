package com.minegame.core;

/**
 * Camera is the viewport controller, which works by adjusting the pixel location
 * of every object the game renders, both Cells and GameObjects
 */
public class Camera {
    private Player player;
    private int y = 0; //the vertical location of the camera
    private int x = 0; //the vertical location of the camera
    private int offsetTop;
    private int offsetBottom;
    private int offsetLeft;
    private int offsetRight;
    private double zoomLevel = 1.0; //as a percent

    public Camera(int offsetTop, int offsetBottom, int offsetLeft, int offsetRight) {
        this.offsetTop = offsetTop;
        this.offsetBottom = offsetBottom;
        this.offsetLeft = offsetLeft;
        this.offsetRight = offsetRight;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    //GETTERS
    public double getZoomLevel() {
        return zoomLevel;
    }

    //SETTERS
    public void setY(int newY) {
        this.y = (int) Game.clamp(newY, offsetTop, offsetBottom);
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setX(int newX) {
        this.x = (int) Game.clamp(newX, offsetLeft, offsetRight);
    }

    public void setZoomLevel(double newZoom) {
        zoomLevel = newZoom;
    }

}
