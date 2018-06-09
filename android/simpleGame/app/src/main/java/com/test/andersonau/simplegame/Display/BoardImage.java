package com.test.andersonau.simplegame.Display;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.test.andersonau.simplegame.BitmapManager;
import com.test.andersonau.simplegame.R;


public class BoardImage {
    private static final long serialVersionUID = 1L;
    private final Bitmap Ice;
    private final Bitmap Rock;
    private boolean[][] rockMap;
    private int tileHeight;
    private int tileWidth;
    public BoardImage(int tileHeight,int tileWidth,BitmapManager manager,boolean[][]rockMap){
        this.rockMap=rockMap;
        this.tileHeight=tileHeight;
        this.tileWidth=tileWidth;
        Ice=manager.getBitmap(R.drawable.ice);
        Rock=manager.getBitmap(R.drawable.rock);
    }
    private Bitmap getImg(boolean rock){
        if(rock) return Rock;
        return Ice;
    }
    public void update(int tileHeight,int tileWidth,boolean[][] rockMap){
        this.tileWidth=tileWidth;
        this.tileHeight=tileHeight;
        this.rockMap=rockMap;
    }
    public void draw(Canvas canvas){
        for(int r=0;r<rockMap.length;r++){
            for(int c=0;c<rockMap[0].length;c++){
                Bitmap current=getImg(rockMap[r][c]);
                Rect dest=new Rect(c*tileWidth,r*tileHeight,c*tileWidth+tileWidth,r*tileHeight+tileHeight);
                canvas.drawBitmap(current,null,dest,null);
            }
        }
    }
}


