package com.test.andersonau.simplegame;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class MainThread extends Thread {

    public static final int MAX_FPS=60;
    private double averageFPS;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    private static Canvas canvas;
    public MainThread(SurfaceHolder surfaceHolder,GamePanel gamePanel){
        super();
        this.surfaceHolder=surfaceHolder;
        this.gamePanel=gamePanel;
    }
    public void setRunning(boolean value){
        running=value;
    }
    @Override
    public void run(){
        long startTime;
        long timeMillis=1000/MAX_FPS;
        long waitTime;
        int frameCount=0;
        long totalTime=0;
        long targetTime=timeMillis;
        while(running){
            startTime=System.nanoTime();
            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    gamePanel.draw(canvas);
                }
            }catch (Exception ex){ex.printStackTrace();}
            finally {
                if(canvas!=null){
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception ex){ex.printStackTrace();}
                }
            }
            timeMillis=(System.nanoTime()-startTime)/1000000;
            waitTime=targetTime-timeMillis;
            if(waitTime>0){
                try{
                    Thread.sleep(waitTime);
                }catch (Exception swallowed){}
            }
            frameCount++;
            totalTime+=System.nanoTime()-startTime;
            if(frameCount>=MAX_FPS){
                averageFPS=1000/((totalTime/frameCount)/1000000);
                frameCount=0;
                totalTime=0;
                System.out.println(averageFPS);
            }
        }
    }

}