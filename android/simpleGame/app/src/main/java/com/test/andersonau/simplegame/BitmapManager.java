package com.test.andersonau.simplegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapManager {

    private Context context;
    private BitmapFactory factory=new BitmapFactory();
    public BitmapManager(Context context){
        this.context=context;
        factory=new BitmapFactory();
    }
    public Bitmap getBitmap(int Rwhich){
        return factory.decodeResource(context.getResources(),Rwhich);
    }
}