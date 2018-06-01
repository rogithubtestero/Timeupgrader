package com.robinrosenstock.timeupgrader.dummy

import com.robinrosenstock.timeupgrader.parseFile
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.format.DateTimeFormat

import java.util.HashMap
import kotlin.collections.ArrayList

/**
 * Helper class for providing sample content for user interfaces created by
 * Androides, see aapt output above for details.
	at com.android.build.gradle.internal.res.LinkApplica template wizards.
 *
 */
object TaskContent {

    /**
     * An array of sample (dummy) items.
     */
    val TASKS: MutableList<TaskItem> = ArrayList()


    /**
     * A map of sample (dummy) items, by ID.
     */
    val TASK_MAP: MutableMap<String, TaskItem> = HashMap()


    init {

//here take the last opened file uri!!!! from sharedprefs? for parseFile

        parseFile("time.txt")

//        addItem(TaskItem("1", "string1", "details1"))
//        addItem(TaskItem("2", "string2", "details2"))
//        addItem(TaskItem("3", "string3", "details3"))
    }



//    private val COUNT = 25
//
//    init {
//        // Add some sample items.
//        for (i in 1..COUNT) {
//            addItem(createTaskItem(i))
//        }
//    }

    private fun addItem(item: TaskItem) {
        TASKS.add(item)
        TASK_MAP.put(item.id!!, item)
    }


    private fun deleteItem(item: TaskItem) {
        TASKS.remove(item)
        TASK_MAP.remove(item.id)
    }



//    private fun createTaskItem(position: Int): TaskItem {
//        return TaskItem(position.toString(), "Item " + position, makeDetails(position))
//    }

//    private fun makeDetails(position: Int): String {
//        val builder = StringBuilder()
//        builder.append("Details about Item: ").append(position)
//        for (i in 0..position - 1) {
//            builder.append("\nMore details information here.")
//        }
//        return builder.toString()
//    }


    class IntervalItem(var begin_time: DateTime?, var end_time: DateTime?, var begin_time_number: Int?, var end_time_number : Int?){


        val time_entry_format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        val only_time_format = DateTimeFormat.forPattern("HH:mm:ss")


        fun getInterval() : Interval{
           return Interval(begin_time, end_time)
        }

//        fun isOngoing(): Boolean{
////            if (end_time != null) true else false
//            val mu = getInterval().end
//            mu
//        }


        fun getTimeFormatted(timeinstant: DateTime?) : String? {

            //            here the formatter must come in!
            return timeinstant?.toString(only_time_format) ?: "-->"
        }


        fun getTimeFormatted2(timeinstant: DateTime?) : String? {

            //            here the formatter must come in!
            return timeinstant?.toString(time_entry_format) ?: "-->"
        }



//        override fun toString(): String {
//            begin_time.toString()
//        }


//        override fun toString(): String {
//            return super.toString()
//        }
    }



    /**
     * A TaskItem is a item representing a task, which contains an id (maybe this is unnecassary?), an title (which is for now named content) and a list of Instants .
     */
    data class TaskItem(val id: String?, val title: String?,
                        val interval_list: MutableList<IntervalItem> = ArrayList(),
                        val task_number_list: MutableList<Int?> = ArrayList()) {
        override fun toString(): String
        {
            return title ?: "undefined"
        }
    }
}



