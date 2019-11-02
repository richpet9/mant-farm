package com.minegame.world;

import com.minegame.core.Game;
import com.minegame.core.GameID;
import com.minegame.core.GameObject;

import java.awt.*;

public class Chunk extends GameObject {
    private static final int MOVE_DELAY = 5;
    private Element element;
    private World world;
    private int timeToMove = MOVE_DELAY;

    public Chunk(World world, Element element, int cellX, int cellY) {
        this.element = element;
        this.cellX = cellX;
        this.cellY = cellY;
        this.pixelX = cellX * Cell.CELL_WIDTH;
        this.pixelY = cellY * Cell.CELL_HEIGHT;
        this.w = Cell.CELL_WIDTH;
        this.h = Cell.CELL_HEIGHT;
        this.id = GameID.CHUNK;

        this.world = world;
    }

    @Override
    public Rectangle getBounds() { return new Rectangle(pixelX, pixelY, w, h); }

    @Override
    public void tick() {
        //TODO: IF ELEMENT IS AIR DESTROY SELF IMMEDIATELY

        checkCollisions();
        checkConveyors();

        super.tick();
    }

    @Override
    public void render(Graphics2D g) {
        //Assign color based on element
        switch(element) {
            case DIRT: g.setColor(new Color(0x7A5649));
                break;
            case ROCK: g.setColor(new Color(0x66717C));
                break;
            case IRON: g.setColor(new Color(0x6D4E4F));
                break;
            case COPPER: g.setColor(new Color(0x916036));
                break;
            case SILVER: g.setColor(new Color(0xC4C4C4));
                break;
            case GOLD: g.setColor(new Color(0xBBB059));
                break;
        }
        g.fillOval(pixelX - cameraX, pixelY - cameraY, w, h);
    }

    private void checkCollisions() {
        //Get the cell below us
        int neighborY = Game.clamp(cellY + 1, 0, world.getNumY() - 1);
        int neighborX = Game.clamp(cellX, 0, world.getNumX() - 1);
        Cell neighbor = world.getCell(neighborX, neighborY);

        if(neighbor.isAir() && !neighbor.hasChunk()) {
            timeToMove--;
            if(timeToMove <= 0) {
                world.getCell(cellX, cellY).setHasChunk(false);
                cellX = neighborX;
                cellY = neighborY;
                pixelX = cellX * Cell.CELL_WIDTH;
                pixelY = cellY * Cell.CELL_HEIGHT;

                neighbor.setHasChunk(true);

                timeToMove = MOVE_DELAY;
            }
            return;
        }

        //50/50 chance of checking left or right first
        if(Math.random() < 0.5) {
            //Check bottom left cell
            neighborY = Game.clamp(cellY + 1, 0, world.getNumY() - 1);
            neighborX = Game.clamp(cellX - 1, 0, world.getNumX() - 1);
            neighbor = world.getCell(neighborX, neighborY);

            if(neighbor.isAir() && !neighbor.hasChunk()) {
                timeToMove--;
                if(timeToMove <= 0) {
                    world.getCell(cellX, cellY).setHasChunk(false);
                    cellX = neighborX;
                    cellY = neighborY;
                    pixelX = cellX * Cell.CELL_WIDTH;
                    pixelY = cellY * Cell.CELL_HEIGHT;

                    neighbor.setHasChunk(true);

                    timeToMove = MOVE_DELAY;
                }
            }
            //Check bottom right cell
            neighborY = Game.clamp(cellY + 1, 0, world.getNumY() - 1);
            neighborX = Game.clamp(cellX + 1, 0, world.getNumX() - 1);
            neighbor = world.getCell(neighborX, neighborY);

            if(neighbor.isAir() && !neighbor.hasChunk()) {
                timeToMove--;
                if(timeToMove <= 0) {
                    world.getCell(cellX, cellY).setHasChunk(false);
                    cellX = neighborX;
                    cellY = neighborY;
                    pixelX = cellX * Cell.CELL_WIDTH;
                    pixelY = cellY * Cell.CELL_HEIGHT;

                    neighbor.setHasChunk(true);

                    timeToMove = MOVE_DELAY;
                }
            }
        } else {
            //Check bottom left cell
            neighborY = Game.clamp(cellY + 1, 0, world.getNumY() - 1);
            neighborX = Game.clamp(cellX - 1, 0, world.getNumX() - 1);
            neighbor = world.getCell(neighborX, neighborY);

            if(neighbor.isAir() && !neighbor.hasChunk()) {
                timeToMove--;
                if(timeToMove <= 0) {
                    world.getCell(cellX, cellY).setHasChunk(false);
                    cellX = neighborX;
                    cellY = neighborY;
                    pixelX = cellX * Cell.CELL_WIDTH;
                    pixelY = cellY * Cell.CELL_HEIGHT;

                    neighbor.setHasChunk(true);

                    timeToMove = MOVE_DELAY;
                }
            }
            //Check bottom right cell
            neighborY = Game.clamp(cellY + 1, 0, world.getNumY() - 1);
            neighborX = Game.clamp(cellX + 1, 0, world.getNumX() - 1);
            neighbor = world.getCell(neighborX, neighborY);

            if(neighbor.isAir() && !neighbor.hasChunk()) {
                timeToMove--;
                if(timeToMove <= 0) {
                    world.getCell(cellX, cellY).setHasChunk(false);
                    cellX = neighborX;
                    cellY = neighborY;
                    pixelX = cellX * Cell.CELL_WIDTH;
                    pixelY = cellY * Cell.CELL_HEIGHT;

                    neighbor.setHasChunk(true);

                    timeToMove = MOVE_DELAY;
                }
            }
        }
    }

    private void checkConveyors() {
        Cell currCell = world.getCell(cellX, cellY);
        if(currCell.getItem() != null) {
            if(currCell.getItem().getID() == GameID.CONVEYOR) {
                 Conveyor conv = (Conveyor) currCell.getItem();
                 Cell neighbor = world.getCell(Game.clamp(cellX + conv.getDirection(), 0, world.getNumX() - 1), cellY);
                 if(neighbor.isAir() && !neighbor.hasChunk()) {
                     timeToMove--;
                     if(timeToMove <= 0) {
                         currCell.setHasChunk(false);
                         cellX = neighbor.getCellX();
                         cellY = neighbor.getCellY();
                         pixelX = cellX * Cell.CELL_WIDTH;
                         pixelY = cellY * Cell.CELL_HEIGHT;
                         neighbor.setHasChunk(true);

                         timeToMove = MOVE_DELAY;
                     }
                 }
            }
        }

    }
}
