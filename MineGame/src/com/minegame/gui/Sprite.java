package com.minegame.gui;

import com.minegame.core.GameID;

import java.awt.*;

public class Sprite {
    private GameID id;
    private Image image;

    Sprite(GameID id, Image img) {
        this.id = id;
        this.image = img;
    }

    public GameID getID() {
        return id;
    }

    public Image getImage() {
        return image;
    }
}