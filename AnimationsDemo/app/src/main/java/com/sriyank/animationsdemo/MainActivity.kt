package com.sriyank.animationsdemo

import android.animation.AnimatorInflater
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickAlpha(view: View){
        val alphaAnimation = AnimatorInflater.loadAnimator(this,R.animator.alpha)
        alphaAnimation?.apply {
            setTarget(wheel)
            start()
        }
    }

    fun onClickRotate(view: View){
        val rotateAnimation = AnimatorInflater.loadAnimator(this,R.animator.rotate)
        rotateAnimation?.apply {
            setTarget(wheel)
            start()
        }
    }
}
