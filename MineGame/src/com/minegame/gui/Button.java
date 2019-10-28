package com.minegame.gui;

import java.awt.*;

/**
 * Abstract class to represent clickable buttons
 * This object has a collection in Menu that is looped through in MouseClick
 */
public abstract class Button extends UserInterface {
    private String value;
    private Color color;
    protected boolean mouseOver = false;

    public Button(String val, int x, int y, Color c) {
        super(x, y, c);

        this.value = val;
        this.x = x;
        this.y = y;
        this.color = c;
    }

    public String getValue() { return value; }
    public void setValue(String s) { value = s; }

    public void tick() {

    };

    @Override
    public void render(Graphics2D g) {
        w = getTextWidth(g, value, g.getFont());
        h = getTextHeight(g, value, g.getFont());

        Color oldColor = g.getColor();
        g.setColor(color);
        g.drawString(value, x, y);

        g.setColor(oldColor);
    }

    public abstract void click();

}
