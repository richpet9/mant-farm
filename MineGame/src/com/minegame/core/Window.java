package com.minegame.core;

import javax.swing.*;
import java.awt.*;

/**
 * A canvas-extended class for creating a JFrame window
 */
class Window extends Canvas {

    private int WIDTH;
    private int HEIGHT;
    private JFrame frame;

    /**
     * Constructor will create a new JFrame window
     * @param w Width of the window
     * @param h Height of the window
     * @param game Component, a Game, to add to the window
     */
    public Window(int w, int h, Game game) {
        WIDTH = w; HEIGHT = h;

        frame = new JFrame();
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(game);

        frame.setVisible(true);
    }
}
