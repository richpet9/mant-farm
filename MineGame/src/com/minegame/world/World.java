package com.minegame.world;

import com.minegame.core.Game;

import java.util.ArrayList;
import java.util.Random;

/**
 * World represents the data within the generated world as a two dimensional array of Cells
 */
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

    public World(int numX, int numY) {
        this.numX = numX;
        this.numY = numY;
        this.width = this.numX * Cell.CELL_WIDTH;
        this.height = this.numY * Cell.CELL_HEIGHT;


        this.cells = new Cell[numX][numY];
    }

    private void createLand() {
        System.out.print("Creating land...");
        //For every cell in the x direction
        for(int x = 0; x < numX; x++) {
            //Generate a random height for this column
            int randHeight = rand.nextInt(GROUND_LEVEL_NOISE);
            //For every cell in the y direction
            for(int y = 0; y < numY; y++) {
                //Create a cell
                cells[x][y] = new Cell(x * Cell.CELL_WIDTH, y * Cell.CELL_HEIGHT, x, y);

                //Get a random int with a max of the defined level of noise, and set rock to that point
                if(y >= GROUND_LEVEL + randHeight) cells[x][y].setElement(Element.ROCK);
            }
        }
        System.out.print("Done!\n");
    }

    private void createMountains() {
        System.out.print("Building peaks...");
        int maxMtnLevel = GROUND_LEVEL - MAX_MOUNTAIN_HEIGHT;
        int numPeaks = rand.nextInt(5) + 2; //Max three peaks, min 2 peaks

        //For every peak
        for(int i = 0; i < numPeaks; i++) {
            //Get a random X and Y location for the peak
            int peakX = rand.nextInt(numX);
            int peakY = rand.nextInt(GROUND_LEVEL - maxMtnLevel) + maxMtnLevel;

            //Get the cell at this peaks location
            Cell currCell = cells[peakX][peakY];
            int prevWidth = 2;  //This will be the width of the previous generated layer of this mountain
            int y = peakY;      //This is the y location, or center pillar of the mountain

            //While the current cell is above the defined ground level + it's noise level (deep enough to avoid bubbles)
            while(y < GROUND_LEVEL + GROUND_LEVEL_NOISE) {
                //Make currCell ROCK
                currCell.setElement(Element.ROCK);

                //The width of this layer is a random number proportional the mountain smoothing constant
                // a higher constant will result in wider, smoother, mountains. We add previous width so
                // we don't generate overhangs. This could be a cool place to add some overhang generation code.
                int width = rand.nextInt((int)( MOUNTAIN_SMOOTHING) + 1) + prevWidth;

                //Set the cells next to this one to be rock, as a proportion of the distance from the peak
                for(int xOffset = 0; xOffset < width; xOffset++) {
                    if(peakX - xOffset >= 0) cells[peakX - xOffset][y].setElement(Element.ROCK);
                    if(peakX + xOffset < numX) cells[peakX + xOffset][y].setElement(Element.ROCK);
                }

                //Set previous width for future calculations
                prevWidth = width;

                //Get the cell below
                currCell = cells[peakX][++y];
            }
        }
        System.out.print("Done!\n");
    }

    private void smoothTerrain() {
        System.out.print("Smoothing Terrain...");
        for(int x = 0; x < numX; x++) {
            for(int y = 0; y < numY - 1; y++) {
                //Kinda like a base case. We don't need to smooth anything past level 100
                //Fun fact: as of 10/27 this saves 5ms of world generation time
                if(y > 100) break;
                //If the element is not air, meaning the first terrain block in the column
                if(!cells[x][y].isAir()) {
                    //This checks if the cells next to it are air, meaning this cell is sticking out
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

        //Go through every cell, generate a number, and build veins
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

    private void buildOreVein(Element ore, Cell root) {
        int size = 0;           //The target size of the vein
        Cell currCell = root;   //The root of the vein

        root.setElement(ore);   //Set the root to the ore

        //Set the target size based off ore
        switch (ore) {
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

        //TODO: Remove this safety, make it work
        // so sometimes this loop goes on forever, and I haven't been able to pinpoint why yet
        // for now this works. It has something to do with constantly going in circles.
        int safety = 0;
        while (size > 0 && safety < 100) {
            int x = currCell.getCellX();
            int y = currCell.getCellY();

            //Generate a random direction
            switch (rand.nextInt(4)) {
                case 0:
                    //Spread north - clamp x and y to world size
                    y = Game.clamp(y - 1, 0, numY - 1);
                    if (cells[x][y].getElement() != ore) {
                        cells[x][y].setElement(ore);
                        currCell = cells[x][y];
                        size -= 1;
                        continue;
                    }
                    safety += 1;
                case 1:
                    //Spread east
                    x = Game.clamp(x + 1, 0, numX - 1);
                    if (cells[x][y].getElement() != ore) {
                        cells[x][y].setElement(ore);
                        currCell = cells[x][y];
                        size -= 1;
                        continue;
                    }
                    safety += 1;
                case 2:
                    //Spread south
                    y = Game.clamp(y + 1, 0, numY - 1);
                    if (cells[x][y].getElement() != ore) {
                        cells[x][y].setElement(ore);
                        currCell = cells[x][y];
                        size -= 1;
                        continue;
                    }
                    safety += 1;
                case 3:
                    //Spread west
                    x = Game.clamp(x - 1, 0, numX - 1);
                    if (cells[x][y].getElement() != ore) {
                        cells[x][y].setElement(ore);
                        currCell = cells[x][y];
                        size -= 1;
                    }
                    safety += 1;
            }
        }
    }

    private void createDirt() {
        System.out.print("Laying down dirt...");
        //Iterate through every column at the top (where only AIR can be)
        for(int x = 0; x < numX; x++) {
            //Start at the top, loop downward until we hit not air
            int y = 0;
            Cell currCell = cells[x][y];

            //While the current cell is air (the y < numY is a loop safety cutoff)
            while(currCell.isAir() && y < numY) {
                //Get the cell below
                Cell cellBelow = cells[x][y + 1];

                //If this cell is a rock,
                if(cellBelow.getElement() == Element.ROCK) {
                    //Make the next ROCK_LEVEL above levels dirt
                    for(int i = 0; i < ROCK_LEVEL + rand.nextInt(ROCK_LEVEL); i++) {
                        cells[x][y - i].setElement(Element.DIRT);
                    }
                }

                currCell = cellBelow;
                y++;
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
        createOreVeins();
        smoothTerrain();
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

    public ArrayList<Cell> getNeighbors(Cell cell) {
        //ArrayList to return
        ArrayList<Cell> res = new ArrayList<Cell>();
        //The location of queried cell
        int cellX = cell.getCellX();
        int cellY = cell.getCellY();

        //Loop through the neighbors by getting +/- 1 in both directions
        for(int x = cellX - 1; x < cellX + 1; x++ ) {
            for(int y = cellY - 1; y < cellY + 1; y++) {
                //Clamp our values
                int newX = Game.clamp(x, 0, numX - 1);
                int newY = Game.clamp(y, 0, numY - 1);

                //Make sure we aren't on the center cell
                if(newX != cellX && newY != cellY) {
                    Cell cellToAdd = cells[newX][newY];
                    res.add(cellToAdd);
                }
            }
        }

        return res;
    }

    public ArrayList<Cell> getNeighbors(int cellX, int cellY) {
        //ArrayList to return
        ArrayList<Cell> res = new ArrayList<Cell>();

        //Loop through the neighbors by getting +/- 1 in both directions
        for(int x = cellX - 1; x <= cellX + 1; x++ ) {
            for(int y = cellY - 1; y <= cellY + 1; y++) {
                //Clamp our values
                int newX = Game.clamp(x, 0, numX - 1);
                int newY = Game.clamp(y, 0, numY - 1);

                //Make sure we aren't on the center cell
                if(newX != cellX || newY != cellY) {
                    Cell cellToAdd = cells[newX][newY];
                    res.add(cellToAdd);
                }
            }
        }

        return res;
    }
}
