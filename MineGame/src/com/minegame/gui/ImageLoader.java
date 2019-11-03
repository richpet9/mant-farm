package com.minegame.gui;

import com.minegame.core.Game;
import com.minegame.exceptions.NullSpriteException;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.util.ArrayList;

/**
 * A class for loading images into the Game object
 */
public class ImageLoader {
    //Create an array of sprites with the length of our number of GameObjects
    private static final Sprite MISSING_SPRITE = new Sprite(null, null);
    private static ArrayList<Sprite> sprites = new ArrayList<Sprite>(64);

    /**
     * Loads an image into the game
     * @param id The ID of the object this image will represent
     * @param src The local file path of the image within the project
     * @throws NullPointerException If the image cannot be located
     */
    public static void loadImage(String id, String src) {
        Image img;
        try {
            img = new ImageIcon(Game.class.getResource(src)).getImage();
            addSprite(id, img);
        } catch (NullPointerException e) {
            System.out.println("Unable to locate requested resource: \n" + e.getMessage());
        }
    }

    public static Sprite getSprite(String id) throws NullSpriteException {
        for(Sprite sprite : sprites) {
            if(sprite.getID().equals(id)) {
                return sprite;
            }
        }

        return MISSING_SPRITE;
    }

    private static void addSprite(String id, Image img) {
        sprites.add(new Sprite(id, img));
    }
}
