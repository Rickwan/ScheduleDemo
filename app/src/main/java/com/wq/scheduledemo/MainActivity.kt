package com.wq.scheduledemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var beans = mutableListOf<ScheduleBean>()
        beans.add(ScheduleBean("0","日程0日程0日程0日程0",0,"2019/11/12 12:00:00","2019/11/12 12:10:00"))
        beans.add(ScheduleBean("1","日程1日阿道夫程日程日程日程日程日程",0,"2019/11/12 13:00:00","2019/11/12 18:10:00"))
        beans.add(ScheduleBean("2","日程2日挨打程日程日程日程",0,"2019/11/12 13:00:00","2019/11/12 17:50:00"))
        beans.add(ScheduleBean("3","日程3日程日程日程",1,"2019/11/12 13:00:00","2019/11/12 16:50:00"))
        beans.add(ScheduleBean("4","日程4日程日程",0,"2019/11/12 13:00:00","2019/11/12 13:10:00"))
        beans.add(ScheduleBean("5","日程5日程日程日程",0,"2019/11/12 15:00:00","2019/11/12 15:10:00"))
//        beans.add(ScheduleBean("6","日程6",0,"2019/11/12 13:00:00","2019/11/12 15:10:00"))
        beans.add(ScheduleBean("7","日程6日程日程日程日程",0,"2019/11/12 16:00:00","2019/11/12 16:10:00"))
//        beans.add(ScheduleBean("8","日程8",0,"2019/11/12 14:00:00","2019/11/12 16:10:00"))
        beans.add(ScheduleBean("9","日程9日程日程日程日程",0,"2019/11/12 12:00:00","2019/11/12 12:10:00"))


        scheduleLt.setData(beans)
    }
}
