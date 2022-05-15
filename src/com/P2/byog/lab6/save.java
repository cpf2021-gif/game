package com.P2.byog.lab6;

import com.P2.byog.TileEngine.TERenderer;
import com.P2.byog.TileEngine.TETile;
import com.P2.byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;


public class save {
    public static void main(String[] args) {

        TETile[][] world = new TETile[50][50];
        boolean loop = false;
        Map2 map = new Map2(50,50);
        TERenderer ter = new TERenderer();
        ter.initialize(50, 50);
        String[] s = {"New game (N)","Load Game (L)","Quit (q)"};

        // 开始界面
        drawFrame(s);
        while(!loop) {
            if(StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                // 生成新游戏
                if (c == 'n') {
                    File f = new File("test.data");
                    if(f.exists()) {
                        f.delete();
                    }
                    loop = true;
                } else if (c == 'q') {
                    writeMap(map);
                    System.exit(0);
                } else if (c == 'l') {
                    map = readMap(50,50);
                    loop = true;
                }
            }
        }

        // 清空设置
        StdDraw.enableDoubleBuffering();
        ter.initialize(50, 50);
        changeWorld(map.getWorld(),world);
        ter.renderFrame(world);

        while (!isWin(map)) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if(c == 'q') {
                    writeMap(map);
                    System.exit(0);
                }
                else {
                    move(map,c);
                    changeWorld(map.getWorld(),world);
                    ter.renderFrame(world);
                }
            }
            changeWorld(map.getWorld(),world);
            ter.renderFrame(world);
        }
        File file = new File("test.data");
        file.delete();
        drawFrame("Win!!");
    }

    public static void writeMap(Map2 map){
        File file = new File("test.data");

        FileOutputStream out;
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(map);
            objOut.flush();
            objOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map2 readMap(int width, int height) {
        Map2 temp = null;
        File file = new File("test.data");
        FileInputStream in;
        if (file.exists()) {
            try {
                in = new FileInputStream(file);
                ObjectInputStream objIn = new ObjectInputStream(in);
                temp = (Map2) objIn.readObject();
                objIn.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return temp;
        }
        return new Map2(width,height);
    }

    public static boolean isWin(Map2 map) {
        return map.playerX == map.targetX && map.playerY == map.targetY;
    }

    public static void swap(Map2 map, int x, int y) {
        int[][] w = map.getWorld();
        int px = map.playerX;
        int py = map.playerY;
        int temp = w[x][y];
        w[x][y] = w[px][py];
        w[px][py] = temp;
        if(w[px][py] == 3) {
            w[px][py] = 0;
        }
    }

    public static void  drawFrame(String ... s) {
        int midWidth = 25;
        int midHeight = 35;

        StdDraw.clear();
        StdDraw.clear(Color.black);

        // Draw the actual text
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        for(String str : s) {
            midHeight -= 5;
            StdDraw.text(midWidth, midHeight, str);
        }
        StdDraw.show();
    }

    public static void move(Map2 map, char c) {
        int[] p = movePoint(c,map);
        int x = p[0];
        int y = p[1];
        if(map.isOk(x,y)) {
            if(map.getWorld()[x][y] == 0 || map.getWorld()[x][y] == 3) {
                swap(map,x,y);
                map.playerX = x;
                map.playerY = y;
            }
        }
    }

    public static int[] movePoint(char ch, Map2 map) {
        switch (ch) {
            case 'w':
                return new int[]{map.playerX, map.playerY + 1};
            case 's':
                return new int[]{map.playerX, map.playerY - 1};
            case 'a':
                return new int[]{map.playerX - 1, map.playerY};
            case 'd':
                return new int[]{map.playerX + 1, map.playerY};
            default:
                return new int[]{map.playerX, map.playerY};
        }
    }

    public static void changeWorld(int[][] m, TETile[][] world) {
        for(int i = 0; i < m.length; i++) {
            for(int j = 0; j < m[0].length; j++) {
                switch (m[i][j]) {
                    case 0:
                        world[i][j] = Tileset.NOTHING;
                        break;
                    case 1:
                        world[i][j] = Tileset.WALL;
                        break;
                    case 2:
                        world[i][j] = Tileset.PLAYER;
                        break;
                        default:
                            world[i][j] = Tileset.LOCKED_DOOR;
                }
            }
        }
    }
}
