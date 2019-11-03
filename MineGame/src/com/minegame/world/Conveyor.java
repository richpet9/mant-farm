package com.minegame.world;

import com.minegame.core.GameID;
import com.minegame.core.GameObject;
import com.minegame.exceptions.NullSpriteException;
import com.minegame.gui.ImageLoader;
import com.minegame.gui.Sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Conveyor extends GameObject {
    private int direction;
    private Color color = Color.DARK_GRAY;
    private Sprite icon;
    private Sprite iconGhost;

    public Conveyor(int cellX, int cellY, int direction) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.pixelX = cellX * Cell.CELL_WIDTH;
        this.pixelY = ((cellY * Cell.CELL_HEIGHT) + (2 * Cell.CELL_HEIGHT / 3));
        this.w = Cell.CELL_WIDTH;
        this.h = Cell.CELL_HEIGHT / 3;
        this.id = GameID.CONVEYOR;

        this.direction = direction;

        try {
            this.icon = ImageLoader.getSprite("conveyor");
            this.iconGhost = ImageLoader.getSprite("conveyor_g");
        } catch (NullSpriteException e) {
            e.printStackTrace();
            System.out.println("Sprite could not be found for object: " + this.id);
        }
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(color);
        g.drawImage((!ghost) ? icon.getImage() : iconGhost.getImage(), pixelX - cameraX, pixelY - cameraY, w, h, null);


        if(ghost) g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 70));

        g.setColor(Color.RED);
        g.drawString((direction > 0) ? ">" : "<", pixelX - cameraX, (pixelY - cameraY) + 5);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(pixelX, pixelY, w, h);
    }

    //GETTERS
    public int getDirection() {
        return direction;
    }

    //SETTERS
    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public void setCellXY(int x, int y) {
        this.cellX = x;
        this.cellY = y;
        this.pixelX = cellX * Cell.CELL_WIDTH;
        this.pixelY = ((cellY * Cell.CELL_HEIGHT) + (2 * Cell.CELL_HEIGHT / 3));
    }
}
