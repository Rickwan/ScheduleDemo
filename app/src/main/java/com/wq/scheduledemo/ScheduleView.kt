package com.wq.scheduledemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round

/**
 * @author wq
 * @date 2019-11-19 16:05
 * @desc ScheduleView
 */
class ScheduleView : View {


    /**
     * 每分钟的刻度高度
     */
    private var itemHeight: Int = 2

    /**
     * 左右上下间距
     */
    private var padding: Float = 20f

    /**
     * 左侧文字颜色
     */
    private var leftLabelColor = Color.parseColor("#AEB5C2")

    /**
     * 日程文字颜色
     */
    private var textColor = Color.parseColor("#1D1D1D")

    /**
     * 背景线颜色
     */
    private var bgLineColor = Color.parseColor("#cccccc")

    /**
     * 当前时间线颜色
     */
    private var baseLineColor = Color.parseColor("#FF6A06")

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


    private lateinit var textPaint: TextPaint

    private lateinit var linePaint: Paint

    private lateinit var itemPaint: Paint

    private lateinit var schedulePools: MutableList<SchedulePool>

    private lateinit var scheduleItems: MutableList<ScheduleItem>

    private var schedules: MutableList<ScheduleBean>? = null


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

        schedulePools = mutableListOf()
        scheduleItems = mutableListOf()

        linePaint = Paint()
        linePaint.color = bgLineColor
        linePaint.isAntiAlias = true

        textPaint = TextPaint()
        textPaint.textSize = 26f
        textPaint.color = leftLabelColor
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.isAntiAlias = true

        itemPaint = Paint()
        itemPaint.color = bgLineColor
        itemPaint.isAntiAlias = true
        itemPaint.strokeWidth = 1f
        itemPaint.style = Paint.Style.FILL

        setBackgroundColor(Color.WHITE)

    }


    fun setData(schedules: MutableList<ScheduleBean>) {

        this.schedules = schedules

        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val format = SimpleDateFormat("HH.mm")



        this.schedules?.sortWith(Comparator { o1, o2 ->


            try {
                var startTime1 = format.format(sdf.parse(o1.startDate)).toFloat()
                var endTime1 = format.format(sdf.parse(o1.endDate)).toFloat()

                var startTime2 = format.format(sdf.parse(o2.startDate)).toFloat()
                var endTime2 = format.format(sdf.parse(o2.endDate)).toFloat()

                var dateInterval = (endTime2 - startTime2) - (endTime1 - startTime1)





                when {
                    dateInterval > 0 -> 1
                    dateInterval < 0 -> -1
                    else -> 0
                }

            } catch (e: Exception) {
                0
            }

        })

        this.schedules?.forEach {
            var startPosition = format.format(sdf.parse(it.startDate)).toFloat()
            var endPosition = format.format(sdf.parse(it.endDate)).toFloat()

            if (endPosition - startPosition < 0.5) {
                endPosition = startPosition + 0.5f
            }
            it.startPosition = startPosition
            it.endPosition = endPosition

        }

        invalidate()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        var heightSpec = MeasureSpec.makeMeasureSpec(
            itemHeight * 25 * 60 + 2 * padding.toInt(),
            MeasureSpec.EXACTLY
        )
        super.onMeasure(widthMeasureSpec, heightSpec)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {


            drawBackground(it)
            calculateScheduleItem()
            drawSchedule(it)
            drawSchedulePool(it)
            drawCurrentBaseLine(it)
        }

    }


    /**
     * 将排序后的原始对象转换为绘制需要的值
     */
    private fun calculateScheduleItem() {

        schedulePools.clear()

        schedules?.forEach {

            try {

                var offsetCount = 0
                var title = if (it.type == 0) it.title else "已占用"
                var startPosition = it.startPosition
                var endPosition = it.endPosition

                var currentPosition = schedules!!.indexOf(it)
                if (currentPosition > 0) {

                    for (index in 0..currentPosition) {
                        var lastScheduleBean = schedules!![index]

                        if (lastScheduleBean.id == it.id) {
                            continue
                        }
                        var lastStartPosition =
                            lastScheduleBean.startPosition
                        var lastEndPosition =
                            lastScheduleBean.endPosition

                        if ((startPosition in lastStartPosition..lastEndPosition) || (endPosition in lastStartPosition..lastEndPosition)) {

                            offsetCount++

                        }
                    }

                }

                if (offsetCount > 2) {

                    var createPool = true// 1 add,2 update
                    schedulePools.iterator().forEach { it1 ->

                        if (!(endPosition < it1.startPosition || startPosition > it1.endPosition)) {

                            if (startPosition < it1.startPosition) {
                                it1.startPosition = startPosition
                            }

                            if (endPosition > it1.endPosition) {
                                it1.endPosition = endPosition
                            }
                            it1.count += 1
                            createPool = false
                        }

                    }

                    if (createPool || schedulePools.isEmpty()) {


                        //起始位置不足0.5的向下取整，超过0.5的向上取整
                        if (round(startPosition) - startPosition != 0.5f) {
                            startPosition = round(startPosition)
                        }

                        //结束位置不足0.5取0.5，超过0.5向上取整
                        if (endPosition - floor(endPosition) > 0.5) {
                            endPosition = ceil(endPosition)
                        } else if (endPosition - floor(endPosition) < 0.5) {
                            endPosition = floor(endPosition) + 0.5f
                        }

                        if (endPosition - startPosition < 1) {
                            endPosition = startPosition + 1f
                        }

                        var schedulePool = SchedulePool(1, startPosition, endPosition)
                        schedulePools.add(schedulePool)
                    }


                } else {


                    var width = (width - offsetLeftPadding - padding * 3) / 4
                    var left = if (offsetCount > 0) {
                        offsetLeftPadding + (width + 10) * offsetCount
                    } else {
                        offsetLeftPadding
                    }
                    var top = itemHeight * 60 * startPosition + 10
                    var bottom =
                        if (endPosition - floor(endPosition) != 0f) {
                            itemHeight * 60 * endPosition + 10
                        } else {
                            itemHeight * 60 * endPosition - 10
                        }

                    var right = left + width

                    var scheduleItem =
                        ScheduleItem(
                            it.id,
                            title,
                            it.type,
                            offsetCount,
                            left,
                            top,
                            right,
                            bottom,
                            width
                        )
                    scheduleItems.add(scheduleItem)
                }


            } catch (exception: Exception) {

            }

        }

    }


    /**
     * 绘制背景图
     */
    private fun drawBackground(canvas: Canvas) {

        canvas.translate(0f, 20f)
        linePaint.color = bgLineColor
        textPaint.color = leftLabelColor

        var rect = Rect()
        textPaint.getTextBounds("上午12时", 0, 5, rect)
        offsetLeftPadding = rect.width() + padding * 2

        var labels = context.resources.getStringArray(R.array.schedule)

        for ((position, label) in labels.withIndex()) {

            var startY = itemHeight * 60 * position.toFloat()
            var textStartY = startY + rect.height() / 2 - 2

            canvas.drawText(label, padding, textStartY, textPaint)
            canvas.drawLine(
                offsetLeftPadding,
                startY,
                width.toFloat() - padding,
                startY,
                linePaint
            )

            if (position == labels.size - 1) {

                var startY = itemHeight * 60 * (position + 1).toFloat()

                canvas.drawLine(
                    rect.width() + padding * 2,
                    startY,
                    width.toFloat() - padding,
                    startY,
                    linePaint
                )
            }


        }


    }


    private fun drawCurrentBaseLine(canvas: Canvas) {


        linePaint.color = baseLineColor

        textPaint.textSize = 26f
        textPaint.color = baseLineColor

        var calendar = Calendar.getInstance()
        var hour = calendar.get(Calendar.HOUR)
        var minute = calendar.get(Calendar.MINUTE)

        var isAm = calendar.get(Calendar.AM_PM) == Calendar.AM

        var label = if (isAm) {
            "上午$hour:${if (minute < 10) "0$minute" else minute}"
        } else {
            "下午$hour:${if (minute < 10) "0$minute" else minute}"
        }

        var startY =
            if (isAm) {
                itemHeight * hour * 60.toFloat() + minute * itemHeight
            } else {
                itemHeight * (hour + 12) * 60.toFloat() + minute * itemHeight
            }

        var rect = Rect()
        textPaint.getTextBounds(label, 0, label.length, rect)


        canvas.drawText(label, padding, startY + rect.height() / 2 - 2, textPaint)
        canvas.drawCircle(offsetLeftPadding, startY, 8f, linePaint)
        canvas.drawLine(
            offsetLeftPadding,
            startY,
            width.toFloat() - padding,
            startY,
            linePaint
        )

    }

    /**
     * 绘制日程
     */
    private fun drawSchedule(canvas: Canvas) {

        scheduleItems.forEach {
            drawScheduleItem(canvas, it)
        }

    }

    /**
     * 绘制日程item
     */
    private fun drawScheduleItem(canvas: Canvas, scheduleItem: ScheduleItem) {

        var left = scheduleItem.left
        var top = scheduleItem.top
        var right = scheduleItem.right
        var bottom = scheduleItem.bottom
        var width = scheduleItem.width
        var title = scheduleItem.title

        itemPaint.color = bgLineColor
        canvas.drawRoundRect(left, top, right, bottom, 12f, 10f, itemPaint)
        itemPaint.color = if (scheduleItem.type == 0) validItemColor else invalidItemColor
        canvas.drawRoundRect(left, top, left + 40, bottom, 12f, 10f, itemPaint)
        itemPaint.color = bgLineColor
        canvas.drawRect(left + 10, top, left + 30, bottom, itemPaint)
        itemPaint.color = Color.WHITE
        canvas.drawRect(left + 10, top + 1, left + 30, bottom - 1, itemPaint)
        canvas.drawRoundRect(left + 20, top + 1, right - 1, bottom - 1, 12f, 10f, itemPaint)

        textPaint.textSize = 30f
        textPaint.color = textColor

        var rect = Rect()
        textPaint.getTextBounds(title, 0, title.length, rect)

        var count = (bottom - top - 42) / rect.height()
        if (count < 1) {
            count = 1f
        }

        var buidler =
            StaticLayout.Builder.obtain(title, 0, title.length, textPaint, width.toInt() - 40)

        buidler.setAlignment(Layout.Alignment.ALIGN_NORMAL)
        buidler.setMaxLines(count.toInt())
        buidler.setEllipsize(TextUtils.TruncateAt.END)
        canvas.translate(left + 25, top + 10)
        buidler.build().draw(canvas)
        canvas.translate(-(left + 25), -top - 10)
    }

    /**
     * 绘制汇总类日程
     */
    private fun drawSchedulePool(canvas: Canvas) {


        schedulePools.forEach {

            drawSchedulePoolItem(canvas, "+${it.count}", it.startPosition, it.endPosition)
        }

    }

    /**
     * 绘制汇总类日程item
     */
    private fun drawSchedulePoolItem(
        canvas: Canvas,
        title: String,
        startPosition: Float,
        endPosition: Float
    ) {

        var offsetCount = 3

        var startPosition = startPosition
        var endPosition = endPosition

        var width = (width - offsetLeftPadding - padding * 3) / 4
        var left = if (offsetCount > 0) {
            offsetLeftPadding + (width + 10) * offsetCount
        } else {
            offsetLeftPadding
        }
        var top = itemHeight * 60 * startPosition + 10
        var bottom =
            if (endPosition - floor(endPosition) != 0f) {
                itemHeight * 60 * endPosition + 10
            } else {
                itemHeight * 60 * endPosition - 10
            }

        var right = left + width
        itemPaint.color = bgLineColor
        canvas.drawRoundRect(left, top, right, bottom, 12f, 10f, itemPaint)
        itemPaint.color = validItemColor
        canvas.drawRoundRect(left, top, left + 40, bottom, 12f, 10f, itemPaint)
        itemPaint.color = bgLineColor
        canvas.drawRect(left + 10, top, left + 30, bottom, itemPaint)
        itemPaint.color = Color.WHITE
        canvas.drawRect(left + 10, top + 1, left + 30, bottom - 1, itemPaint)
        canvas.drawRoundRect(left + 20, top + 1, right - 1, bottom - 1, 12f, 10f, itemPaint)

        textPaint.textSize = 50f
        textPaint.color = validItemColor

        var rect = Rect()
        textPaint.getTextBounds(title, 0, title.length, rect)

        var count = (bottom - top - 42) / rect.height()
        if (count < 1) {
            count = 1f
        }

        var buidler =
            StaticLayout.Builder.obtain(title, 0, title.length, textPaint, width.toInt() - 20)

        buidler.setAlignment(Layout.Alignment.ALIGN_NORMAL)
        buidler.setMaxLines(count.toInt())
        buidler.setEllipsize(TextUtils.TruncateAt.END)
        canvas.translate(left + 25, top + 10)
        buidler.build().draw(canvas)
        canvas.translate(-(left + 25), -top - 10)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {

            MotionEvent.ACTION_DOWN -> {

            }

            MotionEvent.ACTION_MOVE -> {

            }

            MotionEvent.ACTION_UP -> {

            }
        }

        return super.onTouchEvent(event)
    }


}