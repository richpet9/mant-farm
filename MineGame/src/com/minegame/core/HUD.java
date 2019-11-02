package com.minegame.core;

import com.minegame.gui.Button;

import java.awt.*;

/**
 * A container and handler for all the GUI elements while in-game
 */
public class HUD {
    private Handler handler;
    private Game game;
    private Button[] clickModeButtons = new Button[6];

    public HUD(Game game, Handler handler) {
        this.game = game;
        this.handler = handler;

        this.clickModeButtons[0] = new Button("MANT", 10, 165, Color.WHITE) {
            @Override
            public void click() {
                handler.setClickMode("SPAWN");
            }
        };

        this.clickModeButtons[1] = new Button("MINE", 10, 185, Color.WHITE) {
            @Override
            public void click() {
                handler.setClickMode("MINE");
            }
        };

        this.clickModeButtons[2] = new Button("DIRT", 10, 205, Color.WHITE) {
            @Override
            public void click() {
                handler.setClickMode("DIRT");
            }
        };

        this.clickModeButtons[3] = new Button("PLACE BOMB", 10, 225, Color.WHITE) {
            @Override
            public void click() {
                handler.setClickMode("BOMB");
            }
        };

        this.clickModeButtons[4] = new Button("ARM BOMB", 10, 245, Color.RED) {
            @Override
            public void click() {
                handler.setClickMode("ARM");
            }
        };

        this.clickModeButtons[5] = new Button("CLEAR QUEUE", 10, 265, Color.RED) {
            @Override
            public void click() {
                handler.getJobQueue().clear(false);
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
