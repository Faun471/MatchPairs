package me.faun.matchpairs.utils;

import android.animation.*;
import android.view.View;
import android.view.animation.OvershootInterpolator;

public class ViewUtils {
    public static void animateBounce(View view) {
        // create an ObjectAnimator for scaling the card
        ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", 1f, 0.8f, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 1f, 0.8f, 1f)
        );
        scale.setDuration(250);
        scale.setInterpolator(new OvershootInterpolator());

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scale);
        animSet.start();
    }

    public static void animateBounce(View view, AnimatorListenerAdapter listener) {
        // create an ObjectAnimator for scaling the card
        ObjectAnimator scale = ObjectAnimator.ofPropertyValuesHolder(view,
                PropertyValuesHolder.ofFloat("scaleX", 1f, 0.8f, 1f),
                PropertyValuesHolder.ofFloat("scaleY", 1f, 0.8f, 1f)
        );
        scale.setDuration(250);
        scale.setInterpolator(new OvershootInterpolator());

        scale.addListener(listener);

        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scale);
        animSet.start();
    }

    public static void animateShrink(View view) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f);
        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(scaleDownX, scaleDownY);
        scaleDown.setDuration(250);
        scaleDown.start();
    }

    public static void animateGrow(View view) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f);
        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.playTogether(scaleDownX, scaleDownY);
        scaleDown.setDuration(250);
        scaleDown.start();
    }
}
