package com.wq.scheduledemo

import android.content.Context
import android.util.AttributeSet
import android.view.ViewConfiguration
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Scroller
import java.text.SimpleDateFormat

/**
 * @author wq
 * @date 2019-11-19 16:05
 * @desc ScheduleView
 */
class ScheduleLayout : LinearLayout {

    private lateinit var scheduleHeader: ScheduleHeader

    private lateinit var scheduleView: ScheduleView

    private lateinit var scroller: Scroller

    private var mTouchSlop: Int = 0

    private var schedules: MutableList<ScheduleBean>? = null

    private var onScheduleClickListener: OnScheduleClickListener? = null

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


        val configuration = ViewConfiguration.get(context)
        mTouchSlop = configuration.scaledTouchSlop

        scroller = Scroller(context)
        orientation = VERTICAL

        scheduleHeader = ScheduleHeader(context)
        addView(scheduleHeader)

        var scrollView = ScrollView(context)
        scrollView.isVerticalScrollBarEnabled = false
        scheduleView = ScheduleView(context)
        scrollView.addView(scheduleView)
        addView(scrollView)

    }

    fun setOnScheduleClickListener(onScheduleClickListener: OnScheduleClickListener) {

        this.onScheduleClickListener = onScheduleClickListener
        scheduleView?.setOnScheduleClickListener(onScheduleClickListener)
        scheduleHeader?.setOnScheduleClickListener(onScheduleClickListener)
    }

    fun setData(schedules: MutableList<ScheduleBean>) {

        this.schedules = schedules
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val format = SimpleDateFormat("HH.mm")


        var allDaySchedules = mutableListOf<ScheduleBean>()
        var otherSchedules = mutableListOf<ScheduleBean>()
        schedules.forEach {

            var startDate = format.format(sdf.parse(it.startDate)).toFloat()
            var endDate = format.format(sdf.parse(it.endDate)).toFloat()
            if (endDate - startDate >= 23.59) {
                allDaySchedules.add(it)
            } else {
                otherSchedules.add(it)
            }

        }

        scheduleView.setData(otherSchedules)
        scheduleHeader.setData(allDaySchedules)
        invalidate()
    }


    private fun smoothScrollTo(deltaY: Int) {

        scroller.startScroll(scrollX, scrollY, 0, deltaY, 0)
        invalidate()

    }

    override fun computeScroll() {
        if (scroller.computeScrollOffset()) {
            scheduleView.scrollTo(scroller.currX, scroller.currY)
            postInvalidate()
        }
    }

}