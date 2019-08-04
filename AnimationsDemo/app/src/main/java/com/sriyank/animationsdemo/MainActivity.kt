package com.sriyank.animationsdemo

import android.animation.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.BounceInterpolator
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
/*    var alphaAnimation:Animator? = null
    var rotateAnimation:Animator? = null
    var scaleAnimation:Animator? = null
    var translateAnimation:Animator? = null*/


    var alphaAnimation: ObjectAnimator? = null
    var rotateAnimation: ObjectAnimator? = null
    var scaleAnimation: ObjectAnimator? = null
    var translateAnimation: ObjectAnimator? = null
    val mListener = AnimatorListener()
    var checked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        avdImgView.setOnClickListener {
            if(!checked){
                avdImgView.setImageResource(R.drawable.avd_check_to_cross)
                val avDrawable = avdImgView.drawable as Animatable
                avDrawable.start()
            }else{
                avdImgView.setImageResource(R.drawable.avd_cross_to_check)
                val avDrawable = avdImgView.drawable as Animatable
                avDrawable.start()
            }
            checked = !checked
        }
    }

    fun onClickAlpha(view: View) {
/*        alphaAnimation = AnimatorInflater.loadAnimator(this,R.animator.alpha)
        alphaAnimation?.apply {
            setTarget(wheel)
            addListener(mListener)
            start()

        }*/

        alphaAnimation = ObjectAnimator.ofFloat(wheel, "alpha", 1.0f, 0.0f)
        alphaAnimation?.apply {
            duration = 1000
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            addListener(mListener)
            start()
        }
    }

    fun onClickRotate(view: View) {
/*        rotateAnimation = AnimatorInflater.loadAnimator(this,R.animator.rotate)
        rotateAnimation?.apply {
            setTarget(wheel)
            addListener(mListener)
            start()
        }*/
        rotateAnimation = ObjectAnimator.ofFloat(wheel, "rotation", 0.0f, -180.0f)
        rotateAnimation?.apply {
            duration = 1000
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            addListener(mListener)
            start()
        }
    }

    fun onClickScale(view: View) {
        /*       scaleAnimation = AnimatorInflater.loadAnimator(this,R.animator.scale)
               scaleAnimation?.apply {
                   setTarget(wheel)
                   addListener(mListener)
                   start()
               }*/
        scaleAnimation = ObjectAnimator.ofFloat(wheel, "scaleX", 1.0f, 1.5f)
        scaleAnimation?.apply {
            duration = 1000
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 1
            addListener(mListener)
            start()
        }
    }

    fun onClickTranslate(view: View) {
/*        translateAnimation = AnimatorInflater.loadAnimator(this,R.animator.translate)
        translateAnimation?.apply {
            setTarget(wheel)
            addListener(mListener)
            start()
        }*/

        translateAnimation = ObjectAnimator.ofFloat(wheel, "translationX", 0.0f, 200.0f)
        translateAnimation?.apply {
            duration = 1000
            repeatCount = 1
            repeatMode = ValueAnimator.REVERSE
            addListener(mListener)
            start()
        }
    }

    fun onClickSetXml(view: View) {
        val animator = AnimatorInflater.loadAnimator(this, R.animator.set)
        animator?.apply {
            setTarget(wheel)
            start()
        }
    }

    fun onClickSetCode(view: View) {
        val rootSet = AnimatorSet()
        val rotationY = ObjectAnimator.ofFloat(wheel, "rotationY", 0.0f, 360.0f);
        rotationY.duration = 1000
        val childSet = AnimatorSet()
        val scaleX = ObjectAnimator.ofFloat(wheel, "scaleX", 1.0f, 1.5f)
        scaleX.duration = 1000
        scaleX.interpolator = BounceInterpolator()
        val scaleY = ObjectAnimator.ofFloat(wheel, "scaleY", 1.0f, 1.5f)
        scaleY.duration = 1000
        scaleY.interpolator = BounceInterpolator()
/*        childSet.playTogether(scaleX,scaleY)
        rootSet.playSequentially(rotationY,childSet)*/

        childSet.play(scaleX).with(scaleY)
        rootSet.play(childSet).after(rotationY)
        rootSet.start()
    }

    /*
        Animation runs in parallel. Dont require start values
        Since ViewPropertyAnimator doesnt use ObjectAnimator, animations cant be controlled
        Suitable for simple animations
        Efficient than objectanimator
    * */
    fun onClickViewPropertyAnimation(view: View) {
        val animator = wheel.animate()
        animator.apply {
            duration = 1000
            scaleX(1.5f)
            scaleY(1.5f)
            rotationX(360.0f)
            interpolator = BounceInterpolator()
            start()
        }
    }


    /*
        Animation runs in parallel. Dont require start values
        Since PropertyValuesHolder uses ObjectAnimator, animations can be controlled by using animatorlistener
    * */
    fun onClickViewPropertyHolder(view: View) {
        val rotationX = PropertyValuesHolder.ofFloat("rotationX", 360.0f)
        val scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.5f)
        val scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.5f)

        val animator = ObjectAnimator.ofPropertyValuesHolder(wheel, rotationX, scaleX, scaleY)
        animator.apply {
            animator.duration = 1000
            animator.interpolator = BounceInterpolator()
            start()
        }
    }


    inner class AnimatorListener : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {
            when (animation) {
                translateAnimation -> Toast.makeText(
                    this@MainActivity,
                    "translateAnimation onRepeat",
                    Toast.LENGTH_SHORT
                ).show()
                rotateAnimation -> Toast.makeText(
                    this@MainActivity,
                    "rotateAnimation onRepeat",
                    Toast.LENGTH_SHORT
                ).show()
                scaleAnimation -> Toast.makeText(
                    this@MainActivity,
                    "scaleAnimation onRepeat",
                    Toast.LENGTH_SHORT
                ).show()
                alphaAnimation -> Toast.makeText(
                    this@MainActivity,
                    "alphaAnimation onRepeat",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onAnimationEnd(animation: Animator?) {
            when (animation) {
                translateAnimation -> Toast.makeText(
                    this@MainActivity,
                    "translateAnimation onEnd",
                    Toast.LENGTH_SHORT
                ).show()
                rotateAnimation -> Toast.makeText(this@MainActivity, "rotateAnimation onEnd", Toast.LENGTH_SHORT).show()
                scaleAnimation -> Toast.makeText(this@MainActivity, "scaleAnimation onEnd", Toast.LENGTH_SHORT).show()
                alphaAnimation -> Toast.makeText(this@MainActivity, "alphaAnimation onEnd", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onAnimationCancel(animation: Animator?) {
            when (animation) {
                translateAnimation -> Toast.makeText(
                    this@MainActivity,
                    "translateAnimation onCancel",
                    Toast.LENGTH_SHORT
                ).show()
                rotateAnimation -> Toast.makeText(
                    this@MainActivity,
                    "rotateAnimation onCancel",
                    Toast.LENGTH_SHORT
                ).show()
                scaleAnimation -> Toast.makeText(
                    this@MainActivity,
                    "scaleAnimation onCancel",
                    Toast.LENGTH_SHORT
                ).show()
                alphaAnimation -> Toast.makeText(
                    this@MainActivity,
                    "alphaAnimation onCancel",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        override fun onAnimationStart(animation: Animator?) {
            when (animation) {
                translateAnimation -> Toast.makeText(
                    this@MainActivity,
                    "translateAnimation onStart",
                    Toast.LENGTH_SHORT
                ).show()
                rotateAnimation -> Toast.makeText(
                    this@MainActivity,
                    "rotateAnimation onStart",
                    Toast.LENGTH_SHORT
                ).show()
                scaleAnimation -> Toast.makeText(this@MainActivity, "scaleAnimation onStart", Toast.LENGTH_SHORT).show()
                alphaAnimation -> Toast.makeText(this@MainActivity, "alphaAnimation onStart", Toast.LENGTH_SHORT).show()
            }
        }

    }

}


