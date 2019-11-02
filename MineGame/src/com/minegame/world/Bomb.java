package com.minegame.world;

import com.minegame.core.GameID;
import com.minegame.core.GameObject;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * A bomb can be detonated by Mants. It destroys a radius of cells and spawns chunks.
 */
public class Bomb extends GameObject {
    private Color color = Color.BLACK;
    private boolean armed = false;
    private boolean detonated = false;
    private int radius;
    private int detonationTimer;
    private long armedTime;
    private double countdown;

    public Bomb(int cellX, int cellY, int detonationTimer, int radius) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.pixelX = cellX * Cell.CELL_WIDTH;
        this.pixelY = cellY * Cell.CELL_HEIGHT;
        this.w = Cell.CELL_WIDTH - 4;
        this.h = Cell.CELL_HEIGHT - 4;
        this.id = GameID.BOMB;

        //TODO: Make detonation timer real-time based rather than tick based
        this.detonationTimer = detonationTimer;
        this.countdown = detonationTimer;
        this.radius = radius;
    }

    @Override
    public void tick() {
        super.tick();

        if(armed && !detonated) {
            double elapsedTime = (System.nanoTime() - armedTime) / 1E9;

            //Set the countdown var for display purposes
            countdown = (double) detonationTimer - elapsedTime;
            if(countdown < 0) countdown = 0;

            if(elapsedTime >= detonationTimer) {
                color = Color.RED;
                detonated = true;
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(color);
        g.fillRect((pixelX - cameraX) + 2, (pixelY - cameraY) + 2, w, h);

        if(!armed) {
            g.setColor(Color.WHITE);
            g.drawString("Not Armed", pixelX - cameraX, pixelY - cameraY - 5);
        } else {
            g.setColor(Color.RED);
            g.drawString("Armed", pixelX - cameraX, pixelY - cameraY - 5);
            g.drawString("T-" + new DecimalFormat("#.##").format(countdown), pixelX - cameraX, pixelY - cameraY - 20);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(pixelX, pixelY, w, h);
    }

    //GETTERS
    public boolean isArmed() {
        return armed;
    }
    public boolean isDetonated() {
        return detonated;
    }
    public int getRadius() {
        return radius;
    }

    //SETTERS
    public void setArmed(boolean armed) {
        this.armed = armed;
        armedTime = System.nanoTime();
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }
}