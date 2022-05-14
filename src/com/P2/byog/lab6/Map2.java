package com.P2.byog.lab6;

import java.io.Serializable;
import java.util.Random;

public class Map2 implements Serializable {

    private static final long serialVersionUID = 123123123123123L;

    public final int WIDTH = 50;
    public final int HEIGHT = 50;
    public int width;
    public int height;
    public int[][] w;
    public UnionDs union;
    public int playerX;
    public int playerY;
    public int targetX;
    public int targetY;

    public Map2(int width, int height) {

        this.width = width;
        this.height = height;
        w = new int[width][height];
        // 初始化
        initialize();
        connect();
        setXY();
    }

    // 初始化
    public void initialize() {
        // 地板是0 墙是1 人是2 目标是3

        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                Random random = new Random();
                int x = random.nextInt(100);
                // 墙的概率 30 %
                if(x < 35 || i == 0 || i == width - 1|| j == 0 || j == height - 1) {
                    w[i][j] = 1;
                }
                else {
                    w[i][j] = 0;
                }
            }
        }
    }

    //在范围内
    public boolean isOk(int x, int y) {
        return x >= 0 && x < width && y < height && y >= 0;
    }



    // 在外部直接调用,隐藏真正的细节
    public void connect() {
        if(union == null) {
            union = new UnionDs();
        }
        union.connect();
    }

    public int[][] SelectPoint() {
        return union.SelectPoint();
    }

    private void setXY() {
        int[][] p = SelectPoint();
        playerX = p[0][0];
        playerY = p[0][1];
        targetX = p[1][0];
        targetY = p[1][1];
    }

    public int[][] getWorld() {
        return w;
    }


    // 通过并查集 找到连通的部分;
    class UnionDs implements Serializable {

        private static final long serialVersionUID = 45498234798734234L;

        public int[] parent;
        public int[] size;

        public UnionDs() {
            parent = new int[width*height];
            size = new int[width*height];
            // 初始化
            for(int i = 0; i < width; i++) {
                for(int j = 0; j < height; j++) {
                    int index = j * 50 + i;
                    if(w[i][j] == 1) {
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
        public void connect() {
            for(int i = 0; i < width; i++) {
                for(int j = 0; j < height; j++) {
                    if(w[i][j] == 0) {
                        // 1. 向上
                        if (isOk(i,j+1)) {
                            if (w[i][j+1] == 0) {
                                int index1 = toIndex(i,j);
                                int index2 = toIndex(i,j+1);
                                connected(index1, index2);
                            }
                        }
                        // 2. 向右
                        if (isOk(i+1,j)) {
                            if (w[i+1][j] == 0) {
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
        public int[][] SelectPoint() {
            while(true) {
                Random random = new Random();
                int x1 = random.nextInt(width);
                int x2 = random.nextInt(width);
                int y1 = random.nextInt(height);
                int y2 = random.nextInt(height);
                int index1 = toIndex(x1,y1);
                int index2 = toIndex(x2,y2);
                if (w[x1][y1] == 0 && w[x2][y2] == 0) {
                    if (distance(x1,y1,x2,y2) >= 30) {
                        if(isConnected(index1,index2)) {
                            w[x1][y1] = 2;
                            w[x2][y2] = 3;
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
