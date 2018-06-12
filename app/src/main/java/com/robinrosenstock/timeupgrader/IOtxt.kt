package com.robinrosenstock.timeupgrader

import android.content.Context
import android.net.Uri
import android.os.Environment
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
                val neger = TaskContent.TaskItem(222, line, ArrayList(), 1)
                TaskContent.TASKS.add(neger)
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
                it.write(intervalItem.getBeginTimeFormatted())
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


fun writeFile(TASKS : MutableList<TaskContent.TaskItem>) {

    val timefile = File(Environment.getExternalStoragePublicDirectory("/time"), "time.txt")
//    val tmpFilename = File(Environment.getExternalStoragePublicDirectory("/time"), "tmp.txt")
//    val tmpFile = timefile.copyTo(tmpFilename, true, DEFAULT_BUFFER_SIZE)

    val writer = timefile.bufferedWriter()

    writer.use {

        TASKS.forEach { task ->
            it.write("# ${task.title}")
            it.newLine()
            it.newLine()

            task.interval_list.forEach {interval ->
                it.write(interval.getBeginTimeFormatted())
                it.newLine()
                it.write(interval.getEndTimeFormatted())
                it.newLine()
                it.newLine()
            }
            it.newLine()
        }

    }

}


//fun removeTask(task : TaskContent.TaskItem){
//
//    val timefile = File(Environment.getExternalStoragePublicDirectory("/time"), "time.txt")
//    val tmpFilename = File(Environment.getExternalStoragePublicDirectory("/time"), "tmp.txt")
//    val tmpFile = timefile.copyTo(tmpFilename,true, DEFAULT_BUFFER_SIZE)
//
//    val reader = LineNumberReader(tmpFile.bufferedReader())
//    val writer = timefile.bufferedWriter()
//
//    var line = reader.readLine()
//        writer.use {
//
//        while (line != null) {
//
//            if (reader.lineNumber == task.line_number){
//
//                    while (reader.lineNumber < TaskContent.TASKS[task.pos+1].line_number) {
//                        line = reader.readLine()
//                        if (line == null){
//                            break
//                        }
//                    }
//                }
//
//            else {
//                it.write(line)
//                it.newLine()
//                line = reader.readLine()
//            }
//        }
//
//    }
//
//}


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


fun addTaskToFile(last_time_pos : Int, name: String){


    val timefile = File(Environment.getExternalStoragePublicDirectory("/time"), "time.txt")
    val tmpFilename = File(Environment.getExternalStoragePublicDirectory("/time"), "tmp.txt")
    val tmpFile = timefile.copyTo(tmpFilename,true, DEFAULT_BUFFER_SIZE)

    val reader = LineNumberReader(tmpFile.bufferedReader())
    val writer = timefile.bufferedWriter()

    var line = reader.readLine()
    writer.use {

        while (line != null) {

            if (reader.lineNumber == last_time_pos){
                it.write(line)
                it.newLine()
                it.newLine()
                it.write("# $name")
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




//fun renameTaskonFile(task : TaskContent.TaskItem, newTaskName: String){
//
//
//    val timefile = File(Environment.getExternalStoragePublicDirectory("/time"), "time.txt")
//    val tmpFilename = File(Environment.getExternalStoragePublicDirectory("/time"), "tmp.txt")
//    val tmpFile = timefile.copyTo(tmpFilename,true, DEFAULT_BUFFER_SIZE)
//
//    val reader = LineNumberReader(tmpFile.bufferedReader())
//    val writer = timefile.bufferedWriter()
//
//    var line = reader.readLine()
//    writer.use {
//
//        while (line != null) {
//
//            if (reader.lineNumber == task.line_number){
//                it.write("# $newTaskName")
//                it.newLine()
//                line = reader.readLine()
//            }
//
//            else {
//                it.write(line)
//                it.newLine()
//                line = reader.readLine()
//            }
//        }
//    }
//}


fun reLoad(){
    TaskContent.TASKS.removeAll(TaskContent.TASKS)
    parseFile("time.txt")
}




fun parseFile(filename: String): Int {

    var end_line_number : Int = -1

    val reader = LineNumberReader(File(Environment.getExternalStoragePublicDirectory("/time"), filename).bufferedReader())
    reader.use {

        var line = it.readLine()
        var task_name: String
        var task_pos : Int = -1
        var time_pos : Int = -1
        var begin_time: DateTime? = null
        var ongoing: Boolean = false
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
                if (line == null){
                    break@taskLoop
                }
            }


            if (task_name_regex.matches(line)) {

                task_line_number = it.lineNumber
                task_name = task_name_regex.matchEntire(line)!!.groups[2]!!.value
                if(task_name.isBlank()){
                    line = it.readLine()
                    continue@taskLoop
                }
                task_pos = task_pos.plus(1)
                interval_list = ArrayList()


                line = it.readLine()


                timeLoop@ while (line != null) {

                    while (line.isBlank()) {
                        line = it.readLine()
                        if (line == null){
                            break@timeLoop
                        }
                    }


                    when {
                        time_entry_regex.matches(line) -> {
//
//                            we have begin_time!
                            begin_time = time_entry_format.parseDateTime(line)
                            begin_time_number = it.lineNumber
                            time_pos = time_pos.plus(1)

                            line = it.readLine()

                            when{
                                time_entry_regex.matches(line) -> {

//                                    we have begin_time & end_time!
                                    end_time = time_entry_format.parseDateTime(line)
                                    end_time_number = it.lineNumber
                                    interval_list.add(TaskContent.IntervalItem(time_pos, begin_time, end_time, begin_time_number, end_time_number))

//                                    there might be additional times continue time Loop:
                                    line = it.readLine()
                                    continue@timeLoop
                                }

                                line.matches(Regex("-->")) -> {

//                                  we have begin_time and end time is null -> ongoing
                                    ongoing = true
                                    end_time = null
                                    end_time_number = it.lineNumber
                                    interval_list.add(TaskContent.IntervalItem(time_pos, begin_time, end_time, begin_time_number, end_time_number))

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
                            time_pos = time_pos.plus(1)

                            line = it.readLine()

                            when{
                                time_entry_regex.matches(line) -> {

//                                    we have begin_time & end_time!
                                    end_time = time_entry_format.parseDateTime(line)
                                    end_time_number = it.lineNumber
                                    interval_list.add(TaskContent.IntervalItem(time_pos, begin_time, end_time, begin_time_number, end_time_number))

//                                    there might be additional times continue time Loop:
                                    line = it.readLine()
                                    continue@timeLoop
                                }

                                line.matches(Regex("-->")) -> {

//                                  we have begin_time and end time is null
                                    end_time = null
                                    end_time_number = it.lineNumber
                                    interval_list.add(TaskContent.IntervalItem(time_pos, begin_time, end_time, begin_time_number, end_time_number))

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
                            val parsedTask = TaskContent.TaskItem(task_pos, task_name, interval_list, task_line_number, ongoing)
                            TaskContent.TASKS.add(parsedTask)
                            ongoing = false
                            continue@taskLoop
                        }

                        else -> {

                            print("abort")
                            val parsedTask = TaskContent.TaskItem(task_pos, task_name, interval_list, task_line_number, ongoing)
                            TaskContent.TASKS.add(parsedTask)
                            ongoing = false


                            line = it.readLine()
                            continue@taskLoop
                        }
                    }


                }//timeLoop@

//                We have a valid task name, but no times or invalid times
                val parsedTask = TaskContent.TaskItem(task_pos, task_name, interval_list, task_line_number, ongoing)
                TaskContent.TASKS.add(parsedTask)
                ongoing = false


            } // if task_name_regex.matches
            else{
//                we do not have a valid task_name
//                ignore and try on the next line
                line = it.readLine()
            }

            ongoing = false
            end_line_number = it.lineNumber
        }//taskLoop@

    }


    return end_line_number
}





