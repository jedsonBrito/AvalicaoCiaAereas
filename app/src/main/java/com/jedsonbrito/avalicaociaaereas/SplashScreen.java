package com.jedsonbrito.avalicaociaaereas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity  implements AnimationListener {

    Animation animMove;
    private ImageView plane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        plane = findViewById(R.id.planeList);

        //AnimationDrawable turbulencia = (AnimationDrawable) plane.getDrawable();
        //turbulencia.start();

        animMove = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.logo_animation);
        plane.setAnimation(animMove);
        animMove.setAnimationListener(this);

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
            Intent intent = new Intent(getApplicationContext(),
                    MainActivity.class);
            startActivity(intent);
            finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
