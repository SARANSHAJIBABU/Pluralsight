package com.jwhh.notekeeper

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.color_selector.view.*

/**
 * Created by Saran on 4/7/19.
 */


class ColorSelector @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet?=null,defstyleAttr:Int=0,defstyleRes:Int=0)
    : LinearLayout(context,attributeSet,defstyleAttr,defstyleRes) {

    private val listOfColors = listOf(Color.RED, Color.BLUE, Color.GREEN)
    private var selectedIndex = 0
    private var mListener: ColorSelectedListener? = null

    init {
        orientation = LinearLayout.HORIZONTAL

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.color_selector,this)
        color_selector_display.setBackgroundColor(listOfColors[selectedIndex])

        color_selector_left.setOnClickListener {
            moveToPrevious()
        }

        color_selector_right.setOnClickListener {
            moveToNext()
        }

        colo_selector_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            broadCastColor()
         }
    }

    fun moveToPrevious(){
        if(selectedIndex==0){
            selectedIndex = listOfColors.lastIndex
        }else{
            selectedIndex--
        }
        color_selector_display.setBackgroundColor(listOfColors[selectedIndex])
    }

    fun moveToNext(){
        if(selectedIndex==listOfColors.lastIndex){
            selectedIndex = 0
        }else{
            selectedIndex++
        }
        color_selector_display.setBackgroundColor(listOfColors[selectedIndex])
    }

    interface ColorSelectedListener{
        fun onColorSected(color:Int)
    }

    fun setSelectedColor(color:Int){
        val index = listOfColors.indexOf(color)
        if(index==-1){
            colo_selector_checkbox.isChecked = false
        }else{
            colo_selector_checkbox.isChecked = true
            selectedIndex = index
            color_selector_display.setBackgroundColor(color)
        }
    }

    fun setColorSelectedListener(listener: ColorSelectedListener){
        mListener = listener
    }

    fun broadCastColor(){
        mListener?.onColorSected(listOfColors[selectedIndex])
    }
}