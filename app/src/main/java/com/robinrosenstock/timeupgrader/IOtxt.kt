package com.robinrosenstock.timeupgrader

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.robinrosenstock.timeupgrader.dummy.DummyContent
import org.joda.time.*
import org.joda.time.format.*
import java.io.File
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import org.joda.time.format.DateTimeFormat




fun writeFile(filename: String){

    ////// save  entry/line to the file //////////
    File(Environment.getExternalStoragePublicDirectory("/time"), filename).bufferedWriter().use { out ->
        DummyContent.ITEMS.forEach {
            val date = Regex("2018-05-23")
            if (date.matches(it.content)){
//                Log.e("out",it.content)

            }
//            Log.d("oh","stingsauer")
//            Log.e("puff",it.content)
            out.write(it.content + "\n")
        }
    }
}



fun readFile(context: Context, filename: Uri?) {

    val input = context.getContentResolver().openInputStream(filename)
    input.bufferedReader().use {
        var line = it.readLine()
        while (line != null) {
            if (line.isNotBlank()) {
                val neger = DummyContent.DummyItem("222", line, "details will be filled later")
                DummyContent.ITEMS.add(neger)
                DummyContent.ITEM_MAP.put(neger.id, neger)
            }
            line = it.readLine()
        }
    }
}


fun isValidDatum(test : String): Boolean {
    try {
        ISODateTimeFormat.date().parseDateTime(test)
        return true
    } catch (e: IllegalArgumentException) {
        return false
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

                    val task_entry = DummyContent.DummyItem(new_task, new_task, only_time_format.print(begin_time) + "\n" + only_time_format.print(end_time))

                    if (!task_already_defined(new_task)){
//                        define task normally:
                        DummyContent.ITEMS.add(task_entry)
                        DummyContent.ITEM_MAP.put(task_entry.id, task_entry)
                    }
                    else{
//                        else add the details to the already defined task
                        val test = DummyContent.ITEM_MAP[new_task]

                        test?.details = test?.details + "\n" + only_time_format.print(begin_time) + "\n" + only_time_format.print(end_time)
//                        test?.details?.replace("alt", "new")
                        val indexi = DummyContent.ITEMS.indexOf(DummyContent.ITEM_MAP[new_task])
//                        Log.e("indexi: ", test?.details.toString())
                        //                        DummyContent.ITEM_MAP.put(task_entry.id, task_entry)
                    }


                    new_task = ""

                }
                else{
                    new_task = line
                    Log.e("task: ", line.toString())

                }


            }
            line = it.readLine()
        }
        }
    }

fun task_already_defined(task_entry : String) : Boolean {
    DummyContent.ITEMS.forEach {
        if (task_entry.equals(it.id)){
            return true
        }
        }
    return false
}


//
//    val input = context.getContentResolver().openInputStream()
//    input.bufferedReader().use {
//        var line = it.readLine()
//
//        while (line != null) {
//            if (line.isNotBlank()) {
//                val neger = DummyContent.DummyItem("222", line, "details will be filled later")
//                DummyContent.ITEMS.add(neger)
//            }
//            line = it.readLine()
//        }
//    }
//}






fun readFile2(filename: String) {



    File(Environment.getExternalStoragePublicDirectory("/time"), filename).bufferedReader().use {
        var line = it.readLine()
        var new_date = false
        val date_iso_format = ISODateTimeFormat.date()
        var date_line : LocalDate = LocalDate()

        while (line != null) {
            if (line.isNotBlank()) {


                if(isValidDatum(line)){
                    new_date = true
                    date_line = LocalDate(line)
                    Log.e("date_line: ", date_line.toString())
                }


                if(new_date){
//                    create a new catergory_date from the date
                    new_date = false
                }
                else {
//                    /////// evalute the coressponding task to the date

                    val splitet_line = line.split(",")



                    val begin_time = LocalTime(splitet_line[0].trim())
                    val end_time = LocalTime(splitet_line[1].trim())
                    val title_content = splitet_line[2]
//                    Log.e("begin_time: ", begin_time.toString())
//                    Log.e("end_time: ", end_time.toString())

//                    begin_time.toDateTimeToday()


                    val formatter = DateTimeFormat.forPattern("HH:mm:ss")
                    val dt = formatter.parseDateTime(splitet_line[0].trim())
//                    Log.e("begin_time: ", dt.toString())
//
//
                    val nigger1 = date_line.toLocalDateTime(begin_time).toDateTime()
                    val nigger2 = date_line.toLocalDateTime(end_time).toDateTime()

                    val uiae = Interval(nigger1, nigger2)
                    Log.e("Interval: ", uiae.toString())
                    val dura = uiae.toDuration().standardSeconds
                    Log.e("Duration: ", dura.toString())


//                    if (task_regex.matches(line)){
//
//                    }

                    val neger = DummyContent.DummyItem("222", line, "details will be filled later")
                    DummyContent.ITEMS.add(neger)
                    DummyContent.ITEM_MAP.put(neger.id, neger)

                }



            }
            line = it.readLine()
        }
    }
}