package com.robinrosenstock.timeupgrader

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.robinrosenstock.timeupgrader.dummy.TaskContent
import org.joda.time.*
import java.io.File
import org.joda.time.format.DateTimeFormat
import java.io.BufferedReader
import java.io.LineNumberReader


//fun writeFile(filename: String){
//
//    ////// save  entry/line to the file //////////
//    val time_entry_format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
//    File(Environment.getExternalStoragePublicDirectory("/time"), filename).appendText("\n" + "texttobeappended")
//}


fun import_todo_txt(context: Context, filename: Uri?) {

    val input = context.getContentResolver().openInputStream(filename)
    input.bufferedReader().use {
        var line = it.readLine()
        while (line != null) {
            if (line.isNotBlank()) {
                var task_number_list: MutableList<Int?> = ArrayList()
                val neger = TaskContent.TaskItem("222", line, ArrayList(), task_number_list)
                TaskContent.TASKS.add(neger)
                TaskContent.TASK_MAP.put(neger.id!!, neger)
            }
            line = it.readLine()
        }
    }
}


fun readFileAsWhole(filename: String): String {

    val reader = LineNumberReader(File(Environment.getExternalStoragePublicDirectory("/time"), filename).bufferedReader())
    val inputString = reader.use {it.readText()}
    return inputString
}


//fun readFileAsList(filename: String): {
//
//    val reader = LineNumberReader(File(Environment.getExternalStoragePublicDirectory("/time"), filename).bufferedReader())
//    val inputString = reader.use {it.readText()}
//    return inputString
//}


fun parseFile(filename: String): Int? {

    var end_line_number : Int? = null

    val reader = LineNumberReader(File(Environment.getExternalStoragePublicDirectory("/time"), filename).bufferedReader())
    reader.use {

        //    File(Environment.getExternalStoragePublicDirectory("/time"), filename).bufferedReader().use {
        var line = it.readLine()
        val time_entry_format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        var task_name: String? = null
        var task_line_number : Int? = null
        var interval_list: MutableList<TaskContent.IntervalItem> = ArrayList()
        var begin_time: DateTime? = null
        var begin_time_number : Int? = null
        var end_time: DateTime? = null
        var end_time_number : Int? = null
        val task_name_regex = Regex("^#+(?!#)(.*)")
        val time_entry_regex = Regex("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\$")
        var from_time_loop = false


        taskLoop@ while (line != null) {

            while (line.isBlank()) {
                line = it.readLine()
            }

            if (task_name_regex.matches(line)) {

                task_name = line
                task_line_number = it.lineNumber
                begin_time = null
                end_time = null
                interval_list = ArrayList()

                line = it.readLine()

                timeLoop@ while (line != null) {

                    while (line.isBlank()) {
                        line = it.readLine()
                    }

                    if (time_entry_regex.matches(line)) {


                        from_time_loop = true
                        begin_time = time_entry_format.parseDateTime(line)
                        begin_time_number = it.lineNumber

                        line = it.readLine()

                        when {
                            line.isBlank() || line.matches(Regex("-->"))  -> {

//                                end_time is still null, but there is an begin_time
                                interval_list.add(TaskContent.IntervalItem(begin_time, end_time, begin_time_number, end_time_number))
                                addTimeToTask(task_name, begin_time, end_time, interval_list, task_line_number, begin_time_number, end_time_number)

                                // there might be additional begin_time & end_time pairs (or only begin_times) so continue this timeloop
                                line = it.readLine()

                                continue@timeLoop
                            }

                            time_entry_regex.matches(line) -> {

//                                there is an begin_time and an end_time
                                end_time = time_entry_format.parseDateTime(line)
                                end_time_number = it.lineNumber
                                interval_list.add(TaskContent.IntervalItem(begin_time, end_time, begin_time_number, end_time_number))
                                addTimeToTask(task_name, begin_time, end_time, interval_list, task_line_number, begin_time_number, end_time_number)

//                                there might be additional begin_time & end_time pairs (or only begin_times) so continue this timeloop
                                //                                so reset the interval_list
                                interval_list = ArrayList()
                                line = it.readLine()

                                continue@timeLoop
                            }
                            else -> {
                                Log.e("ABORT", "time entry is not correct")

                                line = it.readLine()
                                continue@taskLoop
                            }
                        }

                    } else {
                        Log.e("ABORT", "time entry not correct")

//                        begin_time and end_time are null and interval_list is empty

//                        if coming from the time loop, then the task is already defined
                        if (!from_time_loop) {
                            addTimeToTask(task_name, begin_time, end_time, interval_list, task_line_number, begin_time_number, end_time_number)
                        }

                        from_time_loop = false
                        continue@taskLoop
                    }
                }
            }
            line = it.readLine()
        }
        end_line_number = it.lineNumber
    }

    return end_line_number

}





fun addTimeToTask(task_name : String?, begin_time : DateTime?, end_time : DateTime?, interval_list: MutableList<TaskContent.IntervalItem>,
                  task_line_number : Int?, begin_time_number: Int?, end_time_number: Int?){

    if (isTaskAlreadyDefined(task_name)){
        // first get the task
        val task = TaskContent.TASK_MAP[task_name]

        // then add a new intervallItem to its interval_list
        task?.interval_list?.add(TaskContent.IntervalItem(begin_time, end_time, begin_time_number, end_time_number))
    }
    else{
        // define task first:
        var task_number_list: MutableList<Int?> = ArrayList()
        task_number_list.add(task_line_number)
        val task_entry = TaskContent.TaskItem(task_name, task_name, interval_list, task_number_list)
        TaskContent.TASKS.add(task_entry)
        TaskContent.TASK_MAP.put(task_entry.id!!, task_entry)
    }

}



fun isTaskAlreadyDefined(task_entry : String?) : Boolean {
    TaskContent.TASKS.forEach {
        if (task_entry.equals(it.id)){
            return true
        }
    }
    return false
}


