package com.robinrosenstock.timeupgrader

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.robinrosenstock.timeupgrader.dummy.TaskContent
import org.joda.time.*
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
        var task_name = ""
        var interval_list: MutableList<TaskContent.IntervalItem> = ArrayList()
        var begin_time : DateTime? = null
        var end_time : DateTime? = null

        while (line != null) {

            //                search for a non blank entry
            if(line.isBlank()) {
                while (line.isBlank()) {
                    line = it.readLine()
                    task_name = line
                }
            }else{
                task_name = line
            }
            line = it.readLine()
            // the next line can be blank, then there is no begin_time and start over with setting new_task = "",
            // otherwise there is a begin_time then go on

            if (task_name.isNotEmpty()) {

                if (line.isBlank()) {

//                there is no begin_time and no end_time, so no interval_list:
                    begin_time = null
                    end_time = null
                    interval_list = ArrayList()

                    addTask(task_name, begin_time, end_time, interval_list)
//
                    task_name = ""
                } else {
                    try {
                       begin_time = time_entry_format.parseDateTime(line)
                    } catch (e: Exception) {
                        Log.e("ABORT", "False Time format")
                    }
                    line = it.readLine()
                }

            }


            if (task_name.isNotEmpty()) {

//            there might be no end_time, then start over with setting new_task = ""
                if (line.isBlank()) {

                    end_time = null

                    interval_list = ArrayList()
                    interval_list.add(TaskContent.IntervalItem(begin_time, end_time))
                    addTask(task_name, begin_time, end_time, interval_list)

                    task_name = ""
                } else {
                    try {
                        end_time = time_entry_format.parseDateTime(line)
                    } catch (e: Exception) {
                        Log.e("ABORT", "False Time format")
                    }

                    interval_list = ArrayList()
                    interval_list.add(TaskContent.IntervalItem(begin_time, end_time))
                    addTask(task_name, begin_time, end_time, interval_list)

                }
            }

            line = it.readLine()
        }
}
}









fun addTask(new_task : String, begin_time : DateTime?, end_time : DateTime?,  interval_list: MutableList<TaskContent.IntervalItem>){

    val task_entry = TaskContent.TaskItem(new_task, new_task, interval_list)

    if (!task_already_defined(new_task)){
// define task normally:
        TaskContent.TASKS.add(task_entry)
        TaskContent.TASK_MAP.put(task_entry.id, task_entry)
    }
    else{
//                        else add another intervalItem to the interval_list of this task_entry
//                        first get the task
        val test = TaskContent.TASK_MAP[new_task]
//                        then add a new intervallItem
        test?.interval_list?.add(TaskContent.IntervalItem(begin_time, end_time))

//                        test?.details = test?.details + "\n" + only_time_format.print(begin_time) + "\n" + only_time_format.print(end_time)
//                        test?.details?.replace("alt", "new")
//                        val indexi = TaskContent.TASKS.indexOf(TaskContent.TASK_MAP[new_task])
//                        Log.e("indexi: ", test?.details.toString())
        //                        TaskContent.TASK_MAP.put(task_entry.id, task_entry)
    }

}






fun readFile2(filename: String) {

    File(Environment.getExternalStoragePublicDirectory("/time"), filename).bufferedReader().use {
        var line = it.readLine()
        val time_entry_format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        val only_time_format = DateTimeFormat.forPattern("HH:mm:ss")
        var new_task = ""
        var begin_time = DateTime()

        while (line != null) {
            if (line.isNotBlank()) {
                if (new_task.isNotEmpty()){
//

                    try {
                        begin_time = time_entry_format.parseDateTime(line)
                    }catch (e : Exception){
                        Log.e("ABORT", "ABORT")
                    }



//                    this is not really correct:
                    var end_time = DateTime()

                    line = it.readLine()
                    while (line.isBlank()) {
                        line = it.readLine()
                    }

//                    here an exception must be catched!
                    try {
                        end_time = time_entry_format.parseDateTime(line)
                    }catch (e : Exception){
                        Log.e("ABORT", "ABORT")
                    }

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



                    line = it.readLine()
                    while (line.isBlank()) {
                        line = it.readLine()
                    }

//                    here an exception must be catched
//                    java.lang.ExceptionInInitializerError


//                    begin_time = time_entry_format.parseDateTime(line)
//                    Log.e("begin_time second ", line.toString())




                    new_task = ""

                }
                else{
                    new_task = line
//                    Log.e("task: ", line.toString())

                    try {
                        begin_time = time_entry_format.parseDateTime(line)
                        Log.e("ABORT", "First a task must be named")
                    }catch (e : Exception){

                    }

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


