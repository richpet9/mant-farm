package com.minegame.core;

import com.minegame.data.MineQueue;
import com.minegame.world.Cell;
import com.minegame.world.Element;
import com.minegame.world.Mant;
import com.minegame.world.World;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Handler is the main driving force of the engine, storing each
 * GameObject and telling them when to tick() and render()
 */
public class Handler {
    private ArrayList<Cell> cells;                                 //Big array of cells
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();    //All other GameObjects
    private Camera camera;
    private World world;
    private MineQueue mineQueue;
    private String clickMode = "SPAWN";
    private boolean worldGenerated = false;

    public Handler(World world) {
        this.world = world;
        this.mineQueue = new MineQueue(100);
    }

    public void tick() {
        if(worldGenerated) {
            for(Cell cell : cells) {
                //Camera fix pixel shift
                cell.setCameraXY(camera.getX(), camera.getY());

                //If the cells has gravity and isn't in the last row
                if(cell.falls() && cell.getCellY() != world.getNumY() - 1) {
                    Cell cellBelow = world.getCell(cell.getCellX(), cell.getCellY() + 1);
                    if(cellBelow.isAir()) {
                        world.swapCells(cell, cellBelow);
                    }
                }

                //TODO: This is for debugging & the clear queue button
                if(mineQueue.isEmpty() && cell.isOverlayOn()) cell.setOverlay(false);

                //Tick the cell
                cell.tick();
            }
        }

        //For every object that isn't a cell
        for (int i = 0; i < objects.size(); i++) {
            GameObject object = objects.get(i);
            //Give it camera info
            object.setCameraXY(camera.getX(), camera.getY());

            //make object fall
            if (object.falls()) {
                object.setVelY(object.getVelY() + -0.2);
            }

            //If this object is a MANT
            if (object.getID() == GameID.MANT) {
                Mant mant = (Mant) object;
                //TODO: This logic probably shouldn't be here and DEFINITELY needs to be reworked
                //If the minequeue isn't empty, get this mant to that cell
                if (!mineQueue.isEmpty()) {
                    Cell cellToSeek = mineQueue.peek();
                    //If this cell is to the right
                    if (object.getCellX() < cellToSeek.getCellX()) {
                        mant.setVelX(2);
                    } else if (object.getCellX() > cellToSeek.getCellX()) {
                        //Cell is to the left
                        mant.setVelX(-2);
                    } else {
                        mant.setVelX(0);
                        mineQueue.dequeue().setElement(Element.AIR);
                    }
                } else {
                    mant.setVelX(0);
                }
            }

            //Tick the object itself
            object.tick();
        }
    }

    public void render(Graphics2D g) {
        if(worldGenerated) {
            //Background, sky, if you would
            g.setColor(new Color(0xD6AF8D));
            g.fillRect(0, 0, world.getWidth(), world.getHeight());
            //Render every cell
            for(Cell cell : cells) {
                cell.render(g);
            }
        }

        //Render every game object
        for (int i = 0; i < objects.size(); i++) {
            GameObject object = objects.get(i);
            object.render(g);
        }
    }

    public ArrayList<GameObject> getObjects() {
        return objects;
    }
    public Camera getCamera() {
        return camera;
    }
    public MineQueue getMineQueue() {
        return mineQueue;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void setClickMode(String s) {
        this.clickMode = s;
    }

    public void addObject(GameObject obj) {
        objects.add(obj);
    }

    public void removeObject(GameObject obj) {
        objects.remove(obj);
    }

    /**
     * Tells the World to generate and add's all the Cells it creates to a list here
     */
    public void generateWorld() {
        world.generateWorld();
        //Get the cells
        Cell[][] worldArray = world.getCells();

        //Make cells a new ArrayList of the size of the world
        this.cells = new ArrayList<Cell>(world.getNumY() * world.getNumY());
        for(int x = 0; x < worldArray.length; x++) {
            this.cells.addAll(Arrays.asList(worldArray[x]));
        }

        worldGenerated = true;
    }

    /**
     * Handle a click event, called in MouseListeners
     * @param pixelX The x location of the click
     * @param pixelY The y location of the click
     * @param cellX The x cell location of the click (RELATIVE TO VIEWPORT)
     * @param cellY The Y cell location of the click (RELATIVE TO VIEWPORT)
     */
    public void handleClick(int pixelX, int pixelY, int cellX, int cellY) {
        //MouseListener doesn't have access to the camera, so it sends us
        //we convert it here
        int trueX = cellX + (camera.getX() / Cell.CELL_WIDTH);
        int trueY = cellY + (camera.getY() / Cell.CELL_HEIGHT);

        switch (clickMode) {
            case "SPAWN":
                //Add a mant to the map
                addObject(new Mant(world, trueX, trueY, Color.WHITE));
                break;
            case "MINE":
                //Queue up the clicked cell for digging
                mineQueue.enqueue(world.getCell(trueX, trueY));
                world.getCell(trueX, trueY).setOverlay(true);
                break;
            case "DIRT":
                world.getCell(trueX, trueY).setElement(Element.DIRT);
        }
    }
}
