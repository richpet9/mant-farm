package com.minegame.core;

import com.minegame.gui.Button;

import java.awt.*;

/**
 * A container and handler for all the GUI elements while in-game
 */
public class HUD {
    private Handler handler;
    private Game game;
    private Button[] clickModeButtons = new Button[3];

    public HUD(Game game, Handler handler) {
        this.game = game;
        this.handler = handler;

        this.clickModeButtons[0] = new Button("MANT", 100, 60, Color.WHITE) {
            @Override
            public void click() {
                handler.setClickMode("SPAWN");
            }
        };

        this.clickModeButtons[1] = new Button("MINE", 150, 60, Color.WHITE) {
            @Override
            public void click() {
                handler.setClickMode("MINE");
            }
        };

        this.clickModeButtons[2] = new Button("CLEAR QUEUE", 200, 60, Color.RED) {
            @Override
            public void click() {
                handler.getMineQueue().clear(false);
            }
        };
    }

    public void tick() {

    }

    public void render(Graphics2D g) {
        for(int i = 0; i < clickModeButtons.length; i++) {
            clickModeButtons[i].render(g);
        }
    }

    public Button[] getButtons() {
        return this.clickModeButtons;
    }
 }
