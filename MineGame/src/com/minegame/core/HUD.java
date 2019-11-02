package com.minegame.core;

import com.minegame.gui.Button;

import java.awt.*;

/**
 * A container and handler for all the GUI elements while in-game
 */
public class HUD {
    private Handler handler;
    private Game game;
    private Button[] clickModeButtons = new Button[8];

    public HUD(Game game, Handler handler) {
        this.game = game;
        this.handler = handler;

        this.clickModeButtons[0] = new Button("MANT", 10, 165, Color.WHITE) {
            @Override
            public void click() {
                handler.setClickMode("SPAWN");
                resetActiveButton();
                this.setActive(true);
            }
        };

        this.clickModeButtons[1] = new Button("MINE", 10, 185, Color.WHITE) {
            @Override
            public void click() {
                handler.setClickMode("MINE");
                resetActiveButton();
                this.setActive(true);
            }
        };

        this.clickModeButtons[2] = new Button("DIRT", 10, 205, Color.WHITE) {
            @Override
            public void click() {
                handler.setClickMode("DIRT");
                resetActiveButton();
                this.setActive(true);
            }
        };

        this.clickModeButtons[3] = new Button("PLACE CONVEYOR", 10, 225, Color.WHITE) {
            @Override
            public void click() {
                handler.setClickMode("CONVEYOR");
                resetActiveButton();
                this.setActive(true);
            }
        };

        this.clickModeButtons[4] = new Button("CHANGE CONVEYOR DIR", 10, 245, Color.WHITE) {
            @Override
            public void click() {
                handler.setClickMode("CONVEYOR_DIR");
                resetActiveButton();
                this.setActive(true);
            }
        };

        this.clickModeButtons[5] = new Button("PLACE BOMB", 10, 265, Color.WHITE) {
            @Override
            public void click() {
                handler.setClickMode("BOMB");
                resetActiveButton();
                this.setActive(true);
            }
        };

        this.clickModeButtons[6] = new Button("ARM BOMB", 10, 285, Color.RED) {
            @Override
            public void click() {
                handler.setClickMode("ARM");
                resetActiveButton();
                this.setActive(true);
            }
        };

        this.clickModeButtons[7] = new Button("CLEAR QUEUE", 10, 305, Color.RED) {
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

    public void resetActiveButton() {
        for(Button button : clickModeButtons) {
            button.setActive(false);
        }
    }
 }
