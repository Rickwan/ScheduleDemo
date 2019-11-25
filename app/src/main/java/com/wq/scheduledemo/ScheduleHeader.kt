package com.wq.scheduledemo

import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.abs

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

    private var schedules: MutableList<ScheduleBean>? = null

    private var mTouchSlop: Int = 0

    private var onScheduleClickListener: OnScheduleClickListener? = null


    private var downY: Float = 0f


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
            drawScheduleItem(it)
            drawSchedulePoolItem(it)


        }

    }

    private fun init() {

        val configuration = ViewConfiguration.get(context)
        mTouchSlop = configuration.scaledTouchSlop
        textPaint = TextPaint()
        textPaint.color = leftLabelColor
        textPaint.textAlign = Paint.Align.LEFT
        textPaint.isAntiAlias = true

        itemPaint = Paint()
        itemPaint.color = bgLineColor
        itemPaint.isAntiAlias = true
        itemPaint.strokeWidth = 1f
        itemPaint.style = Paint.Style.FILL


    }

    fun setOnScheduleClickListener(onScheduleClickListener: OnScheduleClickListener) {

        this.onScheduleClickListener = onScheduleClickListener
    }

    fun setData(schedules: MutableList<ScheduleBean>) {

        this.schedules = schedules
        invalidate()
    }


    /**
     * 绘制背景图
     */
    private fun drawBackground(canvas: Canvas) {

        itemPaint.color = Color.WHITE
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), itemPaint)

        var paint = Paint()
        paint.color = Color.parseColor("#f2f2f2")
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        canvas.drawLine(0f, height.toFloat(), width.toFloat(), height.toFloat(), paint)

        textPaint.color = leftLabelColor

        var label = "上午12时"
        var rect = Rect()
        textPaint.textSize = 26f//为了保持左间距与日程值相同
        textPaint.getTextBounds(label, 0, label.length, rect)
        offsetLeftPadding = rect.width() + padding * 2
        textPaint.textSize = 30f

        var textStartY = height / 2 + rect.height() / 2
        canvas.drawText("全天", padding, textStartY.toFloat(), textPaint)

    }

    /**
     * 绘制日程item
     */
    private fun drawScheduleItem(canvas: Canvas) {

        if (schedules.isNullOrEmpty()) {
            return
        }

        var scheduleItem = schedules!![0]

        var itemWidth = if (schedules!!.size > 1) {
            (width - offsetLeftPadding - padding * 3) / 4 * 3
        } else {
            offsetLeftPadding
        }
        var left = offsetLeftPadding
        var top = 20f
        var bottom = height - 20f
        var right = left + itemWidth
        var title = scheduleItem.title


//        itemPaint.color = bgLineColor
//        canvas.drawRoundRect(left, top, right, bottom, 12f, 10f, itemPaint)
//        itemPaint.color = if (scheduleItem.type == 0) validItemColor else invalidItemColor
//        canvas.drawRoundRect(left, top, left + 40, bottom, 12f, 10f, itemPaint)
//        itemPaint.color = bgLineColor
//        canvas.drawRect(left + 10, top, left + 30, bottom, itemPaint)
//        itemPaint.color = Color.WHITE
//        canvas.drawRect(left + 10, top + 1, left + 30, bottom - 1, itemPaint)
//        canvas.drawRoundRect(left + 20, top + 1, right - 1, bottom - 1, 12f, 10f, itemPaint)


        drawItem(
            canvas,
            left,
            top,
            right,
            bottom,
            if (scheduleItem.type == 0) validItemColor else invalidItemColor
        )
        textPaint.textSize = 30f
        textPaint.color = textColor

        var rect = Rect()
        textPaint.getTextBounds(title, 0, title.length, rect)

        var buidler =
            StaticLayout.Builder.obtain(title, 0, title.length, textPaint, itemWidth.toInt() - 40)

        buidler.setAlignment(Layout.Alignment.ALIGN_NORMAL)
        buidler.setMaxLines(1)
        buidler.setEllipsize(TextUtils.TruncateAt.END)

        var offset = height / 2 - rect.height() / 2 - rect.height()

        canvas.translate(left + 25, top + offset)
        buidler.build().draw(canvas)
        canvas.translate(-(left + 25), -top - offset)
    }

    /**
     * 绘制汇总类日程item
     */
    private fun drawSchedulePoolItem(canvas: Canvas) {

        var size = schedules?.size ?: 0

        if (size < 2) {
            return
        }

        var title = "+${size - 1}"
        var itemWidth = (width - offsetLeftPadding - padding * 3) / 4
        var left = itemWidth * 3 + offsetLeftPadding + padding
        var right = width.toFloat() - padding
        var top = 20f
        var bottom = height - 20f

//        itemPaint.color = bgLineColor
//        canvas.drawRoundRect(left, top, right, bottom, 12f, 10f, itemPaint)
//        itemPaint.color = validItemColor
//        canvas.drawRoundRect(left, top, left + 40, bottom, 12f, 10f, itemPaint)
//        itemPaint.color = bgLineColor
//        canvas.drawRect(left + 10, top, left + 30, bottom, itemPaint)
//        itemPaint.color = Color.WHITE
//        canvas.drawRect(left + 10, top + 1, left + 30, bottom - 1, itemPaint)
//        canvas.drawRoundRect(left + 20, top + 1, right - 1, bottom - 1, 12f, 10f, itemPaint)

        drawItem(canvas, left, top, right, bottom, validItemColor)

        textPaint.textSize = 50f
        textPaint.color = validItemColor

        var rect = Rect()
        textPaint.getTextBounds(title, 0, title.length, rect)
        var offset = height / 2 - rect.height() / 2 - rect.height()

        var buidler =
            StaticLayout.Builder.obtain(title, 0, title.length, textPaint, itemWidth.toInt() - 20)
        buidler.setAlignment(Layout.Alignment.ALIGN_NORMAL)
        buidler.setMaxLines(1)
        buidler.setEllipsize(TextUtils.TruncateAt.END)
        canvas.translate(left + 25, top + offset)
        buidler.build().draw(canvas)
        canvas.translate(-(left + 25), -top - offset)
    }


    private fun drawItem(
        canvas: Canvas,
        left: Float,
        top: Float,
        right: Float,
        bottom: Float,
        headerColor: Int
    ) {

        itemPaint.strokeWidth = 1f
        itemPaint.color = headerColor
        itemPaint.style = Paint.Style.FILL_AND_STROKE

        var radii = 9f
        var headerPath = Path()
        headerPath.addRoundRect(
            left, top, left + 10f, bottom,
            floatArrayOf(radii, radii, 0f, 0f, 0f, 0f, radii, radii),
            Path.Direction.CCW
        )

        canvas.drawPath(headerPath, itemPaint)

        itemPaint.color = bgLineColor
        itemPaint.style = Paint.Style.STROKE
        var strokePath = Path()
        strokePath.addRoundRect(
            left + 10f, top, right, bottom,
            floatArrayOf(0f, 0f, radii, radii, radii, radii, 0f, 0f),
            Path.Direction.CCW
        )
        canvas.drawPath(strokePath, itemPaint)

        itemPaint.color = Color.WHITE
        itemPaint.style = Paint.Style.FILL_AND_STROKE
        var contentPath = Path()
        contentPath.addRoundRect(
            left + 10f, top + 1f, right - 1f, bottom - 1f,
            floatArrayOf(0f, 0f, radii, radii, radii, radii, 0f, 0f),
            Path.Direction.CCW
        )
        canvas.drawPath(contentPath, itemPaint)
    }

    private fun getScheduleIdByClick(x: Float, y: Float) {

        if (schedules.isNullOrEmpty()) {
            return
        }

        if (isClickSchedulePool(x, y)) {
            onScheduleClickListener?.onMoreItemClicked()
        }

        var scheduleBean = isClickSchedule(x, y)
        if (scheduleBean != null) {
            onScheduleClickListener?.onItemClicked(scheduleBean)
        }
    }


    private fun isClickSchedulePool(x: Float, y: Float): Boolean {

        var itemWidth = (width - offsetLeftPadding - padding * 3) / 4
        var left = itemWidth * 3 + offsetLeftPadding + padding
        var right = width.toFloat() - padding
        var top = 20f
        var bottom = height - 20f

        return x in left..right && y in top..bottom

    }

    private fun isClickSchedule(x: Float, y: Float): ScheduleBean? {
        var scheduleItem = schedules!![0]

        var itemWidth = if (schedules!!.size > 1) {
            (width - offsetLeftPadding - padding * 3) / 4 * 3
        } else {
            (width - offsetLeftPadding - padding * 3)
        }
        var left = offsetLeftPadding
        var top = 20f
        var bottom = height - 20f
        var right = left + itemWidth

        return if (x in left..right && y in top..bottom) {

            scheduleItem
        } else {
            null
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downY = event.y
                return true
            }

            MotionEvent.ACTION_UP -> {
                var x = event.x
                var y = event.y

                if (abs(y - downY) < mTouchSlop) {
                    getScheduleIdByClick(x, y)
                }
            }
        }

        return super.onTouchEvent(event)
    }
}