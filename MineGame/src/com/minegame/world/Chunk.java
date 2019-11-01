package com.minegame.world;

import com.minegame.core.Game;
import com.minegame.core.GameObject;

import java.awt.*;
import java.util.ArrayList;

public class Chunk extends GameObject {
    private Element element;
    private World world;

    public Chunk(World world, Element element, int cellX, int cellY) {
        this.element = element;
        this.cellX = cellX;
        this.cellY = cellY;
        this.pixelX = cellX * Cell.CELL_WIDTH;
        this.pixelY = cellY * Cell.CELL_HEIGHT;
        this.w = Cell.CELL_WIDTH - 2;
        this.h = Cell.CELL_HEIGHT - 2;

        this.world = world;
        this.moves = true;
        this.usesGravity = true;
    }

    @Override
    public Rectangle getBounds() { return new Rectangle(pixelX, pixelY, w, h); }

    @Override
    public void tick() {
        //TODO: IF ELEMENT IS AIR DESTROY SELF IMMEDIATELY

        checkCollisions();

        super.tick();
    }

    @Override
    public void render(Graphics2D g) {
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
        g.fillOval(pixelX - cameraX, pixelY - cameraY, w, h);
    }

    private void checkCollisions() {
        int newX = pixelX + (int) Math.round(velX);
        int newY = pixelY - (int) Math.round(velY);
        Rectangle newRect = new Rectangle(newX, newY, w, h);

        //Not gonna lie this collision algorithm is slick
        if(velY != 0 || velX != 0) {
            //Chunk is moving
            ArrayList<Cell> neighbors = world.getNeighbors(cellX, cellY);

            for(Cell cell : neighbors) {
                if(newRect.intersects(cell.getBounds()) && (!cell.isAir() || cell.hasChunk())) {
                    //Collision
                    if(velX != 0) velX = 0;
                    if(velY != 0) velY = 0;
                }
            }
        }
    }
}
