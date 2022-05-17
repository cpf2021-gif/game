package com.P2.byog.lab7;

import com.P2.byog.TileEngine.TETile;
import com.P2.byog.TileEngine.Tileset;

public class Shot implements Runnable{
    int x;
    int y;
    int direction;
    TETile[][] w;

    public Shot(int x, int y, int direction, TETile[][] w) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.w = w;
    }

    @Override
    public void run() {
        while(true) {
            int dx;
            int dy;
            switch(direction){
                case 1: // 上
                    dx = x;dy = y + 1;break;
                case 2: // 下
                    dx = x;dy = y - 1;break;
                case 3: // 左
                    dx = x -1;dy = y;break;
                case 4: // 右
                    dx = x + 1;dy = y;break;
                    default:
                        dx = x;dy = y;break;
            }

            if(isOk(dx,dy)) {
                if (w[dx][dy] == Tileset.NOTHING) {
                    TETile t = w[x][y];
                    w[x][y] = w[dx][dy];
                    w[dx][dy] = t;
                    x = dx;
                    y = dy;
                }
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isOk(int x, int y) {
        return x >= 0 && x < 50 && y >=0 && y < 50;
    }
}
