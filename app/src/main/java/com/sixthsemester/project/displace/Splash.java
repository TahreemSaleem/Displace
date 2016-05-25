package com.sixthsemester.project.displace;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;


public class Splash  extends Activity implements Runnable {
    ImageView img;
    ImageView img2;
    protected ProgressBar pbLoad;
    MediaPlayer mp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        img2= (ImageView) findViewById(R.id.logo);
        img2.clearAnimation();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        TranslateAnimation transAnim = new TranslateAnimation(0, 0, 0,getDisplayHeight()/4);
        transAnim.setStartOffset(500);
        transAnim.setDuration(4000);
        transAnim.setFillAfter(true);
        transAnim.setInterpolator(new BounceInterpolator());transAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {


            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                final int left = img2.getLeft();
                final int top =img2.getTop();
                final int right = img2.getRight();
                final int bottom = img2.getBottom();
                img2.layout(left, getDisplayHeight()/2, right, getDisplayHeight()/2);

                Intent intent = new Intent(Splash.this, Login.class);
                startActivity(intent);
            }
        });
        img2.startAnimation(transAnim);
        mp=MediaPlayer.create(this,R.raw.longsound);

        mp.start();

        final ImageView image = (ImageView)findViewById(R.id.logo);

        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        final Animation animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        final Animation animationFadeIn2 = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
        final Animation animationFadeIn3 = AnimationUtils.loadAnimation(this, R.anim.property_animator);




    }


    @Override
    public void run() {
    }
    private int getDisplayHeight() {
        return this.getResources().getDisplayMetrics().heightPixels;
    }

}
