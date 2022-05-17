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
    private static int[][] npc;


    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        Map.initialize(world,WIDTH,HEIGHT);
        Map.connect(world,WIDTH,HEIGHT);
        getPosition(Map.SelectPoint(world));

        int[] t = Map.SelectNpc(world);
        int npc_x1 = t[0];
        int npc_y1 = t[1];
        t = Map.SelectNpc(world);
        int npc_x2 = t[0];
        int npc_y2 = t[1];
        npc = new int[][]{{npc_x1,npc_y1},{npc_x2,npc_y2}};
        // 测试npc移动
        ter.renderFrame(world);

        move(world,ter);
        if (gameOver()) {
            drawFrame("Game over!!!");
        }
        else
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

    public static boolean gameOver() {
        for (int[] n : npc) {
            if (n[0] == player_x && n[1] == player_y) {
                return true;
            }
        }
        return false;
    }

    public static void move(TETile[][] w, TERenderer ter) {
        while (!isWin()) {
            npcMove(w,ter);
            if (gameOver()) {
                return;
            }
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

            }
            else if(Map.isOk(x,y) && w[x][y] == Tileset.MOUNTAIN) {
                w[x][y] = Tileset.PLAYER;
                w[player_x][player_y] = Tileset.NOTHING;
                player_x = x;
                player_y = y;
                return;
            }
        }
    }

    public static void npcMove(TETile[][] w, TERenderer ter) {
        int[][] d = {{1,0},{0,1},{-1,0},{0,-1}};
        Random r = new Random();
        for (int[] n : npc) {
            int t = r.nextInt(4);
            int x = n[0] + d[t][0];
            int y = n[1] + d[t][1];
            if (Map.isOk(x, y) && w[x][y] == Tileset.NOTHING || w[x][y] == Tileset.PLAYER) {
                swap(x, y, n[0], n[1], w);
                if (w[n[0]][n[1]] == Tileset.PLAYER) {
                    w[n[0]][n[1]] = Tileset.NOTHING;
                }
                n[0] = x;
                n[1] = y;
                ter.renderFrame(w);
                if (gameOver())
                    return;
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
