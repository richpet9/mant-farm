package com.minegame.controls;

import com.minegame.core.Camera;
import com.minegame.core.Game;
import com.minegame.core.GameState;
import com.minegame.core.MouseHandler;

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
//        if(Game.GAMESTATE == GameState.PLAYING) {
//            double numClicks = e.getPreciseWheelRotation();
//            int targetY = (int) (camera.getY() + (numClicks * 10));
//            camera.setY(targetY);
//        }
        //TODO: let's change zoom with mouse wheel
    }
}
