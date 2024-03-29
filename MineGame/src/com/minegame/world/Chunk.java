package com.minegame.world;

import com.minegame.core.Game;
import com.minegame.core.GameID;
import com.minegame.core.GameObject;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

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
        this.w = Cell.CELL_WIDTH - 2;
        this.h = Cell.CELL_HEIGHT - 2;
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
        g.fillOval((pixelX - cameraX) + 1, (pixelY - cameraY) + 1, w, h);
    }

    private void checkCollisions() {
        //This checks collision with conveyor belts
        if(world.getCell(cellX, cellY).hasItem()) {
            GameID id = world.getCell(cellX, cellY).getItem().getID();
            if(id == GameID.CONVEYOR || id == GameID.ELEVATOR) {
                //Our current cell has a conveyor, so cancel stay right where we are
                return;
            }
        }
        //Get the cell below us
        int neighborY = Game.clamp(cellY + 1, 0, world.getNumY() - 1);
        int neighborX = Game.clamp(cellX, 0, world.getNumX() - 1);
        Cell neighbor = world.getCell(neighborX, neighborY);

        //If the cell below us is air and doesn't have a chunk, move to it
        if(neighbor.isAir() && neighbor.hasNoChunk()) {
            //If the cell below us has an elevator, don't drop this chunk
            //If we are at the top of an elevator, fall to either left or right, determined by the next if
            if(!(neighbor.hasItem() && neighbor.getItem().getID() == GameID.ELEVATOR)) {
                moveToCell(neighbor);
                return; //Early exit since the cell below us is clear, we are going there, don't check sides
            }
        }

        //50/50 chance of checking left or right first
        if(Math.random() < 0.5) {
            //Check bottom left cell
            neighborY = Game.clamp(cellY + 1, 0, world.getNumY() - 1);
            neighborX = Game.clamp(cellX - 1, 0, world.getNumX() - 1);
            neighbor = world.getCell(neighborX, neighborY);

            if(neighbor.isAir() && neighbor.hasNoChunk()) {
                moveToCell(neighbor);
                return; //Early exit because we found a cell to move to
            }
            //Check bottom right cell
            neighborY = Game.clamp(cellY + 1, 0, world.getNumY() - 1);
            neighborX = Game.clamp(cellX + 1, 0, world.getNumX() - 1);
            neighbor = world.getCell(neighborX, neighborY);

            if(neighbor.isAir() && neighbor.hasNoChunk()) {
                moveToCell(neighbor);
            }
        } else {
            //Check bottom right cell
            neighborY = Game.clamp(cellY + 1, 0, world.getNumY() - 1);
            neighborX = Game.clamp(cellX + 1, 0, world.getNumX() - 1);
            neighbor = world.getCell(neighborX, neighborY);

            if(neighbor.isAir() && neighbor.hasNoChunk()) {
                moveToCell(neighbor);
                return; //Early exit because we found a cell to move to
            }
            //Check bottom left cell
            neighborY = Game.clamp(cellY + 1, 0, world.getNumY() - 1);
            neighborX = Game.clamp(cellX - 1, 0, world.getNumX() - 1);
            neighbor = world.getCell(neighborX, neighborY);

            if(neighbor.isAir() && neighbor.hasNoChunk()) {
                moveToCell(neighbor);
            }
        }
    }

    /**
     * checkConveyors checks if this chunk is on any conveyors or elevators
     */
    private void checkConveyors() {
        Cell currCell = world.getCell(cellX, cellY);
        if(currCell.getItem() != null) {
            if(currCell.getItem().getID() == GameID.CONVEYOR) {
                 Conveyor conv = (Conveyor) currCell.getItem();
                 Cell neighbor = world.getCell(Game.clamp(cellX + conv.getDirection(), 0, world.getNumX() - 1), cellY);
                 if(neighbor.isAir() && neighbor.hasNoChunk()) {
                     moveToCell(neighbor);
                 }
            } else if(currCell.getItem().getID() == GameID.ELEVATOR) {
                Cell neighbor = world.getCell(cellX, Game.clamp(cellY - 1, 0, world.getNumY() - 1));
                if(neighbor.isAir() && neighbor.hasNoChunk()) {
                    moveToCell(neighbor);
                }
            }
        }

    }

    /**
     * moveToCell will move this chunk to the specified cell, if timer permits
     * @param neighbor The Cell to move to.
     */
    public void moveToCell(Cell neighbor) {
        timeToMove--;
        if(timeToMove <= 0) {
            world.getCell(cellX, cellY).setHasChunk(false);
            cellX = neighbor.getCellX();
            cellY = neighbor.getCellY();
            pixelX = cellX * Cell.CELL_WIDTH;
            pixelY = cellY * Cell.CELL_HEIGHT;
            neighbor.setHasChunk(true);

            timeToMove = MOVE_DELAY;
        }

    }

}
