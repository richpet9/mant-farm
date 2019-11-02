package com.minegame.world;

import com.minegame.core.GameID;
import com.minegame.core.GameObject;
import com.minegame.gui.Sprite;

import java.awt.*;

/**
 * A Cell, represents the map grid areas
 */
public class Cell extends GameObject {
    public static final int CELL_WIDTH = 10;
    public static final int CELL_HEIGHT = 10;
    private static final int OVERLAY_PADDING = 2;
    private static final double CHUNK_DROP_CHANCE = 0.5;
    private Element element = Element.AIR;
    private Element dropChunk = null;
    private boolean hasChunk = false;
    private boolean overlay = false;
    private Color overlayColor = Color.white;
    private GameObject item = null;
    private Sprite icon = null;

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
    public void render(Graphics2D g) {
        if(!isAir()) {
            //Assign color based on element
            switch(element) {
                case DIRT: g.setColor(new Color(0x7E614F));
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

        if(overlay) {
            g.setColor(overlayColor);
            g.fillRect((pixelX - cameraX) + OVERLAY_PADDING, (pixelY - cameraY) + OVERLAY_PADDING, w - (2 * OVERLAY_PADDING), h - (2* OVERLAY_PADDING));
        }
    }

    //Getters
    public Element getElement() {
        return element;
    }
    public Element dropChunk() {
        return dropChunk;
    }
    public boolean isOverlayOn() {
        return overlay;
    }
    public boolean hasChunk() {
        return hasChunk;
    }
    public GameObject getItem() {
        return item;
    }

    //Setters
    public void setOverlay(boolean overlay) {
        this.overlay = overlay;
    }
    public boolean isAir() {
        return element == Element.AIR;
    }
    public void setHasChunk(boolean hasChunk) {
        this.hasChunk = hasChunk;
    }
    public void setItem(GameObject item) {
        this.item = item;
    }
    public void setElement(Element element) {
        this.element = element;
    }
    public void setDropChunk(Element chunk) {
        //Random chance of actually dropping the chunk
        if(Math.random() < CHUNK_DROP_CHANCE) {
            this.dropChunk = chunk;
        } else {
            this.dropChunk = null;
        }
    }
}
