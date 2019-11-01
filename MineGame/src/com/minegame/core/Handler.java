package com.minegame.core;

import com.minegame.data.Job;
import com.minegame.data.JobQueue;
import com.minegame.data.MineJob;
import com.minegame.world.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Handler is the main driving force of the engine, storing each
 * GameObject and telling them when to tick() and render()
 */
public class Handler {
    private ArrayList<Cell> cells;                                 //Big array of cells
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();    //All other GameObjects
    private JobQueue jobQueue;
    private ArrayList<Job> inProgressJobs;
    private Camera camera;
    private World world;
    private String clickMode = "SPAWN";
    private boolean worldGenerated = false;

    public Handler(World world) {
        this.world = world;
        this.jobQueue = new JobQueue(64);
        this.inProgressJobs = new ArrayList<Job>(64);
    }

    public void tick() {
        if(worldGenerated) {
            for(Cell cell : cells) {
                //Camera fix pixel shift
                cell.setCameraXY(camera.getX(), camera.getY());

                //If the cells has gravity and isn't in the last row
                if(cell.dropChunk() != null) {
                    Chunk newChunk = new Chunk(world, cell.dropChunk(), cell.cellX, cell.getCellY());
                    addObject(newChunk);
                    cell.setHasChunk(true);
                    cell.setDropChunk(null);
                }

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
                //If the minequeue isn't empty, and this mant isn't assigned a cell already
                if(!jobQueue.isEmpty() && mant.getJob() == null) {
                    //Assign this job to the mant
                    mant.setJob(jobQueue.peek());
                    //Add this job to the in-progress jobs
                    inProgressJobs.add(jobQueue.dequeue());
                }
            }

            //Tick the object itself
            object.tick();
        }

        //For every in progress job
        //This syntax was made my IntelliJ, how neat
        inProgressJobs.removeIf(Job::isComplete);
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
        //TODO: Add Mants in their own list so they can be on top z-level
        for (int i = 0; i < objects.size(); i++) {
            GameObject object = objects.get(i);
            object.render(g);
        }
    }

    //Getters
    public ArrayList<GameObject> getObjects() {
        return objects;
    }
    public JobQueue getJobQueue() {
        return jobQueue;
    };
    public ArrayList<Job> getInProgressJobs() {
        return inProgressJobs;
    }
    public Camera getCamera() {
        return camera;
    }

    //Setters
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
        objects.add(new Chunk(world, Element.IRON, 10, 50));
        objects.add(new Chunk(world, Element.COPPER, 12, 51));
        objects.add(new Chunk(world, Element.GOLD, 14, 51));
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
                Cell cell = world.getCell(trueX, trueY);
                if(cell.isAir()) return;

                jobQueue.enqueue(new MineJob(cell));
                cell.setOverlay(true);
                break;
            case "DIRT":
                world.getCell(trueX, trueY).setElement(Element.DIRT);
        }
    }
}
