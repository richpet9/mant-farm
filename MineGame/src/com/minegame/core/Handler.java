package com.minegame.core;

import com.minegame.jobs.BombJob;
import com.minegame.jobs.Job;
import com.minegame.jobs.JobQueue;
import com.minegame.jobs.MineJob;
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
    private ArrayList<GameObject> objects = new ArrayList<GameObject>(64);    //All other GameObjects
    private ArrayList<Cell> selection = new ArrayList<Cell>(128);
    private ArrayList<Job> inProgressJobs;
    private JobQueue jobQueue;
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
        //TODO: Maybe we should move cells back to the world class?
        if(worldGenerated) {
            for(Cell cell : cells) {
                //Camera fix pixel shift
                cell.setCameraXY(camera.getX(), camera.getY());

                //If the cells has gravity and isn't in the last row
                if(cell.dropChunk() != null) {
                    Chunk newChunk = new Chunk(world, cell.dropChunk(), cell.cellX, cell.getCellY());
                    addObject(newChunk);
                    cell.setDropChunk(null);
                    cell.setHasChunk(true);
                }

                //Tick the cell
                cell.tick();
            }
        }

        //For every object that isn't a cell
        for(Iterator<GameObject> iterator = objects.iterator(); iterator.hasNext();) {
            GameObject object = iterator.next();

            //If object is queued for destruction
            if(object.isDestroy()) iterator.remove();

            //Give it camera info
            object.setCameraXY(camera.getX(), camera.getY());

            //make object fall
            if(object.falls()) {
                object.setVelY(object.getVelY() + -0.2);
            }

            //If this object is a MANT
            if(object.getID() == GameID.MANT) {
                Mant mant = (Mant) object;
                //If the minequeue isn't empty, and this mant isn't assigned a cell already
                if(!jobQueue.isEmpty() && mant.getJob() == null) {
                    //Assign this job to the mant
                    mant.setJob(jobQueue.peek());
                    //Add this job to the in-progress jobs
                    inProgressJobs.add(jobQueue.dequeue());
                }
            }

            //If this object is a BOMB
            if(object.getID() == GameID.BOMB) {
                Bomb bomb = (Bomb) object;
                if(bomb.isDetonated()) {
                    //Remove the bomb from the world
                    iterator.remove();
                    new Explosion(world, bomb.getCellX(), bomb.getCellY(), bomb.getRadius());
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
            g.setColor(new Color(0xA07D64));
            g.fillRect(0, 0, world.getWidth(), world.getHeight());
            //Render every cell
            for(Cell cell : cells) {
                cell.render(g);
            }
        }

        //Render every game object
        //TODO: check object z-index and render according to that
        for(int i = 0; i < objects.size(); i++) {
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
    public ArrayList<Cell> getSelection() {
        return selection;
    }

    //Setters
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    public void setClickMode(String s) {
        this.clickMode = s;
    }
    public void setSelection(ArrayList<Cell> selection) {
        this.selection = selection;
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
        //Create the empty list of cells. This should speed up the next process.
        this.cells = new ArrayList<Cell>(worldArray.length * worldArray[0].length);

        //Make cells an ArrayList of the world cells
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
        //TODO: Make MouseHandler class in core
        //MouseListener doesn't have access to the camera, so it sends us
        //we convert it here
        int trueX = (pixelX + camera.getX()) / Cell.CELL_WIDTH;
        int trueY = (pixelY + camera.getY()) / Cell.CELL_HEIGHT;
        Cell cell = world.getCell(trueX, trueY);
        GameObject item = cell.getItem();

        //TODO: "Selection" should always be size > 0, either 1 cell or or many. No need
        // to check and do different things if didn't drag-- think Rimworld

        switch(clickMode) {
            case "SPAWN":
                //Add a mant to the map if clicked cell and cell below are both air
                if(cell.isAir() && world.getCell(trueX, trueY + 1).isAir()) {
                    //If the cell we clicked and one below it is air
                    addObject(new Mant(world, trueX, trueY, Color.WHITE));
                }
                break;
            case "MINE":
                if(selection.size() > 0) {
                    for(Cell selectedCell : selection) {
                        //Queue up the clicked cell for digging
                        if(selectedCell.isAir()) continue;

                        jobQueue.enqueue(new MineJob(selectedCell));
                        selectedCell.setOverlay(true);
                    }
                } else {
                    //Queue up the clicked cell for digging
                    if(cell.isAir()) return;

                    jobQueue.enqueue(new MineJob(cell));
                    cell.setOverlay(true);
                }
                break;
            case "DIRT":
                cell.setElement(Element.DIRT);
                break;
            case "BOMB":
                //If we clicked air, and the cell below us isn't air, and the cell doesn't contain an item...
                if(cell.isAir() && !world.getCell(trueX, trueY + 1).isAir() && cell.getItem() == null) {
                    Bomb bomb = new Bomb(trueX, trueY, 2, 5);
                    addObject(bomb);
                    cell.setItem(bomb);
                }
                break;
            case "ARM":
                //See if we clicked on a bomb
                if(item != null) {
                    if(cell.getItem().getID() == GameID.BOMB) {
                        //And add an arm job
                        Bomb bomb = (Bomb) cell.getItem();
                        if(bomb.hasJob()) return;
                        jobQueue.enqueue(new BombJob(cell, bomb, BombJob.JobType.ARM));
                    }
                }
                break;
            case "CONVEYOR":
                //If we clicked air, and the cell doesn't contain an item...
                if(cell.isAir() && cell.getItem() == null) {
                    Conveyor conv = new Conveyor(trueX, trueY, -1);
                    addObject(conv);
                    cell.setItem(conv);
                }
                break;
            case "CONVEYOR_DIR":
                //See if we clicked on a conveyor
                if(item != null) {
                    if(cell.getItem().getID() == GameID.CONVEYOR) {
                        //And add an arm job
                        Conveyor conv = (Conveyor) cell.getItem();
                        conv.setDirection(conv.getDirection() * -1);
                    }
                }
                break;

        }
    }

    public void makeSelection(int xStart, int xEnd, int y) {
        for(int x = xStart; x < xEnd + 1; x++) {
            selection.add(world.getCell(x, y));
        }
    }
}
