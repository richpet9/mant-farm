package com.minegame.world;

import com.minegame.core.GameID;
import com.minegame.core.GameObject;
import com.minegame.exceptions.NullSpriteException;
import com.minegame.gui.ImageLoader;
import com.minegame.gui.Sprite;

import java.awt.*;

/**
 * Elevators push chunks up
 */
public class Elevator extends GameObject {
    private Sprite icon;
    private Sprite iconGhost;

    public Elevator(int cellX, int cellY) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.pixelX = (cellX * Cell.CELL_WIDTH)  + ((Cell.CELL_WIDTH / 4));
        this.pixelY = cellY * Cell.CELL_HEIGHT;
        this.w = Cell.CELL_WIDTH / 2;
        this.h = Cell.CELL_HEIGHT;
        this.id = GameID.ELEVATOR;

        try {
            this.icon = ImageLoader.getSprite("elevator");
            this.iconGhost = ImageLoader.getSprite("elevator_g");
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
    public void render(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(pixelX - cameraX, pixelY - cameraY, w, h);
    }

}
