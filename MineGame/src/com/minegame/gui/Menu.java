package com.minegame.gui;

import com.minegame.core.Game;
import com.minegame.core.GameState;
import com.minegame.core.Handler;

import java.awt.*;
import java.util.ArrayList;

/**
 * A class to create the main menu of the game on startup
 */
public class Menu extends Canvas {
    private Game game;
    private Handler handler;
    private Font titleFont = new Font("Futura", Font.BOLD, 98);
    private Font bodyFont = new Font("Futura", Font.PLAIN, 40);
    private String title = "MINE+GAME";
    private ArrayList<Button> buttons = new ArrayList<Button>(3);

    public Menu(Game game, Handler handler) {
        this.game = game;
        this.handler = handler;

        buttons.add(new Button("Generate World", 200, 300, Color.WHITE) {
            @Override
            public void click() {
                handler.generateWorld();
                game.changeGamestate(GameState.PLAYING);
            }
        });
        buttons.add(new Button("Exit", 200, 400, Color.WHITE) {
            @Override
            public void click() {
                System.exit(0);
            }
        });
    }

    public ArrayList<Button> getButtons() { return buttons; }

    public void render(Graphics2D g) {
        g.setColor(new Color(0x5BCBEF));;
        g.fillRect(0,0, Game.VIEWPORT_WIDTH, Game.VIEWPORT_HEIGHT);

        g.setFont(titleFont);
        int width = UserInterface.getTextWidth(g, title, titleFont);

        g.setColor(Color.BLACK);
        g.drawString(title, (Game.VIEWPORT_WIDTH / 2) - (width / 2) + 4, 204);
        g.setColor(Color.WHITE);
        g.drawString(title, (Game.VIEWPORT_WIDTH / 2) - (width / 2), 200);

        //Now render out buttons
        g.setFont(bodyFont);

        for(Button button : buttons) {
            int w = UserInterface.getTextWidth(g, button.getValue(), bodyFont);

            button.setX(Game.VIEWPORT_WIDTH / 2 - (w / 2));
            button.render(g);
        }
    }
}
