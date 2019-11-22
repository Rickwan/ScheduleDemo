package com.wq.scheduledemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View

/**
 * @author wq
 * @date 2019-11-19 16:05
 * @desc ScheduleView
 */
class ScheduleHeader : View {


    /**
     * 左侧文字颜色
     */
    private var leftLabelColor = Color.parseColor("#1D1D1D")

    /**
     * 日程文字颜色
     */
    private var textColor = Color.parseColor("#1D1D1D")

    /**
     * 背景线颜色
     */
    private var bgLineColor = Color.parseColor("#cccccc")


    /**
     * 有效项颜色
     */
    private var validItemColor = Color.parseColor("#93D4B6")

    /**
     * 无效项颜色
     */
    private var invalidItemColor = Color.parseColor("#F68C73")


    /**
     * 背景线左侧偏移量 = 左侧文字宽度+padding*2
     */
    private var offsetLeftPadding: Float = 0f

    /**
     * 左右上下间距
     */
    private var padding: Float = 20f


    private lateinit var textPaint: TextPaint


    private lateinit var itemPaint: Paint

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()

    }

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
            drawBackground(it)
        }

    }

    private fun init() {


        textPaint = TextPaint()
        textPaint.textSize = 30f
        textPaint.color = leftLabelColor
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.isAntiAlias = true

        itemPaint = Paint()
        itemPaint.color = bgLineColor
        itemPaint.isAntiAlias = true
        itemPaint.strokeWidth = 1f
        itemPaint.style = Paint.Style.FILL


    }



    /**
     * 绘制背景图
     */
    private fun drawBackground(canvas: Canvas) {

        itemPaint.color = Color.WHITE
        canvas.drawRect(0f,0f,width.toFloat(),220f,itemPaint)

        textPaint.color = leftLabelColor

        var label = "全天"
        var rect = Rect()
        textPaint.getTextBounds(label, 0, label.length, rect)
        offsetLeftPadding = rect.width() + padding * 2

        var startY = 120f
        var textStartY = startY + rect.height() / 2 - 2
        canvas.drawText(label, padding, textStartY, textPaint)



    }


}