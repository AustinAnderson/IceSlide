package com.test.andersonau.simplegame;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_main, null);

        final RelativeLayout layout= (RelativeLayout) view.findViewById(R.id.MainLayoutId);
        final android.content.Context passingContext=this;
        /*
        setContentView(
                new GamePanel(
                        passingContext,
                        new Game(1000,700,passingContext)
                )
        );
        /*/
        layout.post(new Runnable() {
            @Override
            public void run() {
                int height=layout.getMeasuredHeight();
                int width=layout.getMeasuredWidth();
                setContentView(
                    new GamePanel(
                        passingContext,
                        new Game(height,width,passingContext)
                    )
                );
            }
        });
        //*/
    }
}
