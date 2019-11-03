package com.minegame.world;

import com.minegame.core.GameID;
import com.minegame.core.GameObject;
import com.minegame.exceptions.NullSpriteException;
import com.minegame.gui.ImageLoader;
import com.minegame.gui.Sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.text.DecimalFormat;

/**
 * A bomb can be detonated by Mants. It destroys a radius of cells and spawns chunks.
 */
public class Bomb extends GameObject {
    private boolean armed = false;
    private boolean detonated = false;
    private boolean hasJob = false;
    private int radius;
    private int detonationTimer;
    private long armedTime;
    private double countdown;
    private Sprite icon;
    private Sprite iconGhost;
    private Sprite iconArmed;

    public Bomb(int cellX, int cellY, int detonationTimer, int radius) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.pixelX = cellX * Cell.CELL_WIDTH;
        this.pixelY = cellY * Cell.CELL_HEIGHT;
        this.w = Cell.CELL_WIDTH;
        this.h = Cell.CELL_HEIGHT;
        this.id = GameID.BOMB;

        this.detonationTimer = detonationTimer;
        this.countdown = detonationTimer;
        this.radius = radius;

        try {
            this.icon = ImageLoader.getSprite("bomb");
            this.iconArmed = ImageLoader.getSprite("bomb_armed");
            this.iconGhost = ImageLoader.getSprite("bomb_g");
        } catch (NullSpriteException e) {
            e.printStackTrace();
            System.out.println("Sprite could not be found for object: " + this.id);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(pixelX, pixelY, w, h);
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
                detonated = true;
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        if(!armed) {
            g.drawImage((!ghost) ? icon.getImage() : iconGhost.getImage(), pixelX - cameraX, pixelY - cameraY, w, h, null);
            g.setColor(Color.WHITE);
            g.drawString("Not Armed", pixelX - cameraX, pixelY - cameraY - 5);
        } else {
            g.drawImage(iconArmed.getImage(), pixelX - cameraX, pixelY - cameraY, w, h, null);
            g.setColor(Color.RED);
            g.drawString("Armed", pixelX - cameraX, pixelY - cameraY - 5);
            g.drawString("T-" + new DecimalFormat("#.##").format(countdown), pixelX - cameraX, pixelY - cameraY - 20);
        }
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
    public boolean hasJob() {
        return hasJob;
    }

    //SETTERS
    public void setArmed(boolean armed) {
        this.armed = armed;
        armedTime = System.nanoTime();
    }
    public void setRadius(int radius) {
        this.radius = radius;
    }
    public void setHasJob(boolean hasJob) {
        this.hasJob = hasJob;
    }
    public void setDetonated(boolean detonated) {
        this.detonated = detonated;
    }
}
