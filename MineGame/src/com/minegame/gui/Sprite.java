package com.minegame.gui;

import java.awt.Image;

/**
 * A sprite which can be assigned to any GameObject
 */
public class Sprite {
    private String id;
    private Image image;
    private Image ghostImage;
    private boolean ghost;

    Sprite(String id, Image img) {
        this.id = id;
        this.image = img;

        //TODO: Implement missing sprite image if null
    }

    public String getID() {
        return id;
    }

    public Image getImage() {
        return image;
    }
}
