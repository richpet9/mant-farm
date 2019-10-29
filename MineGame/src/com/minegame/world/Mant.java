package com.minegame.world;

import com.minegame.core.Game;
import com.minegame.core.GameID;
import com.minegame.core.GameObject;

import javax.swing.*;
import java.awt.*;

/**
 * As Mant is a class representing the moving figure of a character
 * on the screen.
 */
public class Mant extends GameObject {
    private static final int MANT_WIDTH = 1;    //IN CELLS
    private static final int MANT_HEIGHT = 2;   //IN CELLS
    private Image icon;

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

        ImageIcon i = new ImageIcon(Game.class.getResource("/resources/icons/mant/mant.png"));
        this.icon = i.getImage();
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
        g.drawImage(this.icon, pixelX - cameraX, pixelY - cameraY, null);
    }

}
