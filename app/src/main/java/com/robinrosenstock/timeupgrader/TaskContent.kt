package com.robinrosenstock.timeupgrader

import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.format.DateTimeFormat
import kotlin.collections.ArrayList
import org.joda.time.format.PeriodFormatterBuilder


object TaskContent {

    val TASKS: MutableList<TaskItem> = ArrayList()

    init {

    }


    class IntervalItem(var pos: Int ,var begin_time: DateTime?, var end_time: DateTime?, var begin_time_number: Int?, var end_time_number : Int?){


        val time_entry_format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        val only_time_format = DateTimeFormat.forPattern("HH:mm:ss")
        var inter_interval = Interval(null)


        fun getBeginTimeFormatted() : String? {
            return this.begin_time?.toString(time_entry_format) ?: "<--"
        }


        fun getEndTimeFormatted() : String? {
            return this.end_time?.toString(time_entry_format) ?: "-->"
        }


        fun getFormattedDuration() : String? {

            val formatter = PeriodFormatterBuilder()
                    .appendDays()
                    .appendSuffix("d")
                    .appendHours()
                    .appendSuffix("h")
                    .appendMinutes()
                    .appendSuffix("m")
                    .appendSeconds()
                    .appendSuffix("s")
                    .toFormatter()

            if (this.begin_time != null && this.end_time != null){
               this.inter_interval = Interval(begin_time, end_time)
//                return inter_interval.toDuration().toStandardMinutes().toString()
                return formatter.print(inter_interval.toPeriod())
            }
            else{
                return ""
            }
        }


    }


    data class TaskItem(var pos: Int,
                        var title: String,
                        var interval_list: MutableList<IntervalItem>,
                        var line_number : Int,
                        var ongoing : Boolean = false) {


        override fun toString(): String
        {
//            return title ?: "undefined"
            return title ?: "undefined"
        }
    }

}



