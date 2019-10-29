package com.minegame.gui;

import com.minegame.core.Game;
import com.minegame.core.GameID;
import com.minegame.exceptions.NullSpriteException;

import javax.swing.*;
import java.awt.*;

/**
 * A class for loading images into the Game object
 */
public class ImageLoader {
    //Create an array of sprites with the length of our number of GameObjects
    private static final Sprite MISSING_SPRITE = new Sprite(null, null);
    private static Sprite[] sprites = new Sprite[GameID.values().length];
    private static int index = 0;

    /**
     * Loads an image into the game
     * @param id The ID of the object this image will represent
     * @param src The local file path of the image within the project
     * @throws NullPointerException If the image cannot be located
     */
    public static void loadImage(GameID id, String src) {
        Image img;
        try {
            img = new ImageIcon(Game.class.getResource(src)).getImage();
            addSprite(id, img);
        } catch (NullPointerException e) {
            System.out.println("Unable to locate requested resource: \n" + e.getMessage());
        }
    }

    public static Sprite getSprite(GameID id) throws NullSpriteException {
        for(int i = 0; i < index; i++) {
            if(sprites[i].getID() == id) {
                return sprites[i];
            }
        }

        return MISSING_SPRITE;
    }

    private static void addSprite(GameID id, Image img) {
        try {
            sprites[index++] = new Sprite(id, img);
        } catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
            System.out.println("ERROR: IndexOutOfBoundsException: Attempted to add sprites which exceeded number of GameObjects");
        }
    }
}
