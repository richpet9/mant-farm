package com.minegame.core;

import com.minegame.world.Cell;
import com.minegame.world.Element;

import java.awt.*;

/**
 * An abstract class of all the entities which can exist in the game.
 */
public abstract class GameObject {
    protected GameID id;
    protected int cellX, cellY, pixelX, pixelY, cameraX, cameraY;
    protected int w;
    protected int h;
    protected double velX = 0, velY = 0;
    protected boolean usesGravity = false;
    protected boolean usesCollision = false;
    protected boolean moves = false;

    public GameID getID() { return id; }
    public int getCellY() { return cellY; }
    public int getCellX() { return cellX; }
    public int getPixelX() { return pixelX; }
    public int getPixelY() { return pixelY; }
    public int getCameraX() { return cameraX; }
    public int getCameraY() { return cameraY; }
    public int getW() { return w; }
    public int getH() { return h; }
    public double getVelX() { return velX; }
    public double getVelY() { return velY; }
    public boolean falls() {return usesGravity; }

    public void setID(GameID id) { this.id = id; }
    public void setCellX(int cellX) { this.cellX = cellX; }
    public void setCellY(int cellY) { this.cellY = cellY; }
    public void setPixelX(int pixelX) { this.pixelX = pixelX; }
    public void setPixelY(int pixelY) { this.pixelY = pixelY; }
    public void setCameraX(int cameraX) { this.cameraX = cameraX; }
    public void setCameraY(int cameraY) { this.cameraY = cameraY; }
    public void setW(int w) { this.w = w; }
    public void setH(int h) { this.h = h; }
    public void setVelX(double x) { this.velX = x; }
    public void setVelY(double y) { this.velY = y; }
    public void setUsesGravity(boolean usesGravity) { this.usesGravity = usesGravity; }
    public void setCellXY(int x, int y) {
        this.cellX = x;
        this.cellY = y;
        this.pixelX = cellX * Cell.CELL_WIDTH;
        this.pixelY = cellY * Cell.CELL_HEIGHT;
    }
    public void setPixelXY(int x, int y) {
        this.pixelX = x;
        this.pixelY = y;
    }
    public void setCameraXY(int x, int y){
        this.cameraX = x;
        this.cameraY = y;
    }

    public void tick() {
        if(moves) {
            pixelX = pixelX + (int) Math.round(velX);
            pixelY = pixelY - (int) Math.round(velY);

            //If our x pixel value is divisible by ten
            cellX = pixelX / Cell.CELL_WIDTH;
            cellY = pixelY / Cell.CELL_HEIGHT;
        }
    };

    public abstract void render(Graphics2D g);
    public abstract Rectangle getBounds();
}
