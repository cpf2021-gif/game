package com.P2.byog.lab7;

import com.P2.byog.TileEngine.TERenderer;
import com.P2.byog.TileEngine.TETile;
import com.P2.byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

public class tryShot implements Runnable{

    TERenderer ter;
    TETile[][] w;

    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        TETile[][] w = new TETile[50][50];

        for(int i = 0; i < 50; i++) {
            for(int j = 0; j< 50; j++) {
                w[i][j] = Tileset.NOTHING;
            }
        }
        w[0][24] = Tileset.SHOT;

        ter.initialize(50,50);
        tryShot tryshot = new tryShot(ter, w);
        new Thread(tryshot).start();

        while(true){
            if(StdDraw.hasNextKeyTyped()) {
                char ch = StdDraw.nextKeyTyped();
                if (ch == 'j') {
                    Shot shot = new Shot(0, 24, 4, w);
                    new Thread(shot).start();
                }
            }
        }

    }

    public tryShot(TERenderer ter, TETile[][] w) {
        this.ter = ter;
        this.w = w;
    }

    @Override
    public void run() {
        while(true){
            ter.renderFrame(w);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
