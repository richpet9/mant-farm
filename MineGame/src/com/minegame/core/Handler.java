package com.minegame.core;

import com.minegame.controls.Mouse;
import com.minegame.world.Cell;
import com.minegame.world.Element;
import com.minegame.world.Mant;
import com.minegame.world.World;

import java.awt.*;
import java.util.ArrayList;

/**
 * Handler is the main driving force of the engine, storing each
 * GameObject and telling them when to tick() and render()
 */
public class Handler {
    private ArrayList<Cell> cells;                                 //Big array of cells
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();    //All other GameObjects
    private Camera camera;
    private World world;
    private String clickMode = "SPAWN";
    private boolean worldGenerated = false;

    public Handler(World world) {
        this.world = world;
    }

    public void tick() {
        if(worldGenerated) {
            for(Cell cell : cells) {
                //Camera fix pixel shift
                cell.setCameraXY(camera.getX(), camera.getY());

                if(cell.falls() && cell.getCellY() != world.getNumY() - 1) {
                    if(world.getCell(cell.getCellX(), cell.getCellY() + 1).isAir()) {
                        //TODO: SOME HOW MAKE THIS CELL FALL
                    }
                }

                cell.tick();
            }
        }

        for (GameObject object : objects) {
            object.setCameraXY(camera.getX(), camera.getY());

            if(object.falls()) {
                //make object fall
                object.setVelY(-5);
            }

            if(object.collides()) {
                checkCollision(object);
            }

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
        for (GameObject object : objects) {
            object.render(g);
        }
    }

    public ArrayList<GameObject> getObjects() {
        return objects;
    }
    public Camera getCamera() {
        return camera;
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

    public void generateWorld() {
        world.generateWorld();
        //Get the cells
        Cell[][] worldArray = world.getCells();

        //Make cells a new ArrayList of the size of the world
        this.cells = new ArrayList<Cell>(world.getNumY() * world.getNumY());
        for(int x = 0; x < worldArray.length; x++) {
            for(int y = 0; y < worldArray[x].length; y++) {
                this.cells.add(worldArray[x][y]);
            }
        }

        worldGenerated = true;
    }

    public void handleClick(int pixelX, int pixelY, int cellX, int cellY) {
        //MouseListener doesn't have access to the camera, so it sends us
        //we convert it here
        int trueX = cellX + (camera.getX() / Cell.CELL_WIDTH);
        int trueY = cellY + (camera.getY() / Cell.CELL_HEIGHT);

        switch(clickMode) {
            case "SPAWN":
                addObject(new Mant(trueX, trueY, Color.WHITE));
                break;
            case "MINE":
//                world.getCell(trueX, trueY);
        }
    }

    public void checkCollision(GameObject object) {
        //First check cell collision
        //We can save memory by only checking the cells within 3 cells of us
        for(int x = object.getCellX() - 3; x < world.getNumX() && x < object.getCellX() + 3; x++) {
            if(x < 0) continue;
            for(int y = object.getCellY() - 3; y < world.getNumY() && y < object.getCellY() + 3; y++) {
                if(y < 0) continue;
                //For every cell
                Cell cell = world.getCell(x, y);
                //We have to compute where this object is going to be next tick
                int newY = object.getPixelY() - (int) object.getVelY();
                //And get that bounding rectangle
                Rectangle boundingRect = new Rectangle(object.getPixelX(), newY, object.getW(), object.getH());

                //If we intersect and the cell is not air, stop motion
                if(boundingRect.intersects(cell.getBounds()) && !cell.isAir()) {
                    //Collision
                    object.setVelY(0);
                }
            }
        }
    }

}
