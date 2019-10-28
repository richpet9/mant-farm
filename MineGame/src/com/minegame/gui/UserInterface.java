package com.minegame.gui;

import java.awt.*;

/**
 * Abstract class to represent any GUI object
 */
public abstract class UserInterface {
    protected int x;
    protected int y;
    protected int w;
    protected int h;
    protected Color color;

    public UserInterface(int x, int y, Color c) {
        this.x = x;
        this.y = y;
        this.color = c;
    }

    public int getX() { return this.x; }
    public int getY() { return y; }
    public int getW() { return w; }
    public int getH() { return h; }
    public Color getColor() { return color; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setW(int w) { this.w = w; }
    public void setH(int h) { this.h = h; }
    public void setColor(Color color) { this.color = color; }

    public abstract void tick();
    public abstract void render(Graphics2D g);

    public static int getTextWidth(Graphics2D g, String s, Font f) {
        return g.getFontMetrics(f).stringWidth(s);
    }

    public static int getTextHeight(Graphics2D g, String s, Font f) {
        return g.getFontMetrics(f).getHeight();
    }
}
