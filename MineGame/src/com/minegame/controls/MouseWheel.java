package com.minegame.controls;

import com.minegame.core.*;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Mouse Wheel Listener variant for tracking
 */
public class MouseWheel extends Mouse implements MouseWheelListener {
    private Camera camera;

    public MouseWheel(MouseHandler mHandler, Camera camera) {
        super(mHandler);
        this.camera = camera;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(Game.GAMESTATE == GameState.PLAYING) {
            double numClicks = e.getPreciseWheelRotation();
            double targetY = camera.getY() + (numClicks * 10);
            double y = camera.getY();
            camera.setY((int) Math.round(y + (targetY - y) * 0.1));
        }
    }
}
