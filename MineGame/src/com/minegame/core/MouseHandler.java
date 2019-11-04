package com.minegame.core;

import com.minegame.jobs.BombJob;
import com.minegame.jobs.MineJob;
import com.minegame.world.*;

import java.util.ArrayList;
import java.util.Iterator;

public class MouseHandler {
    private Handler handler;
    private World world;
    private String clickMode = "NONE";
    private boolean dragging = false;
    private ArrayList<Cell> selection = new ArrayList<Cell>(64);
    private int mouseCellX, mouseCellY, startedDragX, startedDragY;
    private int camXOffset, camYOffset;

    public MouseHandler(Handler handler, World world) {
        this.handler = handler;
        this.world = world;
    }

    public void tick() {
        camXOffset = (handler.getCamera().getX() / Cell.CELL_WIDTH);
        camYOffset = (handler.getCamera().getY() / Cell.CELL_HEIGHT);

        if(Game.GAMESTATE == GameState.PLAYING) {
            if(handler.getActiveObject() != null) {
                handler.getActiveObject().setCellXY(mouseCellX, mouseCellY);
            }
        }
    }

    //GETTERS
    public String getClickMode() {
        return clickMode;
    }
    public boolean isDragging() {
        return dragging;
    }
    public int getStartedDragX() {
        return startedDragX;
    }
    public int getStartedDragY() {
        return startedDragY;
    }
    public int getMouseCellX() {
        return mouseCellX;
    }
    public int getMouseCellY() {
        return mouseCellY;
    }

    //SETTERS
    public void setClickMode(String clickMode) {
        this.clickMode = clickMode;
        switch(clickMode) {
            case "SPAWN":
                handler.setActiveObject(new Mant(world, mouseCellX, mouseCellY));
                break;
            case "BOMB":
                handler.setActiveObject(new Bomb(mouseCellX, mouseCellY, 0, 0));
                break;
            case "CONVEYOR":
                handler.setActiveObject(new Conveyor(mouseCellX, mouseCellY, -1));
                break;
            case "ELEVATOR":
                handler.setActiveObject(new Elevator(mouseCellX, mouseCellY));
                break;
            default:
                handler.setActiveObject(null);
        }

        if(handler.getActiveObject() != null) handler.getActiveObject().setGhost(true);
    }
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }
    public void setStartedDragX(int startedDragX) {
        this.startedDragX = startedDragX;
    }
    public void setStartedDragY(int startedDragY) {
        this.startedDragY = startedDragY;
    }
    public void setMouseCellXY(int mouseCellX, int mouseCellY) {
        this.mouseCellX = mouseCellX;
        this.mouseCellY = mouseCellY;
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
        int trueX = cellX + camXOffset;
        int trueY = cellY + camYOffset;
        Cell cell = world.getCell(trueX, trueY);

        //TODO: "Selection" should always be size > 0, either 1 cell or or many. No need
        // to check and do different things if didn't drag-- think Rimworld

        switch(clickMode) {
            case "SPAWN":
                //Add a mant to the map if clicked cell and cell below are both air
                if(cell.isAir() && world.getCell(trueX, trueY + 1).isAir()) {
                    //If the cell we clicked and one below it is air
                    Mant newMant = new Mant(world, trueX, trueY);
                    handler.addObject(newMant);
                }
                break;
            case "MINE":
                //Queue up the clicked cell for digging
                for(Cell selectedCell : selection) {
                    if(selectedCell.isAir()) return;
                    handler.getJobQueue().enqueue(new MineJob(selectedCell));
                    selectedCell.setOverlay(true);
                }
                break;
            case "DIRT":
                cell.setElement(Element.DIRT);
                break;
            case "BOMB":
                //If we clicked air, and the cell below us isn't air, and the cell doesn't contain an item...
                if(cell.isAir() && !world.getCell(trueX, trueY + 1).isAir() && !cell.hasItem()) {
                    Bomb bomb = new Bomb(trueX, trueY, 2, 5);
                    handler.addObject(bomb);
                    cell.setItem(bomb);
                }
                break;
            case "ARM":
                //See if we clicked on a bomb
                if(cell.hasItem()) {
                    if(cell.getItem().getID() == GameID.BOMB) {
                        //And add an arm job
                        Bomb bomb = (Bomb) cell.getItem();
                        if(bomb.hasJob()) return;
                        handler.getJobQueue().enqueue(new BombJob(cell, bomb, BombJob.JobType.ARM));
                    }
                }
                break;
            case "CONVEYOR":
                //If we clicked air, and the cell doesn't contain an item...
                for(Cell selectedCell : selection) {
                    if(selectedCell.isAir() && !selectedCell.hasItem()) {
                        Conveyor conv = new Conveyor(selectedCell.getCellX(), selectedCell.getCellY(), -1);
                        handler.addObject(conv);
                        selectedCell.setItem(conv);
                    }
                }
                break;
            case "CONVEYOR_DIR":
                //See if we clicked on a conveyor
                for(Cell selectedCell : selection) {
                    if(selectedCell.hasItem()) {
                        if(selectedCell.getItem().getID() == GameID.CONVEYOR) {
                            //And add an arm job
                            Conveyor conv = (Conveyor) selectedCell.getItem();
                            conv.setDirection(conv.getDirection() * -1);
                        }
                    }
                }
                break;
            case "ELEVATOR":
                //If we clicked air, and the cell doesn't contain an item...
                for(Cell selectedCell : selection) {
                    if(selectedCell.isAir() && !selectedCell.hasItem()) {
                        Elevator elev = new Elevator(selectedCell.getCellX(), selectedCell.getCellY());
                        handler.addObject(elev);
                        selectedCell.setItem(elev);
                    }
                }
                break;
        }
    }

    public void checkSelection() {
        if(selection.size() > 0) {
            for(Iterator<Cell> iterator = selection.iterator(); iterator.hasNext();) {
                iterator.next().setOverlay(false);
                iterator.remove();
            }
        }

        if(dragging) {
            for(int i = 0; i < Math.abs(startedDragX - mouseCellX); i++) {
                //Loop through the difference between where we started the drag and where the mouse is now
                Cell cellToSelect;
                //If we are dragging to the right
                if(startedDragX < mouseCellX) {
                    cellToSelect = world.getCell((startedDragX + camXOffset) + i, (startedDragY + camYOffset));
                } else {
                    //We are dragging to the left
                    cellToSelect = world.getCell((startedDragX + camXOffset) - i, (startedDragY + camYOffset));
                }

                selection.add(cellToSelect);
                cellToSelect.setOverlay(true);
            }
        }
    }
}
