package com.sriyank.animationsdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var visibility = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun fadeAnimation(view: View) {
        TransitionManager.beginDelayedTransition(sceneRoot,Fade())
        //1. framework calls transition’s captureStartValues() for each view in the scene and the transition records each view’s visibility.

        txvDescription.visibility = if(visibility) View.INVISIBLE else View.VISIBLE
        //2. framework calls transition’s captureEndValues() method for each view in the scene and the transition records each view’s (recently updated) visibility.
        //3. framework calls transition’s createAnimator() method. The transition analyzes the start and end values of each view and notices a difference:
        //4. Fade transition uses this information to create and return an AnimatorSet that will fade each view’s alpha property to 0f.
        //5. framework runs the returned Animator,
        visibility = !visibility
    }

    fun slideEffect(view: View) {
        //
        TransitionManager.beginDelayedTransition(sceneRoot,Slide(Gravity.END))
        txvDescription.visibility = if(visibility) View.INVISIBLE else View.VISIBLE
        visibility = !visibility
    }
}
