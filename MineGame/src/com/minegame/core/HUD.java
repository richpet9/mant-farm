package com.minegame.core;

import com.minegame.gui.Button;

import java.awt.*;

/**
 * A container and handler for all the GUI elements while in-game
 */
public class HUD {
    private MouseHandler mHandler;
    private Game game;
    private Button[] clickModeButtons = new Button[7];

    public HUD(Game game, MouseHandler mHandler) {
        this.game = game;
        this.mHandler = mHandler;

        this.clickModeButtons[0] = new Button("MANT", 10, 165, Color.WHITE) {
            @Override
            public void click() {
                mHandler.setClickMode("SPAWN");
                resetActiveButton();
                this.setActive(true);
            }
        };

        this.clickModeButtons[1] = new Button("MINE", 10, 185, Color.WHITE) {
            @Override
            public void click() {
                mHandler.setClickMode("MINE");
                resetActiveButton();
                this.setActive(true);
            }
        };

        this.clickModeButtons[2] = new Button("DIRT", 10, 205, Color.WHITE) {
            @Override
            public void click() {
                mHandler.setClickMode("DIRT");
                resetActiveButton();
                this.setActive(true);
            }
        };

        this.clickModeButtons[3] = new Button("PLACE CONVEYOR", 10, 225, Color.WHITE) {
            @Override
            public void click() {
                mHandler.setClickMode("CONVEYOR");
                resetActiveButton();
                this.setActive(true);
            }
        };

        this.clickModeButtons[4] = new Button("CHANGE CONVEYOR DIR", 10, 245, Color.WHITE) {
            @Override
            public void click() {
                mHandler.setClickMode("CONVEYOR_DIR");
                resetActiveButton();
                this.setActive(true);
            }
        };

        this.clickModeButtons[5] = new Button("PLACE BOMB", 10, 265, Color.WHITE) {
            @Override
            public void click() {
                mHandler.setClickMode("BOMB");
                resetActiveButton();
                this.setActive(true);
            }
        };

        this.clickModeButtons[6] = new Button("ARM BOMB", 10, 285, Color.RED) {
            @Override
            public void click() {
                mHandler.setClickMode("ARM");
                resetActiveButton();
                this.setActive(true);
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
