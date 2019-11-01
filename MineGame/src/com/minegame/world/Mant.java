package com.minegame.world;

import com.minegame.core.Game;
import com.minegame.core.GameID;
import com.minegame.core.GameObject;
import com.minegame.data.Job;
import com.minegame.exceptions.NullSpriteException;
import com.minegame.gui.ImageLoader;
import com.minegame.gui.Sprite;

import java.awt.*;

/**
 * As Mant is a class representing the moving figure of a character
 * on the screen.
 */
public class Mant extends GameObject {
    private static final int MANT_WIDTH = 1;    //IN CELLS
    private static final int MANT_HEIGHT = 2;   //IN CELLS
    private World world;
    private Job job;
    private Sprite icon;
    private boolean onGround = false;
    private boolean climbing = false;
    private boolean working = false;
    private int climbingFromPixelY = -1;

    public Mant(World world, int cellX, int cellY, Color color) {
        this.world = world;
        this.cellX = cellX;
        this.cellY = cellY;
        this.pixelX = cellX * Cell.CELL_WIDTH;
        this.pixelY = cellY * Cell.CELL_HEIGHT;
        this.w = Cell.CELL_WIDTH * MANT_WIDTH;
        this.h = Cell.CELL_HEIGHT * MANT_HEIGHT;

        this.id = GameID.MANT;
        this.usesGravity = true;
        this.usesCollision = true;
        this.moves = true;

        try {
            this.icon = ImageLoader.getSprite(this.id);
        } catch (NullSpriteException e) {
            e.printStackTrace();
            System.out.println("Sprite could not be found for object: " + this.id);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(pixelX, pixelY, w, h);
    }

    @Override
    public void tick() {
        if(job != null) {
            Cell targetCell = job.getTargetCell();
            Rectangle bounds = targetCell.getBounds();

            //If our left side is greater than the left side of the target cell
            if(pixelX > bounds.x + bounds.width) {
                //target cell is to the left
                velX = -2;
            } else if(pixelX + w < bounds.x) {  //If our right side is less than the right side of the target cell
                //target cell is to the right
                velX = 2;
            } else {
                //We are at target cell in x plane
                velX = 0;
                //If our Y cell location is one +/- our height away from the target cell, we can work the job
                if(cellY < (targetCell.getCellY() + MANT_HEIGHT + 1) && cellY > (targetCell.getCellY() - MANT_HEIGHT - 1)) {
                    job.work();
                } else {
                    //TODO: Add job cooldown before setting it to complete
                    job.setComplete(true);
                }
            }
        }

        if(climbing) {
            //If our current Y pos is one tile up, plus 3 pixels
            if(pixelY <= (climbingFromPixelY - Cell.CELL_HEIGHT)) {
                climbing = false;
                //Slow down our upwards speed when we stop climbing
                velY = 1;
            } else {
                velY = 2;
            }
            //Always block horizontal movement when climbing
            velX = 0;
        }

        checkCollisions();


        super.tick();
    }

    @Override
    public void render(Graphics2D g) {
        g.drawImage(icon.getImage(), pixelX - cameraX, pixelY - cameraY, null);
        g.setColor(Color.WHITE);
        g.drawString("c: " + cellX + " " + cellY, pixelX - cameraX, pixelY - cameraY - 5);
        g.drawString("p: " + pixelX + " " + pixelY, pixelX - cameraX, pixelY - cameraY - 20);
        if(job != null) {
            g.setColor(Color.RED);
            g.drawString("Has Job!", pixelX - cameraX, pixelY - cameraY - 35);
            if(working) {
                g.drawString("WORKING", pixelX - cameraX, pixelY - cameraY - 50);
            }
        }
    }

    private void checkCollisions() {
        int objCellWidth = MANT_WIDTH;
        int newX = pixelX + (int) Math.round(velX);
        int newY = pixelY - (int) Math.round(velY);

        if(velY != 0) {
            //Object is moving up or down, so check those tiles collisions
            for (int i = 0; i < MANT_WIDTH + 2; i++) {
                //Clamp i so we aren't trying to check cells that don't exist
                i = Game.clamp(i, MANT_WIDTH, world.getNumY() - MANT_HEIGHT);

                //For every cell that is vertically inline with our width, plus the two corners
                int x = (cellX -  1) + i;
                int y = cellY + ((velY > 0) ? -MANT_HEIGHT : MANT_HEIGHT);

                Cell cell = world.getCell(x, y);
                if(cell.isAir()) continue; //We can't collide with air, so don't check it

                //Rectangle used to represent object is where it will be, so we dont use getBounds()
                Rectangle boundingRect = new Rectangle(newX, newY, w, h);

                if (boundingRect.intersects(cell.getBounds())) {
                    //Collision so stop motion
                    velY = 0;
                    break;
                }
            }
        }

        if(velX != 0) {
            //Object is moving left or right, so check those tiles collisions
            //Add additional height to the collision box if we have a vertical velocity as well as horizontal
            // this checks the corners of our vector of motion
            for (int i = 0; i < MANT_HEIGHT + ((velY != 0) ? 2 : 0); i++) {
                //Clamp i so we aren't trying to check cells that don't exist
                i = Game.clamp(i, objCellWidth, world.getNumX() - objCellWidth);

                //For every cell that is vertically inline with us
                int x = cellX + ((velX > 0) ? objCellWidth : -objCellWidth);
                int y = cellY - ((velY != 0) ? 1 : 0) + i;

                Cell cell = world.getCell(x, y);
                if(cell.isAir()) continue;

                //Rectangle used to represent object is where it will be, so we dont use getBounds()
                Rectangle boundingRect = new Rectangle(newX, newY, w, h);

                //If any of the cells in the direction we are moving in,
                //relative to our height, are solid, stop motion in that direction
                if (boundingRect.intersects(cell.getBounds())) {
                    //If the cell above us is air, and we are Mant, go there
                    if (world.getCell(cellX, cellY - 1).isAir()) {
                        //If the mant is not climbing, make it climb
                        if (!climbing) {
                            climbing = true;
                            climbingFromPixelY = pixelY;
                        }
                    }
                    //Collision
                    velX = 0;
                    break;
                }
            }
        }
    }

    //Getters
    public Job getJob() {
        return job;
    }
    public boolean isOnGround() {
        return onGround;
    }
    public boolean isClimbing() {
        return climbing;
    }
    public boolean isWorking() {
        return working;
    }

    //Setters
    public void setJob(Job job) {
        this.job = job;
        //If we are assigning a job, set the worker to this guy
        if(job != null) this.job.setWorker(this);
    }
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
    public void setClimbing(boolean climbing) {
        this.climbing = climbing;
        climbingFromPixelY = pixelY;
    }
    public void setWorking(boolean working) {
        this.working = working;
    }
}
