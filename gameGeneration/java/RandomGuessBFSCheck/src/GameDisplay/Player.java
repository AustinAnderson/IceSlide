package GameDisplay;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import Common.Coordinate;
import Common.Direction;

public class Player {
    private BufferedImage currentSprite;
    private final static int sleepTime=3;
    private final Map<Direction,BufferedImage[]> spriteMap;
    private final int tileHeight;
    private final int tileWidth;
    private final JFrame parent;
    private AtomicBoolean moving;
    private int row;
    private int col;
    private AtomicInteger x;
    private AtomicInteger y;
    public Player(int initialRow,int initialCol,int tileHeight,int tileWidth,JFrame parent) throws IOException{
        this.parent=parent;
        this.moving=new AtomicBoolean(false);
        this.row=initialRow;
        this.col=initialCol;
        this.tileHeight=tileHeight;
        this.tileWidth=tileWidth;
        this.x=new AtomicInteger(initialCol*tileWidth);
        this.y=new AtomicInteger(initialRow*tileHeight);
        spriteMap=new HashMap<>();
        
        spriteMap.put(Direction.DOWN, new BufferedImage[]{
            ImageIO.read(new File("Resources/FairUse/downStep.png")),
            ImageIO.read(new File("Resources/FairUse/down.png"))
        });
        spriteMap.put(Direction.LEFT, new BufferedImage[]{
            ImageIO.read(new File("Resources/FairUse/leftStep.png")),
            ImageIO.read(new File("Resources/FairUse/left.png"))
        });
        spriteMap.put(Direction.RIGHT, new BufferedImage[]{
            ImageIO.read(new File("Resources/FairUse/rightStep.png")),
            ImageIO.read(new File("Resources/FairUse/right.png"))
        });
        spriteMap.put(Direction.UP, new BufferedImage[]{
            ImageIO.read(new File("Resources/FairUse/upStep.png")),
            ImageIO.read(new File("Resources/FairUse/up.png"))
        });
        currentSprite=spriteMap.get(Direction.DOWN)[1];
    }
    
    public Coordinate getPos(){return new Coordinate(row,col);}
    //return -1,0, or 1
    private int sign(int value){
        if(value==0) return 0;
        return (value>>31)|1;//arithmetic shift to fill everything with sign bit, then last number is 1
    }
    public Thread updatePosition(Coordinate target,Direction dir){
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
                            parent.repaint();
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
                            parent.repaint();
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
    public void paintComponent(Graphics g){
        g.drawImage(currentSprite, x.get(),y.get(),null);
    }
}
