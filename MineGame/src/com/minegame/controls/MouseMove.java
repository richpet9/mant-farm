package com.minegame.controls;

import com.minegame.core.Handler;
import com.minegame.world.Cell;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

/**
 * A class responsible for storing all mouse movement
 * information. Used to get the current mouse x and y
 */
public class MouseMove extends Mouse implements MouseMotionListener {
    public MouseMove(Handler h) {
        super(h);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseCellX = e.getX() / Cell.CELL_WIDTH;
        mouseCellY = e.getY() / Cell.CELL_HEIGHT;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

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
