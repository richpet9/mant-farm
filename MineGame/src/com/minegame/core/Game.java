package com.minegame.core;

import com.minegame.controls.KeyInput;
import com.minegame.controls.MouseClick;
import com.minegame.controls.MouseMove;
import com.minegame.controls.MouseWheel;
import com.minegame.gui.ImageLoader;
import com.minegame.gui.Menu;
import com.minegame.world.World;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.text.DecimalFormat;

/**
 * Main class responsible for initializing all subsystems
 */
public class Game extends Canvas implements Runnable {
    public static final int VIEWPORT_WIDTH = 1202;
    public static final int VIEWPORT_HEIGHT = 752;
    public static GameState GAMESTATE = GameState.MENU;

    private Thread thread;
    private boolean running = false;
    private Camera camera;
    private MouseMove mouseMoveListener;
    private MouseClick mouseClickListener;
    private MouseWheel mouseWheelListener;
    private KeyInput keyInput;
    private Handler handler;
    private Menu menu;
    private HUD hud;

    //DEBUG: variables
    private int avgFPS = 0;
    private int memoryCooldown = 60;
    private double usedMemory;

    private Game() {
        World world = new World(VIEWPORT_WIDTH * 4, VIEWPORT_HEIGHT * 4);
        this.camera = new Camera(0, world.getHeight() - VIEWPORT_HEIGHT, 0, VIEWPORT_WIDTH * 4);
        this.handler = new Handler(world);
        this.menu = new Menu(this, handler);
        this.hud = new HUD(this, handler);

        this.handler.setCamera(camera);

        this.mouseClickListener = new MouseClick(handler, menu, hud);
        this.mouseMoveListener = new MouseMove(handler);
        this.mouseWheelListener = new MouseWheel(handler);
        this.keyInput = new KeyInput(handler);

        this.addMouseListener(mouseClickListener);
        this.addMouseMotionListener(mouseMoveListener);
        this.addMouseWheelListener(mouseWheelListener);
        this.addKeyListener(keyInput);

        new Window(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, this);

        loadImages();
        start();
    }

    private void start() {
        if(running) return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        if(!running) return;

        try {
            running = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void run() {
        this.requestFocus();
        long lastTime = System.nanoTime();  //The default last time is right now!
        double ns = 1000000000 / 60.0;      //This is a factor in nanoseconds
        double delta = 0;                   //Delta is the time between while-loop ticks

        //Frame counting variables
        long timer = System.currentTimeMillis();
        int frames = 0;

        while(running) {
            //Get current time, find the difference, divide by our factor, and add it to delta
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            //If enough time has passed to complete 60 frames per second, tick
            if(delta > 1) {
                tick();
                render();

                frames += 1;    //Increase frame count by one
                delta -= 1;     //Decrease delta by one
            }

            //After one second, every second
            if(System.currentTimeMillis() - timer >= 1000) {
                timer += 1000;
                //Get the average fps from last time and now and average it
                avgFPS = (avgFPS + frames) / 2;
                frames = 0;
            }
        }
    }

    private void tick() {
        handler.tick();

        memoryCooldown--;
        if(memoryCooldown <= 0) {
            double newUsedMemory = (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1E6;
            double deltaUsed = newUsedMemory - usedMemory;
            usedMemory = newUsedMemory;
            System.out.println("MB Used: " + new DecimalFormat("#.##").format(usedMemory) + "\t\t Delta Free MB: " + deltaUsed);
            memoryCooldown = 60;
        }
    }

    private void render() {
        //Create a BufferStrategy if none exists
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        //Get the drawable graphics which gets passed to every game element (nearly)
        Graphics2D g = (Graphics2D) bs.getDrawGraphics();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0,VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        //If we are in menu, render that
        if(GAMESTATE == GameState.MENU) menu.render(g);

        //Always render handler
        handler.render(g);

        //Render some debug info
        g.setColor(Color.WHITE);
        g.setFont(new Font("Roboto", 1, 12));
        g.drawString("FPS: " + avgFPS, 10, 25);
        g.drawString("Pixel: [" + mouseMoveListener.getX() + ", " + mouseMoveListener.getY() + "]", 10, 45);
        g.drawString("Cell: [" + mouseMoveListener.getCellX()  + ", " + mouseMoveListener.getCellY()   + "]", 10, 65);
        g.drawString("Camera X: " + camera.getX(), 10, 85);
        g.drawString("Camera Y: " + camera.getY(), 10, 105);
        g.drawString("GameObjects: " + handler.getObjects().size(), 10, 125);
        g.drawString("Jobs: " + (handler.getJobQueue().size() + handler.getInProgressJobs().size()), 10, 145);

        hud.render(g);

        //Clear current screen
        g.dispose();
        //Show what's in the buffer (everything which was just drawn above)
        bs.show();
    }

    public void changeGamestate(GameState state) {
        Game.GAMESTATE = state;
    }

    public static int clamp(int value, int low, int high) {
        if(value < low) {
            value = low;
        } else value = Math.min(value, high);
        return value;
    }

    private void loadImages() {
        ImageLoader.loadImage(GameID.MANT, "/resources/icons/mant/mant.png");
    }


    //Start her up
    public static void main(String[] args) {
        Game game = new Game();
    }
}
