package com.test.andersonau.simplegame;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.test.andersonau.simplegame.Common.Direction;
import com.test.andersonau.simplegame.Display.BoardImage;
import com.test.andersonau.simplegame.Display.Player;
import com.test.andersonau.simplegame.Logic.Board;

public class Game implements Playable{

    private final int maxWidth;
    private final int maxHeight;
    private Player player;
    private BoardImage boardImage;
    private Board board;
    private final Context context;

    private final static int boardRows=15;
    private final static int boardCols=10;
    private final int tileSideLength;

    public Game(int maxHeight, int maxWidth, Context context){
        this.context=context;
        this.maxHeight=maxHeight;
        this.maxWidth=maxWidth;
        BitmapManager bitmapManager=new BitmapManager(context);
        int tileSideLengthCalc=maxWidth/boardCols;
        if(boardCols>boardRows) tileSideLengthCalc=maxHeight/boardRows;
        tileSideLength=tileSideLengthCalc;
        board=new Board(boardRows,boardCols,20);
        player=new Player(board.getStartCoord(),tileSideLength,tileSideLength,bitmapManager);
        boardImage=new BoardImage(tileSideLength,tileSideLength,bitmapManager,board.toBoolMap());
    }
    public void newGame(){
        board=new Board(boardRows,boardCols,20);
        player.resetToStart(board.getStartCoord());
        boardImage.update(tileSideLength,tileSideLength,board.toBoolMap());
    }

    private Thread move(Direction dir){
        Thread moveTask=null;
        if(dir!=null){
            moveTask=player.updatePosition(board.getNextPosition(player.getPos(), dir),dir);
        }
        return moveTask;
    }
    public View.OnTouchListener getTouchListener(){
        return new OnSwipeTouchListener(context) {
            @Override
            public void onSwipeRight() {
                move(Direction.RIGHT);
            }
            @Override
            public void onSwipeLeft() {
                move(Direction.LEFT);
            }

            @Override
            public void onSwipeTop() {
                move(Direction.UP);
            }

            @Override
            public void onSwipeBottom() {
                move(Direction.DOWN);
            }
        };
    }

    public void draw(Canvas canvas){
        boardImage.draw(canvas);
        player.draw(canvas);
    }

}