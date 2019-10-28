package com.minegame.core;

import com.minegame.controls.Mouse;
import com.minegame.world.Cell;
import com.minegame.world.Element;
import com.minegame.world.Mant;
import com.minegame.world.World;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Handler is the main driving force of the engine, storing each
 * GameObject and telling them when to tick() and render()
 */
public class Handler {
    private ArrayList<GameObject> objects = new ArrayList<GameObject>();
    private int index = 0;
    private Camera camera;
    private World world;

    public Handler(World world) {
        this.world = world;
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

    public void tick() {
        world.tick();

        for (int a = 0; a < objects.size(); a++) {
            GameObject object = objects.get(a);
            int pixX = object.getPixelX() - camera.getX();
            int pixY = object.getPixelY() - camera.getY();
            object.setPixelXY(pixX, pixY);

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
        world.render(g);

        for (GameObject object : objects) {
            object.render(g);
        }
    }

    public void addObject(GameObject obj) {
        objects.add(obj);
    }
    public void removeObject(GameObject obj) {
        objects.remove(obj);
    }
    public void generateWorld() {
        world.generateWorld();
    }

    public void handleClick(int pixelX, int pixelY, int cellX, int cellY) {
        addObject(new Mant(cellX, cellY, Color.WHITE));
    }

    public void checkCollision(GameObject object) {
        //First check cell collision
        //We can save memory by only checking the cells within 3 cells of us
        for(int x = object.getCellX() - 3; x < world.getNumX() && x < object.getCellX() + 3; x++) {
            for(int y = object.getCellY() - 3; y < world.getNumY() && y < object.getCellY() + 3; y++) {
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
