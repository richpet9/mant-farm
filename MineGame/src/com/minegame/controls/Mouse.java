package com.minegame.controls;

import com.minegame.core.GameObject;
import com.minegame.core.Handler;

/**
 * Abstract class to make getting mouse info easier from our listeners.
 */
public abstract class Mouse {
    protected int mouseX;
    protected int mouseY;
    protected int mouseCellX;
    protected int mouseCellY;
    protected Handler handler;

    public Mouse(Handler handler) {
        this.handler = handler;
    }

    public boolean mouseOverXY(int x, int y, int w, int h) {
        return (mouseX >= x && mouseX <= x + w) && (mouseY >= y && mouseY <= y + h);
    }

    public boolean mouseOverObject(GameObject obj) {
        return (mouseX > obj.getPixelX() && mouseX < obj.getW()) && (mouseY > obj.getPixelY() && mouseY < obj.getH());
    }
}
