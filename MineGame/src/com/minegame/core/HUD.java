package com.minegame.core;

import com.minegame.gui.Button;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A container and handler for all the GUI elements while in-game
 */
public class HUD {
    private MouseHandler mHandler;
    private Game game;
    private Button[] clickModeButtons = new Button[7];
    private Button activeButton;

    public HUD(Game game, MouseHandler mHandler) {
        this.game = game;
        this.mHandler = mHandler;

        this.clickModeButtons[0] = new Button("MANT", 10, 165, Color.WHITE) {
            @Override
            public void click() {
                mHandler.setClickMode("SPAWN");
            }
        };

        this.clickModeButtons[1] = new Button("MINE", 10, 185, Color.WHITE) {
            @Override
            public void click() {
                mHandler.setClickMode("MINE");
            }
        };

        this.clickModeButtons[2] = new Button("DIRT", 10, 205, Color.WHITE) {
            @Override
            public void click() {
                mHandler.setClickMode("DIRT");
            }
        };

        this.clickModeButtons[3] = new Button("PLACE CONVEYOR", 10, 225, Color.WHITE) {
            @Override
            public void click() {
                mHandler.setClickMode("CONVEYOR");
            }
        };

        this.clickModeButtons[4] = new Button("CHANGE CONVEYOR DIR", 10, 245, Color.WHITE) {
            @Override
            public void click() {
                mHandler.setClickMode("CONVEYOR_DIR");
            }
        };

        this.clickModeButtons[5] = new Button("PLACE BOMB", 10, 265, Color.WHITE) {
            @Override
            public void click() {
                mHandler.setClickMode("BOMB");
            }
        };

        this.clickModeButtons[6] = new Button("ARM BOMB", 10, 285, Color.RED) {
            @Override
            public void click() {
                mHandler.setClickMode("ARM");
            }
        };
    }

    public void tick() {
        switch(mHandler.getClickMode()) {
            case "SPAWN":
                activeButton = clickModeButtons[0];
                break;
            case "MINE":
                activeButton = clickModeButtons[1];
                break;
            case "DIRT":
                activeButton = clickModeButtons[2];
                break;
            case "CONVEYOR":
                activeButton = clickModeButtons[3];
                break;
            case "CONVEYOR_DIR":
                activeButton = clickModeButtons[4];
                break;
            case "BOMB":
                activeButton = clickModeButtons[5];
                break;
            case "ARM":
                activeButton = clickModeButtons[6];
                break;
            default:
                activeButton = null;
        }
    }

    public void render(Graphics2D g) {
        for(int i = 0; i < clickModeButtons.length; i++) {
            clickModeButtons[i].setActive(false);

            if(activeButton != null) {
                activeButton.setActive(true);
            }
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
