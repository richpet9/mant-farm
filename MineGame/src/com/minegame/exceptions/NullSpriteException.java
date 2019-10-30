package com.minegame.exceptions;

/**
 * When attempting to access a Sprite which cannot be found.
 */
public class NullSpriteException extends Exception {
    public NullSpriteException(String msg) {
        super(msg);
    }
}
