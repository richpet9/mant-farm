package com.minegame.tests;

import com.minegame.world.*;

public class WorldTest {

    public static void main(String[] args) {
        for(int i = 0; i < 10; i ++) {
            System.out.println("================ Creating world " + i + "... ================");
            World world = new World(4000, 8000);
            world.generateWorld();
            System.out.println("================ ================ ================");

        }
    }
}
