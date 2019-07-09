package com.jwhh.notekeeper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.widget.SeekBar

/**
 * Created by Saran on 9/7/19.
 */
class ColorPicker @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet?=null,
        defStyleAttr:Int = R.attr.seekBarStyle,
        defStyleRes:Int = 0
): SeekBar(context, attrs, defStyleAttr, defStyleRes) {

    private var colorsList = listOf(Color.RED,Color.GREEN,Color.BLUE)

    init {

        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.ColorPicker)

        try{
            colorsList = typedArray.getTextArray(R.styleable.ColorPicker_colors)
                    .map {
                        Color.parseColor(it.toString())
                    }
        }finally {
            typedArray.recycle()
        }
        max = colorsList.size-1
        splitTrack = false
        progressBackgroundTintList = ContextCompat.getColorStateList(context,android.R.color.transparent)
        progressTintList = ContextCompat.getColorStateList(context,android.R.color.transparent)
        thumb = context.getDrawable(R.drawable.ic_arrow_drop_down_black_24dp)
        setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom+40)
        //setBackgroundColor(resources.getColor(android.R.color.black))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTicks(canvas)
    }

    fun drawTicks(canvas:Canvas?){
        canvas?.let {
            val w = 48
            val h = 48
            val halfWidth = if(w==0)1f else w/2f
            val halfHeight = if(h==0)1f else h/2f
            val spacing = (width - paddingLeft - paddingRight)/(colorsList.size-1).toFloat()

            val saveCount = canvas.save()

            canvas.translate(paddingLeft.toFloat(),(height/2).toFloat() + 40f)

            for(i in 0 until colorsList.size){
                val paint = Paint()
                paint.color = colorsList[i]
                canvas.drawRect(-halfWidth,-halfHeight,halfWidth,halfHeight,paint)
                canvas.translate(spacing,0f)
            }
            canvas.restoreToCount(saveCount)
        }
    }
}