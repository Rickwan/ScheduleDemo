package com.wq.scheduledemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View



/**
 * @author wq
 * @date 2019-11-19 16:05
 * @desc ScheduleView
 */
class RoundView : View {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightSpec = MeasureSpec.makeMeasureSpec(
            120,
            MeasureSpec.EXACTLY
        )
        super.onMeasure(widthMeasureSpec, heightSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {

            var paint = Paint()
            paint.strokeWidth = 2f
            paint.isAntiAlias = true
            paint.color = Color.RED
            paint.style = Paint.Style.FILL_AND_STROKE

            var path = Path()
            path.addRoundRect(
                10f, 0f, 20f, 100f,
                floatArrayOf(15f, 15f, 0f, 0f, 0f, 0f, 15f, 15f),
                Path.Direction.CCW
            )

            it.drawPath(path,paint)

            paint.color=Color.parseColor("#cccccc")
            paint.style = Paint.Style.STROKE
            var path2 = Path()
            path2.addRoundRect(
                20f, 0f, 200f, 100f,
                floatArrayOf(0f, 0f, 15f, 15f, 15f, 15f, 0f, 0f),
                Path.Direction.CCW
            )
            it.drawPath(path2,paint)

            paint.color = Color.WHITE
            paint.style = Paint.Style.FILL_AND_STROKE
            var path1 = Path()
            path1.addRoundRect(
                20f, 1f, 199f, 99f,
                floatArrayOf(0f, 0f, 15f, 15f, 15f, 15f, 0f, 0f),
                Path.Direction.CCW
            )
            it.drawPath(path1,paint)


        }

    }


}