package com.minegame.controls;

import com.minegame.core.Handler;
import com.minegame.world.Cell;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Key Input listener for camera controls
 */
public class KeyInput implements KeyListener {
    private static final int SHIFT_MULTIPLE = Cell.CELL_WIDTH;
    private Handler handler;
    private ArrayList<Integer> keysPressed = new ArrayList<Integer>();

    public KeyInput(Handler h) {
        this.handler = h;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keysPressed.add(e.getKeyCode());

        int multiple = SHIFT_MULTIPLE;
        if(keysPressed.size() > 1) {
            //Multiple keys pressed, set shift to true if shift is pressed
            multiple = (keysPressed.contains(KeyEvent.VK_SHIFT)) ? 1 : SHIFT_MULTIPLE;
        }

        //PRESSED W
//        if(e.getKeyCode() == KeyEvent.VK_W) {
//            //handler.getCamera().setY((handler.getCamera().getY() - (10 * multiple)));
//        }

        //PRESSED A
        if(e.getKeyCode() == KeyEvent.VK_A) {
            //handler.getCamera().setX((handler.getCamera().getX() - (10 * multiple)));
            handler.getPlayer().setVelX(-2);
        }

        //PRESSED S
//        if(e.getKeyCode() == KeyEvent.VK_S) {
//            //handler.getCamera().setY((handler.getCamera().getY() + (10 * multiple)));
//        }

        //PRESSED D
        if(e.getKeyCode() == KeyEvent.VK_D) {
            //handler.getCamera().setX((handler.getCamera().getX() + (10 * multiple)));
            handler.getPlayer().setVelX(2);
        }

        //PRESSED SPACE
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            handler.getPlayer().setVelY(5);
        }

        //PRESSED M
        if(e.getKeyCode() == KeyEvent.VK_M) {
            handler.setClickMode("SPAWN");
        }

        //PRESSED N
        if(e.getKeyCode() == KeyEvent.VK_N) {
            handler.setClickMode("MINE");
        }

        //PRESSED B
        if(e.getKeyCode() == KeyEvent.VK_B) {
            handler.setClickMode("BOMB");
        }

        //PRESSED V
        if(e.getKeyCode() == KeyEvent.VK_V) {
            handler.setClickMode("ARM");
        }

        //PRESSED C
        if(e.getKeyCode() == KeyEvent.VK_C) {
            handler.setClickMode("CONVEYOR");
        }

        //PRESSED X
        if(e.getKeyCode() == KeyEvent.VK_X) {
            handler.setClickMode("CONVEYOR_DIR");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed.remove((Integer) e.getKeyCode());

        //If pressed A or D, reset horizontal velocity
        if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_D) {
            handler.getPlayer().setVelX(0);
        }
    }
}
