package com.P2.byog.lab5;

import com.P2.byog.TileEngine.TERenderer;
import com.P2.byog.TileEngine.TETile;
import com.P2.byog.TileEngine.Tileset;

import java.util.Random;

public class Map {
    private static final int WIDTH = 50; // x
    private static final int HEIGHT = 50; // y
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH,HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];

        initialize(world,50,45);

        ter.renderFrame(world);
    }

    //随机生成
    public static void initialize(TETile[][] map,int width, int height) {
        for(int i = 0; i < WIDTH; i++) {
            for(int j = 0; j < HEIGHT; j++) {
                map[i][j] = Tileset.NOTHING;
            }
        }

        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                Random random = new Random();
                int x = random.nextInt(100);
                // 墙的概率 30 %
                if(x < 35 || i == 0 || i == width - 1|| j == 0 || j == height - 1) {
                    map[i][j] = Tileset.WALL;
                }
                else {
                    map[i][j] = Tileset.NOTHING;
                }
            }
        }
    }

    //在范围内
    public static boolean isOk(int x, int y) {
        return x >= 0 && x < WIDTH && y < HEIGHT && y >= 0;
    }


}
