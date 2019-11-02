package com.minegame.controls;

import com.minegame.core.MouseHandler;
import com.minegame.world.Cell;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * A class responsible for storing all mouse movement
 * information. Used to get the current mouse x and y
 */
public class MouseMove extends Mouse implements MouseMotionListener {
    public MouseMove(MouseHandler mHandler) {
        super(mHandler);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseCellX = e.getX() / Cell.CELL_WIDTH;
        mouseCellY = e.getY() / Cell.CELL_HEIGHT;

        mHandler.setMouseCellXY(mouseCellX, mouseCellY);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseCellX = e.getX() / Cell.CELL_WIDTH;
        mouseCellY = e.getY() / Cell.CELL_HEIGHT;

        int xDif = mHandler.getStartedDragX() - mouseCellX;
        int yDif = mHandler.getStartedDragY() - mouseCellY;
    }

    public int getX() {
        return this.mouseX;
    }
    public int getY() {
        return this.mouseY;
    }
    public int getCellX() {
        return this.mouseCellX;
    }
    public int getCellY() {
        return this.mouseCellY;
    }
}
