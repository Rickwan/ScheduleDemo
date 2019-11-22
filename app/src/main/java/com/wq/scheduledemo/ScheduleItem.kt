package com.wq.scheduledemo

/**
 * @author wq
 * @date 2019-11-22 09:35
 * @desc ScheduleBean
 */
data class ScheduleItem(
    var id: String,
    var title: String,
    var type: Int,
    var row: Int,
    var left: Float,
    var top: Float,
    var right: Float,
    var bottom: Float,
    var width: Float
)