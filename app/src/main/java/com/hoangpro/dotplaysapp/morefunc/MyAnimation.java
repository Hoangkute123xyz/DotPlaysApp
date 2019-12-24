package com.hoangpro.dotplaysapp.morefunc;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public class MyAnimation {
    public static void setAnimAlpha(View view){
        view.setAlpha(0f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "Alpha", 1f);
        objectAnimator.setDuration(1500);

        ObjectAnimator objectAnimatorBack = ObjectAnimator.ofFloat(view, "Alpha", 0.5f);
        objectAnimatorBack.setDuration(1500);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(objectAnimator).before(objectAnimatorBack);
        animatorSet.start();
    }
}
