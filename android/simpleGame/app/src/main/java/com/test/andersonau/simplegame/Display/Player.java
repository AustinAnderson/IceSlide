package com.test.andersonau.simplegame.Display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.test.andersonau.simplegame.BitmapManager;
import com.test.andersonau.simplegame.Common.Coordinate;
import com.test.andersonau.simplegame.Common.Direction;
import com.test.andersonau.simplegame.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Player {
    private Bitmap currentSprite;
    private final static int sleepTime=3;
    private final Map<Direction,Bitmap[]> spriteMap;
    private final int tileHeight;
    private final int tileWidth;
    private AtomicBoolean moving;
    private int row;
    private int col;
    private AtomicInteger x;
    private AtomicInteger y;
    public Player(Coordinate startCoordinate, int tileHeight, int tileWidth, BitmapManager picManager){
        this.moving=new AtomicBoolean(false);
        row=startCoordinate.r;
        col=startCoordinate.c;
        this.tileHeight=tileHeight;
        this.tileWidth=tileWidth;
        this.x=new AtomicInteger(col*tileWidth);
        this.y=new AtomicInteger(row*tileHeight);
        spriteMap=new HashMap<>();

        spriteMap.put(Direction.DOWN, new Bitmap[]{
                picManager.getBitmap(R.drawable.downstep),
                picManager.getBitmap(R.drawable.down),
        });
        spriteMap.put(Direction.LEFT, new Bitmap[]{
                picManager.getBitmap(R.drawable.leftstep),
                picManager.getBitmap(R.drawable.left),
        });
        spriteMap.put(Direction.RIGHT, new Bitmap[]{
                picManager.getBitmap(R.drawable.rightstep),
                picManager.getBitmap(R.drawable.right),
        });
        spriteMap.put(Direction.UP, new Bitmap[]{
                picManager.getBitmap(R.drawable.upstep),
                picManager.getBitmap(R.drawable.up),
        });
        currentSprite=spriteMap.get(Direction.DOWN)[1];
    }

    public Coordinate getPos(){return new Coordinate(row,col);}
    public void resetToStart(Coordinate start){
        row=start.r;
        col=start.c;
    }
    //return -1,0, or 1
    private int sign(int value){
        if(value==0) return 0;
        return (value>>31)|1;//arithmetic shift to fill everything with sign bit, then last number is 1
    }
    public Thread updatePosition(final Coordinate target,final Direction dir){
        Thread t=null;
        if(!moving.get()){
            currentSprite=spriteMap.get(dir)[0];
            t=new Thread(new Runnable(){
                @Override
                public void run(){
                    moving.set(true);
                    int dY=(target.r*tileHeight)-y.get();
                    int dX=(target.c*tileWidth)-x.get();
                    int incX=sign(dX);
                    int incY=sign(dY);
                    int targetDist=Math.abs(dY);
                    if(Math.abs(dX)>targetDist) targetDist=Math.abs(dX);

                    if(targetDist<tileWidth){
                        for(int i=0;i<tileHeight*2;i++){
                            try {Thread.sleep(sleepTime);} catch (InterruptedException e) {}
                            if(i==tileHeight-1) currentSprite=spriteMap.get(dir)[1];
                        }
                    }
                    else{
                        for(int i=0;i<targetDist;i++){
                            if(i==(tileHeight-1)){
                                currentSprite=spriteMap.get(dir)[1];
                            }
                            y.addAndGet(incY);
                            x.addAndGet(incX);
                            try {Thread.sleep(sleepTime);} catch (InterruptedException e) {}
                        }
                    }
                    row=target.r;
                    col=target.c;
                    moving.set(false);
                }
            });
            t.start();
        }
        return t;
    }
    public void draw(Canvas c){
        Rect r=new Rect(x.get(),y.get(),tileWidth+x.get(),tileHeight+y.get());
        c.drawBitmap(currentSprite,null,r,null);
    }
}