package com.minegame.world;

import com.minegame.core.Camera;
import com.minegame.core.GameObject;
import com.minegame.core.Handler;

import java.awt.*;
import java.util.Random;

public class World {
    private static int GROUND_LEVEL = 60;           //How many cells from top is the ground level?
    private static int GROUND_LEVEL_NOISE = 3;      //3How many cells of roughness is the ground level?
    private static int ROCK_LEVEL = 3;              //How many cells from a dirt  block is the rock level?
    private static int MAX_MOUNTAIN_HEIGHT = 50;    //How many cells from GOUND_LEVEL is the maximum mountain height?
    private static int MOUNTAIN_SMOOTHING = 3;      //Mountain smoothing factor (10 = very smooth, 1 = very steep)
    private int width;
    private int height;
    private Cell[][] cells;
    private int numX;
    private int numY;
    private Random rand = new Random();

    public World(int w, int h) {
        this.width = w;
        this.height = h;

        this.numX = width / Cell.CELL_WIDTH;
        this.numY = height / Cell.CELL_HEIGHT;

        this.cells = new Cell[numX][numY];
    }

    public void tick() {

    }

    public void render(Graphics2D g) {
    }

    private void createLand() {
        System.out.print("Creating land...");
        for(int x = 0; x < numX; x++) {
            int test = rand.nextInt(GROUND_LEVEL_NOISE);
            for(int y = 0; y < numY; y++) {
                cells[x][y] = new Cell(x * Cell.CELL_WIDTH, y * Cell.CELL_HEIGHT, x, y);

                //If the cell is below ground level +/- 2, make it rock by default
                if(y >= GROUND_LEVEL + test) cells[x][y].setElement(Element.ROCK);
            }
        }
        System.out.print("Done!\n");
    }

    private void createMountains() {
        System.out.print("Building peaks...");
        int maxMtnLevel = GROUND_LEVEL - MAX_MOUNTAIN_HEIGHT;
        int numPeaks = rand.nextInt(5) + 2; //Max three peaks, min 2 peaks

        for(int i = 0; i < numPeaks; i++) {
            int peakX = rand.nextInt(numX);
            int peakY = rand.nextInt(GROUND_LEVEL - maxMtnLevel) + maxMtnLevel;

            Cell currCell = cells[peakX][peakY];
            int prevWidth = 2;  //This is the width of the previous generated layer of this mountain
            int y = peakY;

            //While the current cell is air (the y < numY is a loop safety cutoff)
            while(y < GROUND_LEVEL + GROUND_LEVEL_NOISE) {
                //Make currCell ROCK
                currCell.setElement(Element.ROCK);

                //If our ideal width is less than 5, just use that (this is to make them not look lame)
                int realWidth = rand.nextInt((int)( MOUNTAIN_SMOOTHING) + 1) + prevWidth;

                //Set the cells next to this one to be rock, as a proportion of the distance from the peak
                for(int xOffset = 0; xOffset < realWidth; xOffset++) {
                    if(peakX - xOffset >= 0) cells[peakX - xOffset][y].setElement(Element.ROCK);
                    if(peakX + xOffset < numX) cells[peakX + xOffset][y].setElement(Element.ROCK);
                }

                //Set previous width for future calculations
                prevWidth = realWidth;

                //Get the cell below
                y += 1;
                currCell = cells[peakX][y];
            }
        }
        System.out.print("Done!\n");
    }

    private void smoothTerrain() {
        System.out.print("Smoothing Terrain...");
        for(int x = 0; x < numX; x++) {
            for(int y = 0; y < numY - 1; y++) {
                //Kinda like a base case. We don't need to smooth anything past level 100
                //Fun fact: as of 10/27 this saves 5ms of world generation
                if(y > 100) break;
                //If the element is not air, meaning the first terrain block
                if(!cells[x][y].isAir()) {
                    //Also I hate having to duplicate similar code like this
                    if (x == 0) {
                        //In the first row, don't check left
                        if (cells[x + 1][y].isAir() || cells[x + 1][y + 1].isAir()) {
                            cells[x][y].setElement(Element.AIR);
                        }
                    } else if (x == numX - 1) {
                        //In the last row don't check right
                        if (cells[x - 1][y].isAir() || cells[x - 1][y + 1].isAir()) {
                            //Make this cell air. This is maybe a little lazy
                            cells[x][y].setElement(Element.AIR);
                        }
                    } else {
                        //IF YOUR LEFT AND RIGHT ARE AIR
                        //OR IF YOUR LEFT AND BOTTOM LEFT ARE AIR
                        //OR IF YOUR RIGHT AND BOTTOM RIGHT ARE AIR
                        if ((cells[x - 1][y].isAir() && cells[x + 1][y].isAir())
                                || (cells[x - 1][y].isAir() && cells[x - 1][y + 1].isAir())
                                || (cells[x + 1][y].isAir() && cells[x + 1][y + 1].isAir())) {
                            //Make this cell air. This is maybe a little lazy
                            cells[x][y].setElement(Element.AIR);
                        }
                    }
                }
            }
        }
        System.out.print("Done!\n");
    }

    private void createOreVeins() {
        System.out.print("Creating ore veins...");
        final double IRON_RARITY = 0.001;        //.10% chance of iron vein forming for every 100 cells
        final double COPPER_RARITY = 0.00085;    //.085% chance of copper vein forming for every 100 cells
        final double SILVER_RARITY = 0.00008;    //.008% chance of silver vein forming for every 100 cells
        final double GOLD_RARITY = 0.00005;      //.005% chance of gold vein forming for every 100 cells

        for(int x = 0; x < numX; x++) {
            for(int y = 0; y < numY; y++) {
                if(cells[x][y].getElement() != Element.ROCK) continue;

                if(rand.nextDouble() < IRON_RARITY) {
                    buildOreVein(Element.IRON, cells[x][y]);
                    continue;
                }
                if(rand.nextDouble() < COPPER_RARITY) {
                    buildOreVein(Element.COPPER, cells[x][y]);
                    continue;
                }
                if(rand.nextDouble() < SILVER_RARITY) {
                    buildOreVein(Element.SILVER, cells[x][y]);
                    continue;
                }
                if(rand.nextDouble() < GOLD_RARITY) {
                    buildOreVein(Element.GOLD, cells[x][y]);
                }
            }
        }

        System.out.print("\nDone creating ore veins!\n");
    }

    private void buildOreVein(Element element, Cell root) {
        int size = 0;
        Cell currCell = root;

        root.setElement(element);

        switch (element) {
            case IRON:
                size = 6;
                break;
            case COPPER:
                size = 5;
                break;
            case SILVER:
                size = 4;
                break;
            case GOLD:
                size = 3;
                break;
        }

        sizeLoop: while(size > 0 && !currCell.isAir()) {
            int x = currCell.getCellX();
            int y = currCell.getCellY();

            switch(rand.nextInt(4)) {
                case 0:
                    //Spread north
                    if(cells[x][y - 1].getElement() != element) {
                        cells[x][y - 1].setElement(element);
                        currCell = cells[x][y - 1];
                        size -=1;
                        continue;
                    }
                case 1:
                    if(x == numX - 1) break;
                    //Spread east
                    if(cells[x + 1][y].getElement() != element) {
                        cells[x + 1][y].setElement(element);
                        currCell = cells[x + 1][y];
                        size -= 1;
                        continue;
                    }
                case 2:
                    if(y == numY - 1) break;
                    //Spread south
                    if(cells[x][y + 1].getElement() != element) {
                        cells[x][y + 1].setElement(element);
                        currCell = cells[x][y + 1];
                        size -= 1;
                        continue;
                    }
                case 3:
                    if(x == 0) break;
                    //Spread west
                    if(cells[x - 1][y].getElement() != element) {
                        cells[x - 1][y].setElement(element);
                        currCell = cells[x - 1][y];
                        size -= 1;
                        continue;
                    }
                default:
                    break sizeLoop;
            }
        }
    }

    private void createDirt() {
        System.out.print("Laying down dirt...");
        //Iterate through every column at the top (where only AIR can be)
        for(int x = 0; x < numX; x++) {
            int y = 0;
            Cell currCell = cells[x][y];

            //While the current cell is air (the y < numY is a loop safety cutoff)
            while(currCell.isAir() && y < numY) {
                //Get the cell below
                y += 1;
                Cell cellBelow = cells[x][y];

                //If this cell is a rock,
                if(cellBelow.getElement() == Element.ROCK) {
                    //Make the next ROCK_LEVEL above levels dirt
                    for(int i = 0; i < ROCK_LEVEL + rand.nextInt(ROCK_LEVEL); i++) {
                        cells[x][y - i].setElement(Element.DIRT);
                    }
                }

                currCell = cellBelow;
            }
        }
        System.out.print("Done!\n");
    }

    public void generateWorld() {
        long start = System.nanoTime();
        createLand();
        createMountains();
        smoothTerrain();
        createDirt();
        smoothTerrain();
        createOreVeins();
        long duration = System.nanoTime() - start;
        double ms = duration * 1E-6;
        System.out.println("World generation took: " + ms + "ms");
    }

    public int getNumX() {
        return numX;
    }
    public int getNumY() {
        return numY;
    }
    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }
    public Cell[][] getCells() {
        return this.cells;
    }

    /**
     * A method to get the cell object at a specific coordinate (on the Cell-Coordinate grid)
     * @param x The x coordinate
     * @param y The y coordinate
     * @return Cell at specified location
     * @throws IndexOutOfBoundsException when no such cells exists at specified location
     */
    public Cell getCell(int x, int y) throws IndexOutOfBoundsException {
        if(x > numX || x < 0) throw new IndexOutOfBoundsException("Index (x): " + x + " is out of bounds of " + numX);
        if(y > numY || y < 0 ) throw new IndexOutOfBoundsException("Index (y): " + y + " is out of bounds " + numY);
        return cells[x][y];
    }

    /**
     * Sets the specified location to a cell
     * @param x The X (cell) location of the cell
     * @param y The Y (cell) location of the cell
     * @param cell The Cell to insert
     */
    public void setCell(int x, int y, Cell cell) throws IndexOutOfBoundsException {
        if(x > numX || x < 0) throw new IndexOutOfBoundsException("Index (x): " + x + " is out of bounds of " + numX);
        if(y > numY || y < 0 ) throw new IndexOutOfBoundsException("Index (y): " + y + " is out of bounds " + numY);
        cells[x][y] = cell;
        cell.setCellXY(x, y);
    }

    /**
     * Swaps two cells in the cell container
     * @param a Cell one to swap
     * @param b Cell two to swap
     */
    public void swapCells(Cell a, Cell b) {
        int aX = a.getCellX();
        int aY = a.getCellY();

        cells[aX][aY] = b;
        cells[b.getCellX()][b.getCellY()] = a;

        a.setCellXY(b.getCellX(), b.getCellY());
        b.setCellXY(aX, aY);
    }
}
