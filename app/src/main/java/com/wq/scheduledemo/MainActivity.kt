package com.wq.scheduledemo

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var beans = mutableListOf<ScheduleBean>()
        beans.add(
            ScheduleBean(
                "0",
                "日程0日程0日程0日程0",
                0,
                "2019/11/12 12:00:00",
                "2019/11/12 12:10:00"
            )
        )
        beans.add(
            ScheduleBean(
                "1",
                "日程1日阿道夫程日程日程日程日程日程",
                0,
                "2019/11/12 13:00:00",
                "2019/11/12 18:10:00"
            )
        )
        beans.add(
            ScheduleBean(
                "2",
                "日程2日挨打程日程日程日程",
                0,
                "2019/11/12 13:00:00",
                "2019/11/12 17:50:00"
            )
        )
        beans.add(ScheduleBean("3", "日程3日程日程日程", 1, "2019/11/12 13:00:00", "2019/11/12 16:50:00"))
        beans.add(ScheduleBean("4", "日程4日程日程", 0, "2019/11/12 13:00:00", "2019/11/12 13:10:00"))
        beans.add(ScheduleBean("5", "日程5日程日程日程", 0, "2019/11/12 15:00:00", "2019/11/12 15:10:00"))
        beans.add(ScheduleBean("6", "日程6", 0, "2019/11/12 13:00:00", "2019/11/12 15:10:00"))
        beans.add(ScheduleBean("7", "日程6日程日程日程日程", 0, "2019/11/12 16:00:00", "2019/11/12 16:10:00"))
        beans.add(ScheduleBean("8", "日程8", 0, "2019/11/12 14:00:00", "2019/11/12 16:10:00"))
        beans.add(ScheduleBean("9", "日程9日程日程日程日程", 0, "2019/11/12 12:00:00", "2019/11/12 12:10:00"))
        beans.add(
            ScheduleBean(
                "10",
                "日程10日程日程日程日程日程程日程日程日程日程程日程日程日程日程程日程日程日程",
                0,
                "2019/11/12 00:00:00",
                "2019/11/12 23:59:00"
            )
        )
        beans.add(
            ScheduleBean(
                "11",
                "日程11日程日程日程日程",
                0,
                "2019/11/12 00:00:00",
                "2019/11/12 23:59:00"
            )
        )
        beans.add(ScheduleBean("12", "日程12", 0, "2019/11/12 11:00:00", "2019/11/12 12:10:00"))
        beans.add(ScheduleBean("13", "日程13", 0, "2019/11/12 00:00:00", "2019/11/12 00:10:00"))
        beans.add(ScheduleBean("14", "日程14", 0, "2019/11/12 00:00:00", "2019/11/12 00:10:00"))
        beans.add(ScheduleBean("15", "日程15", 1, "2019/11/12 00:00:00", "2019/11/12 00:10:00"))
        beans.add(ScheduleBean("16", "日程16", 0, "2019/11/12 00:00:00", "2019/11/12 00:10:00"))
        beans.add(ScheduleBean("17", "日程17", 0, "2019/11/12 00:00:00", "2019/11/12 00:10:00"))
        beans.add(ScheduleBean("18", "日程18", 0, "2019/11/12 00:00:00", "2019/11/12 00:10:00"))


        scheduleLt.setData(beans)
        scheduleLt.setOnScheduleClickListener(object : OnScheduleClickListener {
            override fun onItemClicked(scheduleBean: ScheduleBean) {
                Log.i("tag", "点击日程：${scheduleBean.id},${scheduleBean.title}")

                Toast.makeText(this@MainActivity,"点击日程：${scheduleBean.id},${scheduleBean.title}",Toast.LENGTH_SHORT).show()

            }

            override fun onMoreItemClicked() {
                Log.i("tag", "查看更多")

                Toast.makeText(this@MainActivity,"查看更多",Toast.LENGTH_SHORT).show()
            }

        })
    }
}
