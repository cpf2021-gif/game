package com.P2.byog.lab5;

import com.P2.byog.TileEngine.TERenderer;
import com.P2.byog.TileEngine.TETile;
import com.P2.byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.util.Random;

public class Move {
    private static final int WIDTH = 50; // x
    private static final int HEIGHT = 50; // y
    private static  int player_x;
    private static  int player_y;
    private static  int target_x;
    private static  int target_y;
    private static int npc_x;
    private static int npc_y;

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        Map.initialize(world,WIDTH,HEIGHT);
        Map.connect(world,WIDTH,HEIGHT);
        getPosition(Map.SelectPoint(world));
        int[] t = Map.SelectNpc(world);
        npc_x = t[0];
        npc_y = t[1];
        // 测试npc移动

        ter.renderFrame(world);
        npcMove(world,ter);

        move(world,ter);
        drawFrame("Win!!!");
    }

    public static void getPosition(int[][] p) {
        player_x = p[0][0];
        player_y = p[0][1];
        target_x = p[1][0];
        target_y = p[1][1];
    }

    public static boolean isWin() {
        return player_x == target_x && player_y == target_y;
    }

    public static void move(TETile[][] w, TERenderer ter) {
        while (!isWin()) {
            if(!StdDraw.hasNextKeyTyped())
                continue;
            char ch = StdDraw.nextKeyTyped();
            int[] p = movePoint(ch);
            int x = p[0], y = p[1];
            if(Map.isOk(x,y) && (w[x][y] == Tileset.NOTHING || w[x][y] == Tileset.LOCKED_DOOR)) {
                swap(x, y, player_x, player_y, w);
                player_x = x;
                player_y = y;
                ter.renderFrame(w);
                StdDraw.pause(50);
            }
        }
    }

    public static void npcMove(TETile[][] w, TERenderer ter) {
        int[][] d = {{1,0},{0,1},{-1,0},{0,-1}};
        while (true) {
            Random r = new Random();
            int t = r.nextInt(4);
            int x = npc_x + d[t][0];
            int y = npc_y + d[t][1];
            if (Map.isOk(x,y) && w[x][y] == Tileset.NOTHING) {
                swap(x, y, npc_x,npc_y, w);
                npc_x = x;
                npc_y = y;
                ter.renderFrame(w);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ter.renderFrame(w);
        }
    }

    public static int[] movePoint(char ch) {
        switch (ch) {
            case 'w':
                return new int[]{player_x,player_y + 1};
            case 's':
                return new int[]{player_x,player_y - 1};
            case 'a':
                return new int[]{player_x - 1,player_y};
            case 'd':
                return new int[]{player_x + 1,player_y};
                default:
                    return new int[]{player_x,player_y};
        }
    }

    private static void swap(int x, int y, int px, int py, TETile[][] w) {
        TETile temp = w[x][y];
        w[x][y] = w[px][py];
        w[px][py] = temp;
        if(w[px][py] == Tileset.LOCKED_DOOR) {
            w[px][py] = Tileset.NOTHING;
        }
    }

    public static void  drawFrame(String s) {
        int midWidth = WIDTH / 2;
        int midHeight = HEIGHT / 2;

        StdDraw.clear();
        StdDraw.clear(Color.black);

        // Draw the actual text
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(midWidth, midHeight, s);
        StdDraw.show();
    }
}
