package com.minegame.world;

import com.minegame.core.GameID;
import com.minegame.core.GameObject;

import java.awt.*;

public class Conveyor extends GameObject {
    private int direction;

    public Conveyor(int cellX, int cellY, int direction) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.pixelX = cellX * Cell.CELL_WIDTH;
        this.pixelY = ((cellY * Cell.CELL_HEIGHT) + Cell.CELL_HEIGHT) - (Cell.CELL_HEIGHT / 3);
        this.w = Cell.CELL_WIDTH;
        this.h = Cell.CELL_HEIGHT / 3;
        this.id = GameID.CONVEYOR;

        this.direction = direction;
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(pixelX - cameraX, pixelY - cameraY, w, h);

        g.setColor(Color.RED);
        g.drawString((direction > 0) ? ">" : "<", pixelX - cameraX, (pixelY - cameraY) + 5);
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }

    //GETTERS
    public int getDirection() {
        return direction;
    }

    //SETTERS
    public void setDirection(int direction) {
        this.direction = direction;
    }
}
