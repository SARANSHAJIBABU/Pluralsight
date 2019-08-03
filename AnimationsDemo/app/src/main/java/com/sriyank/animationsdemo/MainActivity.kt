package com.sriyank.animationsdemo

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickAlpha(view: View){
      //  Toast.makeText(this,"alpha",Toast.LENGTH_SHORT).show()
        val alphaAnimation = AnimatorInflater.loadAnimator(this,R.animator.alpha)
        alphaAnimation?.apply {
            setTarget(wheel)
            start()
        }
    }
}
