package com.jwhh.notekeeper

import android.content.Context
import android.graphics.Color
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
        max = colorsList.size-1
        splitTrack = false
        progressBackgroundTintList = ContextCompat.getColorStateList(context,android.R.color.transparent)
        progressTintList = ContextCompat.getColorStateList(context,android.R.color.transparent)
    }
}