package com.robinrosenstock.timeupgrader

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.robinrosenstock.timeupgrader.dummy.TaskContent
import org.joda.time.*
import org.joda.time.format.*
import java.io.File
import org.joda.time.format.DateTimeFormat



fun writeFile(filename: String){

    ////// save  entry/line to the file //////////
    val time_entry_format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

    File(Environment.getExternalStoragePublicDirectory("/time"), filename).appendText("\n" + "texttobeappended")
}


fun import_todo_txt(context: Context, filename: Uri?) {

    val input = context.getContentResolver().openInputStream(filename)
    input.bufferedReader().use {
        var line = it.readLine()
        while (line != null) {
            if (line.isNotBlank()) {
                val neger = TaskContent.TaskItem("222", line, ArrayList())
                TaskContent.TASKS.add(neger)
                TaskContent.TASK_MAP.put(neger.id, neger)
            }
            line = it.readLine()
        }
    }
}



fun readFile(filename: String) {

    File(Environment.getExternalStoragePublicDirectory("/time"), filename).bufferedReader().use {
        var line = it.readLine()
        val time_entry_format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        val only_time_format = DateTimeFormat.forPattern("HH:mm:ss")
        var new_task = ""

        while (line != null) {
            if (line.isNotBlank()) {

                if (new_task.isNotEmpty()){
//
//  evaluate the times (two lines) for this task

                    val begin_time = time_entry_format.parseDateTime(line)
                    line = it.readLine()
                    val end_time = time_entry_format.parseDateTime(line)

                    val interval_list: MutableList<TaskContent.IntervalItem> = ArrayList()
                    interval_list.add(TaskContent.IntervalItem(begin_time, end_time))

                    val task_entry = TaskContent.TaskItem(new_task, new_task, interval_list)

                    if (!task_already_defined(new_task)){
//                        define task normally:
                        TaskContent.TASKS.add(task_entry)
                        TaskContent.TASK_MAP.put(task_entry.id, task_entry)
                    }
                    else{
//                        else add another intervalItem to the interval_list of this task_entry
//                        first get the task
                        val test = TaskContent.TASK_MAP[new_task]
//                        then add a new intervallItem
                        test?.interval_list?.add(TaskContent.IntervalItem(begin_time, end_time))
//                        Log.e("indexi: ", test?.details.toString())


//                        test?.details = test?.details + "\n" + only_time_format.print(begin_time) + "\n" + only_time_format.print(end_time)
//                        test?.details?.replace("alt", "new")
//                        val indexi = TaskContent.TASKS.indexOf(TaskContent.TASK_MAP[new_task])
//                        Log.e("indexi: ", test?.details.toString())
                        //                        TaskContent.TASK_MAP.put(task_entry.id, task_entry)
                    }

                    new_task = ""

                }
                else{
                    new_task = line
//                    Log.e("task: ", line.toString())

                }


            }
            line = it.readLine()
        }
        }
    }

fun task_already_defined(task_entry : String) : Boolean {
    TaskContent.TASKS.forEach {
        if (task_entry.equals(it.id)){
            return true
        }
    }
    return false
}


