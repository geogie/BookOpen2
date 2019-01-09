package com.george.bookopen;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main2Activity extends AppCompatActivity {
    private View picTop;
    private AnimatorSet mAnimatorSet1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        picTop = findViewById(R.id.picTop);
    }
    @Override
    public void finish() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(picTop, "alpha", 0, 1);
        alpha.setDuration(500);
        mAnimatorSet1 = new AnimatorSet();
        mAnimatorSet1.playTogether(alpha);
        mAnimatorSet1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Main2Activity.super.finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimatorSet1.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(picTop, "alpha", 1, 0);
        alpha.setDuration(200);
        mAnimatorSet1 = new AnimatorSet();
        mAnimatorSet1.playTogether(alpha);
        mAnimatorSet1.start();
    }
}
