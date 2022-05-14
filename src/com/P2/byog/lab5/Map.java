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

        UnionDs unionDs = new UnionDs(world, 50, 45);
        unionDs.connect(world);
        unionDs.SelectPoint(world);

        ter.renderFrame(world);
    }

    //随机生成
    public static void initialize(TETile[][] map,int width, int height) {
        // 先全部设置为地板
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

    public static boolean isOk(int x, int y, int width, int height) {
        return x>= 0 && x < width && y >= 0 && y < height;
    }

    // 通过并查集 找到连通的部分;
    static class UnionDs {
        public int[] parent;
        public int[] size;
        public int width;
        public int height;

        public UnionDs(TETile[][] w, int width, int height) {
            parent = new int[width*height];
            size = new int[width*height];
            this.width =width;
            this.height = height;
            // 初始化
            for(int i = 0; i < width; i++) {
                for(int j = 0; j < height; j++) {
                    int index = j * width + i;
                    if(w[i][j] == Tileset.WALL) {
                        parent[index] = -1;
                        size[index] = 0;
                    }
                    else {
                        parent[index] = index;
                        size[index] = 1;
                    }
                }
            }
        }

        // 通过路径压缩查找父节点
        public int find(int p) {
            if(p == parent[p]) {
                return p;
            } else {
                parent[p] = find(parent[p]);
                return parent[p];
            }
        }

        // 连接方法
        public void connected(int p, int q) {
            int i = find(p);
            int j = find(q);
            if (i == j) return;
            if (size[i] < size[j]) {
                parent[i] = j;
                size[j] += size[i];
            } else {
                parent[j] = i;
                size[i] += size[j];
            }
        }

        public int toIndex(int x, int y) {
            return y * width + x;
        }

        // 开始连接
        public void connect(TETile[][] w) {
            for(int i = 0; i < width; i++) {
                for(int j = 0; j < height; j++) {
                    if(w[i][j] == Tileset.NOTHING) {
                        // 1. 向上
                        if (isOk(i,j+1,width,height)) {
                            if (w[i][j+1] == Tileset.NOTHING) {
                                int index1 = toIndex(i,j);
                                int index2 = toIndex(i,j+1);
                                connected(index1, index2);
                            }
                        }
                        // 2. 向右
                        if (isOk(i+1,j,width,height)) {
                            if (w[i+1][j] == Tileset.NOTHING) {
                                int index1 = toIndex(i,j);
                                int index2 = toIndex(i+1,j);
                                connected(index1,index2);
                            }
                        }
                    }
                }
            }
        }

        public boolean isConnected(int i,int j) {
            return find(i) == find(j);
        }

        // 随机选取起点和终点
        public int[][] SelectPoint(TETile[][] w) {
            while(true) {
                Random random = new Random();
                int x1 = random.nextInt(width);
                int x2 = random.nextInt(width);
                int y1 = random.nextInt(height);
                int y2 = random.nextInt(height);
                int index1 = toIndex(x1,y1);
                int index2 = toIndex(x2,y2);
                if (w[x1][y1] == Tileset.NOTHING && w[x2][y2] == Tileset.NOTHING) {
                    if (distance(x1,y1,x2,y2) >= 30) {
                        if(isConnected(index1,index2)) {
                            w[x1][y1] = Tileset.PLAYER;
                            w[x2][y2] = Tileset.LOCKED_DOOR;
                            return new int[][]{{x1,y1},{x2,y2}};
                        }
                    }
                }
            }
        }

        // 俩点间的距离
       private double distance(int x1, int y1, int x2, int y2) {
            double dx = Math.abs(x1 - x2);
            double dy = Math.abs(y1 - y2);
            return Math.sqrt(dx * dx + dy * dy);
        }
    }
}
