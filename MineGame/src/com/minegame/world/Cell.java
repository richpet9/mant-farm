package com.minegame.world;

import com.minegame.core.GameID;
import com.minegame.core.GameObject;
import com.minegame.exceptions.NullSpriteException;
import com.minegame.gui.ImageLoader;
import com.minegame.gui.Sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * A Cell, represents the map grid areas
 */
public class Cell extends GameObject {
    public static final int CELL_WIDTH = 15;
    public static final int CELL_HEIGHT = 15;
    public static final double CHUNK_DROP_CHANCE = 0.65;
    private static final int OVERLAY_PADDING = 2;
    private Element element = Element.AIR;
    private Element dropChunk = null;
    private boolean hasChunk = false;
    private boolean overlay = false;
    private Color overlayColor = Color.white;
    private GameObject item = null;
    private Sprite icon = null;
    private Color mainColor;

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
            if(icon != null) {
                g.drawImage(icon.getImage(), pixelX - cameraX, pixelY - cameraY, w, h, null);
            } else  {
                g.setColor(mainColor);
                g.fillRect(pixelX - cameraX , pixelY - cameraY , w, h);
            }
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
    public boolean isAir() {
        return element == Element.AIR;
    }
    public boolean isOverlayOn() {
        return overlay;
    }
    public boolean hasNoChunk() {
        return !hasChunk;
    }
    public GameObject getItem() {
        return item;
    }
    public boolean hasItem() {
        return item != null;
    }

    //Setters
    public void setOverlay(boolean overlay) {
        this.overlay = overlay;
    }
    public void setHasChunk(boolean hasChunk) {
        this.hasChunk = hasChunk;
    }
    public void setItem(GameObject item) {
        this.item = item;
    }
    public void setElement(Element element) {
        this.element = element;

        //Assign color based on element
        if(!isAir()) {
            switch(this.element) {
                case DIRT: mainColor = new Color(0x7E614F);
                    break;
                case ROCK: mainColor = new Color(0x7A8691);
                    break;
                case IRON: mainColor = new Color(0x7D5E5F);
                    break;
                case COPPER: mainColor = new Color(0xB27643);
                    break;
                case SILVER: mainColor = new Color(0xD4D4D4);
                    break;
                case GOLD: mainColor = new Color(0xD7CA68);
                    break;
            }
        }
    }
    public void setDropChunk(Element chunk) {
        this.dropChunk = chunk;
    }

    /**
     * Tells this cell to recompute what its icon should be
     */
    public void getIcon() {
        switch(element) {
            case AIR:
                break;
            case DIRT:
                try {
                    icon = ImageLoader.getSprite("cell_dirt");
                } catch (NullSpriteException e) {
                    e.printStackTrace();
                }
                break;
            default:
                icon = null;
        }
    }
}
