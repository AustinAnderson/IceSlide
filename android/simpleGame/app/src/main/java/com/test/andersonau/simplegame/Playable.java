package com.test.andersonau.simplegame;

import android.graphics.Canvas;
import android.view.View;

public interface Playable {
    void draw(Canvas canas);
    View.OnTouchListener getTouchListener();
    void newGame();
}