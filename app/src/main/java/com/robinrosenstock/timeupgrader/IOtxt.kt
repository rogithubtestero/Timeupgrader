package com.robinrosenstock.timeupgrader

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.robinrosenstock.timeupgrader.dummy.TaskContent
import org.joda.time.*
import org.joda.time.format.DateTimeFormat
import java.io.*



fun import_todo_txt(context: Context, filename: Uri?) {

    val input = context.getContentResolver().openInputStream(filename)
    input.bufferedReader().use {
        var line = it.readLine()
        while (line != null) {
            if (line.isNotBlank()) {
                var task_number_list: MutableList<Int?> = ArrayList()
                val neger = TaskContent.TaskItem("222", line, ArrayList(), 1)
                TaskContent.TASKS.add(neger)
                TaskContent.TASK_MAP.put(neger.id.toString(), neger)
            }
            line = it.readLine()
        }
    }
}



fun searchOngoingIntervalItem(task: TaskContent.TaskItem): TaskContent.IntervalItem?{

    task.interval_list.forEach {

        if (it.end_time == null) {
            return it
        }
    }
    return null
}



fun addIntervalItemToFile(filename: String, intervalItem: TaskContent.IntervalItem, on_line: Int?) {

    val timefile = File(Environment.getExternalStoragePublicDirectory("/time"), "time.txt")
    val tmpFilename = File(Environment.getExternalStoragePublicDirectory("/time"), "tmp.txt")
    val tmpFile = timefile.copyTo(tmpFilename, true, DEFAULT_BUFFER_SIZE)

    val reader = LineNumberReader(tmpFile.bufferedReader())
    val writer = timefile.bufferedWriter()

    var line = reader.readLine()

    writer.use {

        while (line != null) {

            if (reader.lineNumber == on_line) {
                it.write(line)
                it.newLine()
                it.newLine()
                it.write(intervalItem.getTimeFormatted2(DateTime.now()))
                it.newLine()
                it.write("-->")
                it.newLine()
                line = reader.readLine()
            } else {
                it.write(line)
                it.newLine()
                line = reader.readLine()
            }
        }

    }

}


fun replaceLineInFile(filename: String, linenumber: Int?, text: String?){

    val timefile = File(Environment.getExternalStoragePublicDirectory("/time"), "time.txt")
    val tmpFilename = File(Environment.getExternalStoragePublicDirectory("/time"), "tmp.txt")
    val tmpFile = timefile.copyTo(tmpFilename,true, DEFAULT_BUFFER_SIZE)

    val reader = LineNumberReader(tmpFile.bufferedReader())
    val writer = timefile.bufferedWriter()

    var line = reader.readLine()

    writer.use {

        while (line != null) {

            if (reader.lineNumber == linenumber){
                it.write(text)
                it.newLine()
                line = reader.readLine()
            }
            else {
                it.write(line)
                it.newLine()
                line = reader.readLine()
            }
        }

    }

}


fun parseFile(filename: String): Int? {

    var end_line_number : Int? = null

    val reader = LineNumberReader(File(Environment.getExternalStoragePublicDirectory("/time"), filename).bufferedReader())
    reader.use {

        var line = it.readLine()
        var task_name: String
        var task_id : String = "0"
        var begin_time: DateTime? = null
        var end_time: DateTime? = null
        var task_line_number : Int
        var begin_time_number : Int
        var end_time_number : Int
        var interval_list: MutableList<TaskContent.IntervalItem>
        var parsedTask : TaskContent.TaskItem
        val time_entry_format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        val task_name_regex = Regex("^(#+ +)(?<name>.*)")
        val time_entry_regex = Regex("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\$")


        taskLoop@ while (line != null) {


            while (line.isBlank()) {
                line = it.readLine()
            }


            if (task_name_regex.matches(line)) {

                task_line_number = it.lineNumber
                task_name = task_name_regex.matchEntire(line)!!.groups[2]!!.value
                if(task_name.isBlank()){
                    line = it.readLine()
                    continue@taskLoop
                }
                task_id = task_id.toInt().plus(1).toString()
                interval_list = ArrayList()


                line = it.readLine()

                if (line == null){
//                    abort
                }


                timeLoop@ while (line != null) {

                while (line.isBlank()) {
                    line = it.readLine()
                }


                    when {
                        time_entry_regex.matches(line) -> {
//
//                            we have begin_time!
                            begin_time = time_entry_format.parseDateTime(line)
                            begin_time_number = it.lineNumber

                            line = it.readLine()

                            when{
                                time_entry_regex.matches(line) -> {

//                                    we have begin_time & end_time!
                                    end_time = time_entry_format.parseDateTime(line)
                                    end_time_number = it.lineNumber
                                    interval_list.add(TaskContent.IntervalItem(begin_time, end_time, begin_time_number, end_time_number))

//                                    there might be additional times continue time Loop:
                                    line = it.readLine()
                                    continue@timeLoop
                                }

                                line.matches(Regex("-->")) -> {

//                                  we have begin_time and end time is null
                                    end_time = null
                                    end_time_number = it.lineNumber
                                    interval_list.add(TaskContent.IntervalItem(begin_time, end_time, begin_time_number, end_time_number))

                                    //                                    there might be additional times continue time Loop:
                                    line = it.readLine()
                                    continue@timeLoop
                                }

                                else -> {
//                                    we do not have a valid end_time, only begin time
                                    print("abort")

                                    line = it.readLine()
                                    continue@timeLoop
                                }
                            }

                        }//valid begin_time

                        line.matches(Regex("<--")) -> {

                            begin_time = null
                            begin_time_number = it.lineNumber

                            line = it.readLine()

                            when{
                                time_entry_regex.matches(line) -> {

//                                    we have begin_time & end_time!
                                    end_time = time_entry_format.parseDateTime(line)
                                    end_time_number = it.lineNumber
                                    interval_list.add(TaskContent.IntervalItem(begin_time, end_time, begin_time_number, end_time_number))

//                                    there might be additional times continue time Loop:
                                    line = it.readLine()
                                    continue@timeLoop
                                }

                                line.matches(Regex("-->")) -> {

//                                  we have begin_time and end time is null
                                    end_time = null
                                    end_time_number = it.lineNumber
                                    interval_list.add(TaskContent.IntervalItem(begin_time, end_time, begin_time_number, end_time_number))

                                    //                                    there might be additional times continue time Loop:
                                    line = it.readLine()
                                    continue@timeLoop
                                }

                                else -> {
//                                    we do not have a valid end_time, only begin time
                                    print("abort")

                                    line = it.readLine()
                                    continue@timeLoop
                                }
                            }


                        }//empty begin_time


                        task_name_regex.matches(line) -> {
//                            we have a new task!
//                            now save the old task and then
//                            go out of the time loop to the taskLoop@ again!
                            addParsedTask(task_id, task_name, interval_list, task_line_number)
                            continue@taskLoop
                        }

                            else -> {

                                print("abort")
                                addParsedTask(task_id, task_name, interval_list, task_line_number)
                                line = it.readLine()
                                continue@taskLoop
                            }
                    }


                    }//timeLoop@

//                We have a valid task name, but no times or invalid times
                addParsedTask(task_id, task_name, interval_list, task_line_number)


            } // if task_name_regex.matches
            else{
//                we do not have a valid task_name
//                ignore and try on the next line
                line = it.readLine()
            }

            }//taskLoop@

            }


    return end_line_number
        }




fun addParsedTask(task_id: String,
                  task_name : String,
                  interval_list: MutableList<TaskContent.IntervalItem>,
                  task_line_number : Int) {

    val parsedTask = TaskContent.TaskItem(task_id, task_name, interval_list, task_line_number)
    TaskContent.TASKS.add(parsedTask)
    TaskContent.TASK_MAP.put(parsedTask.id, parsedTask)
}



fun isTaskAlreadyDefined(task_entry : String?) : Boolean {
    TaskContent.TASKS.forEach {
        if (task_entry!!.equals(it.id)){
            return true
        }
    }
    return false
}


