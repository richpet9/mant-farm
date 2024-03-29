package com.minegame.core;

import com.minegame.jobs.Job;
import com.minegame.jobs.JobQueue;
import com.minegame.world.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Handler is the main driving force of the engine, storing each
 * GameObject and telling them when to tick() and render()
 */
public class Handler {
    private ArrayList<GameObject> objects = new ArrayList<GameObject>(64);    //All other GameObjects
    private ArrayList<Job> inProgressJobs;
    private JobQueue jobQueue;
    private Camera camera;
    private World world;
    private MouseHandler mHandler;
    private GameObject activeObject;

    public Handler() {
        this.jobQueue = new JobQueue(64);
        this.inProgressJobs = new ArrayList<Job>(64);
    }

    public void tick() {
        if(Game.GAMESTATE == GameState.PLAYING) {
            //Tick the MouseHandler
            mHandler.tick();
            world.tick();


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
                        world.getCell(bomb.getCellX(), bomb.getCellY()).setItem(null);
                    }
                }

                //Tick the object itself
                object.tick();
            }

            //For every in progress job
            //This syntax was made my IntelliJ, how neat
            inProgressJobs.removeIf(Job::isComplete);
        }
    }

    public void render(Graphics2D g) {
        if(world != null) {
            //Background, sky, if you would
            g.setColor(new Color(0xA07D64));
            g.fillRect(0, 0, world.getWidth(), world.getHeight());
            //Render every cell
            world.render(g);
        }

        //Render every game object
        //TODO: check object z-index and render according to that
        for(int i = 0; i < objects.size(); i++) {
            GameObject object = objects.get(i);
            object.render(g);
        }

        //NOTE: this object does not care where the camera is, since it's not a real object in the game
        // thus, we don't update it in tick()
        if(activeObject != null) {
            activeObject.render(g);
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
    public GameObject getActiveObject() {
        return activeObject;
    }
    public World getWorld() {
        return world;
    }

    //Setters
    public void setCamera(Camera camera) {
        this.camera = camera;
    }
    public void addObject(GameObject obj) {
        objects.add(obj);
    }
    public void removeObject(GameObject obj) {
        objects.remove(obj);
    }
    public void setmHandler(MouseHandler mHandler) {
        this.mHandler = mHandler;
    }
    public void setActiveObject(GameObject object) {
        this.activeObject = object;
    }
    public void setClickMode(String clickMode) {
        this.mHandler.setClickMode(clickMode);
    }
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Tells the World to generate and add's all the Cells it creates to a list here
     */
    public void generateWorld() {
        this.world = new World(this, Game.WORLD_WIDTH, Game.WORLD_HEIGHT);
    }
}
