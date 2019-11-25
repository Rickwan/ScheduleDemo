package com.wq.scheduledemo

/**
 * @author wq
 * @date 2019-11-25 14:13
 * @desc OnScheduleClickListener
 */
interface OnScheduleClickListener {
    fun onItemClicked(scheduleBean: ScheduleBean)
    fun onMoreItemClicked()
}