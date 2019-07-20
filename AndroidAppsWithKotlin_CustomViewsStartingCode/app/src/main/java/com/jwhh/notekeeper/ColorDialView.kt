package com.jwhh.notekeeper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import kotlin.math.roundToInt

/**
 * TODO: document your custom view class.
 */
class ColorDialView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
    : View(context, attrs, defStyleAttr, defStyleRes) {

    private var colors:ArrayList<Int> = arrayListOf( Color.RED, Color.YELLOW, Color.BLUE,
            Color.GREEN, Color.DKGRAY, Color.CYAN,Color.MAGENTA, Color.BLUE)

    private var dialDrawable: Drawable? = null
    private var noColorDrawable: Drawable? = null
    private var dialDiameter = toDP(100)
    private var horizontalSize = 0f
    private var verticalSize = 0f

    private var centerHorizontal = 0f
    private var centerVertical = 0f

    private var extraPadding = toDP(30)
    private var tickSize = toDP(10).toFloat()
    private var angleBetweenColors = 0f

    private var totalLeftPadding =0f
    private var totalRightPadding =0f
    private var totalTopPadding =0f
    private var totalBottomPadding =0f

    private var scale = 1f
    private var tickSizeScaled = tickSize *scale
    private var scaleToFit = false

    private var tickPositionVertical =0f
    private var paint = Paint().also {
        it.color = Color.RED
        it.isAntiAlias = true
    }

    init {
        val typedArray = context.obtainStyledAttributes(attrs,R.styleable.ColorDialView)
        try {
            val customColors = typedArray.getTextArray(R.styleable.ColorPicker_colors)
                    ?.map {
                        Color.parseColor(it.toString())
                    } as ArrayList<Int>?

            customColors?.let {
                colors = customColors
            }

            dialDiameter = typedArray.getDimension(R.styleable.ColorDialView_dialDiameter,
                    toDP(100).toFloat()).toInt()

            tickSize = typedArray.getDimension(R.styleable.ColorDialView_tickRadius,toDP(10).toFloat())
            extraPadding = typedArray.getDimension(R.styleable.ColorDialView_tickPadding,toDP(30).toFloat()).toInt()
            scaleToFit = typedArray.getBoolean(R.styleable.ColorDialView_scaleToFit,false)

        }finally {
            typedArray.recycle()
        }
        dialDrawable = context.getDrawable(R.drawable.ic_dial).also {
            it?.bounds = getCenteredBound(dialDiameter)
            it?.setTint(Color.DKGRAY)
        }

        noColorDrawable= context.getDrawable(R.drawable.ic_clear_black).also {
            it?.bounds = getCenteredBound(tickSize.toInt(),2f)
        }

        colors.add(0,Color.TRANSPARENT)

        refreshValues(true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if(scaleToFit){
            refreshValues(false)
            val specWidth = MeasureSpec.getSize(widthMeasureSpec)
            val specHeight = MeasureSpec.getSize(heightMeasureSpec)
            val workingWidth = specWidth-paddingLeft-paddingRight
            val workingHeight = specHeight-paddingBottom-paddingTop
            scale = if(workingHeight<workingWidth){ //smallest is height
                workingHeight/(verticalSize-paddingTop-paddingBottom)
            }else{
                workingWidth/(horizontalSize-paddingLeft-paddingRight)
            }
            dialDrawable?.let {
                it.bounds = getCenteredBound((dialDiameter * scale).toInt())
            }

            noColorDrawable?.let {
                it.bounds = getCenteredBound((tickSize*scale).toInt(),2f)
            }
            val  width = resolveSizeAndState((this.horizontalSize.toInt()*scale).toInt(),widthMeasureSpec,0)
            val  height = resolveSizeAndState((this.verticalSize.toInt()*scale).toInt(),widthMeasureSpec,0)
            refreshValues(true)
            setMeasuredDimension(width,height)
        }else{
            val width = resolveSizeAndState(this.horizontalSize.toInt(),widthMeasureSpec,0)
            val height = resolveSizeAndState(this.verticalSize.toInt(),heightMeasureSpec,0)
            setMeasuredDimension(width,height)
        }
    }


    override fun onDraw(canvas: Canvas) {
        val saveCount = canvas.save()
        colors.forEachIndexed { index, color ->
            if(index==0){
                canvas.translate(centerHorizontal,tickPositionVertical)
                noColorDrawable?.draw(canvas)
                canvas.translate(-centerHorizontal,-tickPositionVertical)
            }else{
                paint.color = color
                canvas.drawCircle(centerHorizontal,tickPositionVertical,tickSizeScaled,paint)
            }

            canvas.rotate(angleBetweenColors,centerHorizontal,centerVertical)
        }
        canvas.restoreToCount(saveCount)
        canvas.rotate(snapAngle,centerHorizontal,centerVertical)
        canvas.translate(centerHorizontal,centerVertical)
        dialDrawable?.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        dragStartX = event.x
        dragStartY = event.y

        if(event.action == ACTION_DOWN || event.action == ACTION_MOVE){
            isDragging = true

            if(hasNewSnapAngle(dragStartX,dragStartY)){
                broadcastColorChange()
                invalidate()
            }
        }else if (event.action == ACTION_UP){
            isDragging = false
        }
        return true
    }
    //convert x,y into cartesian coordinates
    //convert into angles
    //get nearest angle
    //find color
    //redraw

    private fun convertToCartesianX(x:Float):Float{
        return x-horizontalSize/2
    }

    private fun convertToCartesianY(y:Float):Float{
        return (verticalSize-y)-verticalSize/2
    }

    private fun convertCartesianToPolar(x:Float,y:Float):Float{
        val angle =  Math.toDegrees((Math.atan2(y.toDouble(),
                x.toDouble()))).toFloat()
        return when (angle) {
            in 0 .. 180 -> angle
            in -180 .. 0 -> angle + 360
            else -> angle
        }
    }

    private fun hasNewSnapAngle(x:Float, y:Float):Boolean{
        val cartesianX = convertToCartesianX(x)
        val cartesianY = convertToCartesianY(y)

        val dragAngle = convertCartesianToPolar(cartesianX,cartesianY)

        val nearestAngle = getNearestAngle(dragAngle)
        val nearestPosition = getPositionFromSnappedAngle(nearestAngle)
        val newAngle = nearestPosition * angleBetweenColors
        var shouldUpdate = false
        if(newAngle!=snapAngle){
            shouldUpdate = true
            selectedPosition = nearestPosition
        }
        snapAngle = newAngle
        return shouldUpdate
    }

    private fun getNearestAngle(dragAngle:Float):Float{
        var adjustedAngle = (360 - dragAngle) + 90
        while (adjustedAngle > 360) adjustedAngle -= 360
        return adjustedAngle
    }

    private fun getPositionFromSnappedAngle(nearestAngle:Float):Int{
        return (nearestAngle/angleBetweenColors).roundToInt()
    }

    private fun refreshValues(withScale:Boolean){
        val localScale = if(withScale)scale else 1f

        totalBottomPadding = (paddingBottom+extraPadding*localScale)
        totalLeftPadding = (paddingLeft+extraPadding*localScale)
        totalRightPadding = (paddingRight+extraPadding*localScale)
        totalTopPadding = (paddingTop+extraPadding*localScale)

        tickPositionVertical = paddingTop+extraPadding*localScale/2f


        horizontalSize = paddingLeft+paddingRight+(extraPadding*localScale*2)+dialDiameter* localScale
        verticalSize = paddingTop+paddingBottom+(extraPadding*localScale*2)+dialDiameter* localScale

        centerHorizontal = totalLeftPadding +(horizontalSize-totalLeftPadding-totalRightPadding)/2
        centerVertical = totalTopPadding + (verticalSize-totalTopPadding-totalBottomPadding)/2

        angleBetweenColors = 360f/colors.size

        tickSizeScaled = tickSize * localScale
    }

    private fun toDP(value:Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                value.toFloat(),context.resources.displayMetrics).toInt()
    }

    private fun getCenteredBound(size:Int, scalar:Float=1f):Rect{
        val half = ((if(size>0)size/2 else 1)*scalar).toInt()
        return Rect(-half,-half,half,half)
    }
}
