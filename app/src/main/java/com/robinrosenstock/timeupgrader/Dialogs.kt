package com.robinrosenstock.timeupgrader

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.SystemClock
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.widget.EditText
import android.widget.Toast
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import kotlinx.android.synthetic.main.alert_dialog.view.*
import kotlinx.android.synthetic.main.task_list_recyclerview.*
import kotlinx.android.synthetic.main.time_list_recyclerview.*
import org.jetbrains.anko.layoutInflater
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.*




fun killDialog(context: Context){


    val dialog = AlertDialog.Builder(context)
//    dialog.setTitle("Ciao")
    dialog.setMessage("You have no trust in me? \n" +
            "So neither I will in you.")
    val alert = dialog.create()
    alert.show()

// Hide after some seconds
    val handler = Handler()
    val runnable = Runnable {
        if (alert.isShowing) {
            alert.dismiss()
        }
    }

    alert.setOnDismissListener {
        handler.removeCallbacks(runnable)
        System.exit(0)

    }
    handler.postDelayed(runnable, 3000)
}



fun addTaskDialog(context : Context) {

    val dialogBuilder = AlertDialog.Builder(context)

    var name : String
    val edittext = EditText(context)

    dialogBuilder.setView(edittext)
    dialogBuilder.setTitle("Add a new task")
    dialogBuilder.setNegativeButton("Cancel") { _: DialogInterface, _: Int -> }
    dialogBuilder.setPositiveButton("Add!") { _: DialogInterface, _: Int ->
        name = edittext.text.toString()
        val new_task = TaskContent.TaskItem(name, ArrayList(), false)
        // add the task on the top (index: 0)
        TaskContent.TASKS.add(0, new_task)
        (context as Activity).task_list.adapter.notifyItemInserted(0)
    }

    val alertDialog = dialogBuilder.create()
    alertDialog.show()
}



fun addTimeDialog(context : Context, clicked_task_id : Int) {

    val dialogBuilder = AlertDialog.Builder(context)

    var name : String
    var test : String = "leer"
    var begin_time : DateTime? = null
    var end_time : DateTime? = null

    val view = context.layoutInflater.inflate(R.layout.alert_dialog, null)
    dialogBuilder.setView(view)


    dialogBuilder.setTitle("Add a new time")
    dialogBuilder.setNegativeButton("Cancel", { _: DialogInterface, _: Int ->
        //            do nothing
    })
    dialogBuilder.setPositiveButton("Add!", { _: DialogInterface, _: Int ->


//        take text and build a interval item:

        val new_index = TaskContent.TASKS[clicked_task_id].interval_list.lastIndex+1
        val new_time = TaskContent.IntervalItem(begin_time, end_time)
        TaskContent.TASKS[clicked_task_id].interval_list.add(0, new_time)
        (context as Activity).time_list.adapter.notifyItemInserted(new_index)

    })

    view.button2.setOnClickListener {
//        Toast.makeText(context,"no22222222222222222", Toast.LENGTH_LONG).show()


        val dateTimeDialogFragment2 = SwitchDateTimeDialogFragment.newInstance("End time", "OK", "Cancel", "Set Null"
        )
        dateTimeDialogFragment2.startAtTimeView()
        dateTimeDialogFragment2.set24HoursMode(true)
//        dateTimeDialogFragment2.setDefaultDateTime(clicked_interval.end_time?.toDate())


        dateTimeDialogFragment2.setOnButtonClickListener(
                object : SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener{

                    override fun onPositiveButtonClick(date: Date)
                    {
                        val time_entry_format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
                        begin_time = DateTime(date)
                        val info = begin_time?.toString(time_entry_format)
                        view.textView.text = info
                    }

                    override fun onNegativeButtonClick(date: Date)
                    {

//                    cancel
                    }

                    override fun onNeutralButtonClick(date: Date)
                    {
                        //                    set end time null
//                        Toast.makeText(context,"not yet implemented", Toast.LENGTH_LONG).show()
                        begin_time = null
                        view.textView.text = "<--"
                    }
                })

        dateTimeDialogFragment2.show((context as FragmentActivity).supportFragmentManager, "dialog_time")
    }

    view.button3.setOnClickListener {

        val dateTimeDialogFragment2 = SwitchDateTimeDialogFragment.newInstance("End time", "OK", "Cancel", "Set Null"
        )
        dateTimeDialogFragment2.startAtTimeView()
        dateTimeDialogFragment2.set24HoursMode(true)
//        dateTimeDialogFragment2.setDefaultDateTime(clicked_interval.end_time?.toDate())


        dateTimeDialogFragment2.setOnButtonClickListener(
                object : SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener{

                    override fun onPositiveButtonClick(date: Date)
                    {
                        val time_entry_format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
                        end_time = DateTime(date)
                        val info = end_time?.toString(time_entry_format)
                        view.textView2.text = info
                    }

                    override fun onNegativeButtonClick(date: Date)
                    {

//                    cancel
                    }

                    override fun onNeutralButtonClick(date: Date)
                    {
                        //                    set end time null
//                        Toast.makeText(context,"not yet implemented", Toast.LENGTH_LONG).show()
                        end_time = null
                        view.textView2.text = "-->"
                    }
                })

        dateTimeDialogFragment2.show((context as FragmentActivity).supportFragmentManager, "dialog_time")

    }


    val alertDialog = dialogBuilder.create()
    alertDialog.show()
}




fun renameTaskDialog(task : TaskContent.TaskItem, context: Context) {

    val index = TaskContent.TASKS.indexOf(task)
    val dialogBuilder = AlertDialog.Builder(context)
    var new_name : String
    val edittext = EditText(context)

    dialogBuilder.setView(edittext)
    edittext.setText(task.title)
    dialogBuilder.setTitle("Rename Task")
    dialogBuilder.setNegativeButton("Cancel", { _: DialogInterface, _: Int ->
        //do nothing
    })
    dialogBuilder.setPositiveButton("Rename!", { _: DialogInterface, _: Int ->
        new_name = edittext.text.toString()
        task.title = new_name
        (context as Activity).task_list.adapter.notifyItemChanged(index)
    })
    val alertDialog = dialogBuilder.create()
    alertDialog.show()
}
