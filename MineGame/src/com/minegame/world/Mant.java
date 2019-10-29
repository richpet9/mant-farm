package com.minegame.world;

import com.minegame.core.Game;
import com.minegame.core.GameID;
import com.minegame.core.GameObject;
import com.minegame.exceptions.NullSpriteException;
import com.minegame.gui.ImageLoader;
import com.minegame.gui.Sprite;

import javax.swing.*;
import java.awt.*;

/**
 * As Mant is a class representing the moving figure of a character
 * on the screen.
 */
public class Mant extends GameObject {
    private static final int MANT_WIDTH = 1;    //IN CELLS
    private static final int MANT_HEIGHT = 2;   //IN CELLS
    private Sprite icon;
    private boolean onGround = false;

    public Mant(int cellX, int cellY, Color color) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.pixelX = (cellX * Cell.CELL_WIDTH);
        this.pixelY = cellY * Cell.CELL_HEIGHT;
        this.w = Cell.CELL_WIDTH * MANT_WIDTH;
        this.h = Cell.CELL_HEIGHT * MANT_HEIGHT;

        this.id = GameID.MANT;
        this.usesGravity = true;
        this.usesCollision = true;
        try {
            this.icon = ImageLoader.getSprite(this.id);
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
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(icon.getImage(), pixelX - cameraX, pixelY - cameraY, null);
        g.drawString(cellX + " " + cellY, pixelX - cameraX, pixelY - cameraY - 5);
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
