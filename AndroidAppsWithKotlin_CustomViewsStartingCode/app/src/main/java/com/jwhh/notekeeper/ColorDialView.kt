package com.jwhh.notekeeper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * TODO: document your custom view class.
 */
class ColorDialView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
    : View(context, attrs, defStyleAttr, defStyleRes) {

    private val colors:ArrayList<Int> = arrayListOf(Color.TRANSPARENT, Color.RED, Color.YELLOW, Color.BLUE,
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

    private var tickPositionVertical =0f
    private var paint = Paint().also {
        it.color = Color.RED
        it.isAntiAlias = true
    }

    init {
        dialDrawable = context.getDrawable(R.drawable.ic_dial).also {
            it?.bounds = getCenteredBound(dialDiameter)
            it?.setTint(Color.DKGRAY)
        }

        noColorDrawable= context.getDrawable(R.drawable.ic_clear_black).also {
            it?.bounds = getCenteredBound(tickSize.toInt(),2f)
        }

        refreshValues()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.translate(centerHorizontal,centerVertical)
        dialDrawable?.draw(canvas)
        canvas.translate(-centerHorizontal,-centerVertical)
        colors.forEachIndexed { index, color ->
            if(index==0){
                canvas.translate(centerHorizontal,tickPositionVertical)
                noColorDrawable?.draw(canvas)
                canvas.translate(-centerHorizontal,-tickPositionVertical)
            }else{
                paint.color = color
                canvas.drawCircle(centerHorizontal,tickPositionVertical,tickSize,paint)
            }

            canvas.rotate(angleBetweenColors,centerHorizontal,centerVertical)
        }
    }

    private fun refreshValues(){
        totalBottomPadding = (paddingBottom+extraPadding).toFloat()
        totalLeftPadding = (paddingLeft+extraPadding).toFloat()
        totalRightPadding = (paddingRight+extraPadding).toFloat()
        totalTopPadding = (paddingTop+extraPadding).toFloat()

        tickPositionVertical = paddingTop+extraPadding/2f


        horizontalSize = paddingLeft+paddingRight+(extraPadding*2)+dialDiameter.toFloat()
        verticalSize = paddingTop+paddingBottom+(extraPadding*2)+dialDiameter.toFloat()

        centerHorizontal = totalLeftPadding +(horizontalSize-totalLeftPadding-totalRightPadding)/2
        centerVertical = totalTopPadding + (verticalSize-totalTopPadding-totalBottomPadding)/2

        angleBetweenColors = 360f/colors.size
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
