package com.jwhh.notekeeper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
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

    private var colorsList = mutableListOf(Color.RED,Color.GREEN,Color.BLUE)

    init {

        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.ColorPicker)

        try{
            colorsList = typedArray.getTextArray(R.styleable.ColorPicker_colors)
                    .map {
                        Color.parseColor(it.toString())
                    } as MutableList<Int>
        }finally {
            typedArray.recycle()
        }

        colorsList.add(0,Color.TRANSPARENT)
        max = colorsList.size-1
        splitTrack = false
        progressBackgroundTintList = ContextCompat.getColorStateList(context,android.R.color.transparent)
        progressTintList = ContextCompat.getColorStateList(context,android.R.color.transparent)
        thumb = context.getDrawable(R.drawable.ic_arrow_drop_down_black_24dp)
        setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom+40)
   //     setBackgroundColor(resources.getColor(android.R.color.black))

        setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                listeners.forEach{
                    it(colorsList[progress])
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawTicks(canvas)
    }

    fun drawTicks(canvas:Canvas?){
        if(colorsList.size>0){
            canvas?.let {
                val w = 48
                val h = 48
                val halfWidth = if(w==0)1f else w/2f
                val halfHeight = if(h==0)1f else h/2f
                val spacing = (width - paddingLeft - paddingRight)/(colorsList.size-1).toFloat()

                val saveCount = canvas.save()

                canvas.translate(paddingLeft.toFloat(),(height/2).toFloat() + 40f)

                for(i in 0 until colorsList.size){
                    if(i==0){
                        val d:Drawable?= context.getDrawable(R.drawable.ic_clear_black)
                        val h2 = d?.intrinsicHeight ?: 0
                        val w2 = d?.intrinsicWidth ?: 0
                        val hh2 = if(h2>=0) h2/2 else 1
                        val hw2 = if(w2>=0) w2/2 else 1
                        d?.setBounds(-hw2,-hh2,hw2,hh2)
                        d?.draw(canvas)
                    }else{
                        val paint = Paint()
                        paint.color = colorsList[i]
                        canvas.drawRect(-halfWidth,-halfHeight,halfWidth,halfHeight,paint)
                    }
                    canvas.translate(spacing,0f)
                }
                canvas.restoreToCount(saveCount)
            }
        }
    }

    private var listeners: ArrayList<(Int)->Unit> = ArrayList()

    var selectedColor = Color.TRANSPARENT
            set(value){
                if(colorsList.indexOf(value)==-1){
                    progress = 0
                }else{
                    progress = colorsList.indexOf(value)
                }
            }

    fun addListener(func:(Int)->Unit){
        listeners.add(func)
    }
}