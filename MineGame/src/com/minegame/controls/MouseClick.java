package com.minegame.controls;

import com.minegame.core.Game;
import com.minegame.core.HUD;
import com.minegame.core.Handler;
import com.minegame.core.MouseHandler;
import com.minegame.gui.Button;
import com.minegame.gui.Menu;
import com.minegame.world.Cell;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * MouseListener variant
 */
public class MouseClick extends Mouse implements MouseListener {
    private Menu menu;
    private HUD hud;
    private int pressedCellX;
    private int pressedCellY;

    public MouseClick(MouseHandler mHandler, Menu menu, HUD hud) {
        super(mHandler);

        this.menu = menu;
        this.hud = hud;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        //Store the clicked cell location to compare it to released location
        mHandler.setStartedDragX(e.getX() / Cell.CELL_WIDTH);
        mHandler.setStartedDragY(e.getY() / Cell.CELL_WIDTH);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        mouseCellX = e.getX() / Cell.CELL_WIDTH;
        mouseCellY = e.getY() / Cell.CELL_HEIGHT;

        switch(Game.GAMESTATE) {
            case MENU:
                for(Button button : menu.getButtons()) {
                    if(mouseOverXY(button.getX(), button.getY() - button.getH(), button.getW(), button.getH())) {
                        button.click();
                    }
                }
                break;
            case PLAYING:
                boolean onHUD = false;
                for(Button button : hud.getButtons()) {
                    if(mouseOverXY(button.getX(), button.getY() - button.getH(), button.getW(), button.getH())) {
                        button.click();
                        onHUD = true;
                    }
                }
                if(!onHUD) {
                    mHandler.handleClick(mouseX, mouseY, mouseCellX, mouseCellY);
                }
                break;
            case GAMEOVER:
                break;
            case PAUSE:
        }

        mHandler.setDragging(false);
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) {  }
}
