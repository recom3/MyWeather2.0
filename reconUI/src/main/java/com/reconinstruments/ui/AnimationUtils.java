package com.reconinstruments.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewPropertyAnimator;

/**
 * Utilities to abstract the different transition animations for going
 * between Activities.
 * Simple method of associating transition animations
 */
public class AnimationUtils {

    public enum Transition {
        NEXT(R.anim.fade_in_slide_in_right, R.anim.fade_out),
        PREVIOUS(R.anim.fade_in_slide_in_left, R.anim.fade_out),
        NEW(R.anim.fade_in_slide_in_bottom, R.anim.fade_out),
        PARENT(R.anim.fade_in_slide_in_top, R.anim.fade_out),
        SCROLL_UP(R.anim.slide_in_bottom, R.anim.slide_out_top),
        SCROLL_DOWN(R.anim.slide_in_top, R.anim.slide_out_bottom);

        int enter_anim;
        int exit_anim;
        Transition(int enter_anim, int exit_anim) {
            this.enter_anim = enter_anim;
            this.exit_anim = exit_anim;
        }
    }

    public static void transition(Activity activity,Transition transition) {
        activity.overridePendingTransition(transition.enter_anim, transition.exit_anim);
    }
}