package com.minegame.world;

import com.minegame.core.Game;

/**
 * Creates an explosion at the specified location
 */
public class Explosion {
    private World world;
    private int cellX;
    private int cellY;
    private int radius;

    public Explosion(World world, int cellX, int cellY, int radius) {
        this.world = world;
        this.cellX = cellX;
        this.cellY = cellY;
        this.radius = radius;

        //Let's just make this badboy explode immediately
        explode();
    }

    private void explode() {
        for(int x = 0; x < radius * 2; x++) {
            for (int y = 0; y < radius * 2; y++) {
                int newX = Game.clamp((cellX - radius) + x, 0, world.getNumX() - 1);
                int newY = Game.clamp((cellY - radius) + y, 0, world.getNumY() - 1);

                double cellDist = Math.sqrt(Math.pow(x - radius, 2) + Math.pow(y - radius, 2));

                if(cellDist > radius) continue;

                Cell cell = world.getCell(newX, newY);
                if (!cell.isAir()) {
                    cell.setDropChunk(cell.getElement());
                    cell.setElement(Element.AIR);
                }

            }
        }

    }
}
