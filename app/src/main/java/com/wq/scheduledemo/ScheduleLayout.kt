package com.wq.scheduledemo

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * @author wq
 * @date 2019-11-19 16:05
 * @desc ScheduleView
 */
class ScheduleLayout : FrameLayout {


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

    private lateinit var scheduleView: ScheduleView


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


        scheduleView = ScheduleView(context)
        addView(scheduleView)

    }

    fun setData(schedules: MutableList<ScheduleBean>) {
        scheduleView.setData(schedules)
        invalidate()
    }



}