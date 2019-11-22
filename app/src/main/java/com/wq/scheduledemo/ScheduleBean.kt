package com.wq.scheduledemo

/**
 * @author wq
 * @date 2019-11-22 09:35
 * @desc ScheduleBean
 */
data class ScheduleBean(
    var id: String,
    var title: String,
    var type: Int,
    var startDate: String,
    var endDate: String,
    var startPosition: Float = 0f,
    var endPosition: Float = 0f
)