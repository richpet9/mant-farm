package com.minegame.controls;

import com.minegame.core.Handler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Key Input listener for camera controls
 */
public class KeyInput implements KeyListener {
    private Handler handler;
    private ArrayList<Integer> keysPressed = new ArrayList<Integer>();
    private static int SHIFT_MULTIPLE = 10;

    public KeyInput(Handler h) {
        this.handler = h;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keysPressed.add(e.getKeyCode());

        int multiple = 1;
        if(keysPressed.size() > 1) {
            //Multiple keys pressed, set shift to true if shift is pressed
            multiple = (keysPressed.contains(KeyEvent.VK_SHIFT)) ? 1 : SHIFT_MULTIPLE;
        }

        //PRESSED W
        if(e.getKeyCode() == KeyEvent.VK_W) {
            handler.getCamera().setY((handler.getCamera().getY() - (10 * multiple)));
        }

        //PRESSED A
        if(e.getKeyCode() == KeyEvent.VK_A) {
            handler.getCamera().setX((handler.getCamera().getX() - (10 * multiple)));
        }

        //PRESSED S
        if(e.getKeyCode() == KeyEvent.VK_S) {
            handler.getCamera().setY((handler.getCamera().getY() + (10 * multiple)));
        }

        //PRESSED D
        if(e.getKeyCode() == KeyEvent.VK_D) {
            handler.getCamera().setX((handler.getCamera().getX() + (10 * multiple)));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keysPressed.remove((Integer) e.getKeyCode());
    }
}
