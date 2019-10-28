package com.minegame.world;

import com.minegame.core.GameID;
import com.minegame.core.GameObject;

import java.awt.*;

/**
 * A Cell, represents the map grid
 */
public class Cell extends GameObject {
    public static final int CELL_WIDTH = 10;
    public static final int CELL_HEIGHT = 10;
    public static final int PADDING =0;
    private Element element = Element.AIR;

    public Cell(int pixelX, int pixelY, int cellX, int cellY) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.pixelX = pixelX;
        this.pixelY = pixelY;
        this.w = CELL_WIDTH;
        this.h = CELL_HEIGHT;

        this.id = GameID.CELL;
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
        if(!isAir()) {
            //Assign color based on element
            switch(element) {
                case DIRT: g.setColor(new Color(0x91695C));
                    break;
                case ROCK: g.setColor(new Color(0x7A8691));
                    break;
                case IRON: g.setColor(new Color(0x7D5E5F));
                    break;
                case COPPER: g.setColor(new Color(0xB27643));
                    break;
                case SILVER: g.setColor(new Color(0xD4D4D4));
                    break;
                case GOLD: g.setColor(new Color(0xD7CA68));
                    break;
            }

            g.fillRect(pixelX - cameraX , pixelY - cameraY , w, h);
        }
    }

    public Element getElement() { return element; }

    public void setElement(Element element) {
        this.element = element;
        usesGravity = element != Element.AIR;
    }

    public boolean isAir() {
        return element == Element.AIR;
    }

}
