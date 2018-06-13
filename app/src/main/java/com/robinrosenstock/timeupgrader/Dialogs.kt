package com.robinrosenstock.timeupgrader

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import kotlinx.android.synthetic.main.task_list_recyclerview.*
import kotlinx.android.synthetic.main.time_list_recyclerview.*
import org.joda.time.DateTime
import java.util.*


fun addTaskDialog(context : Context) {

    val dialogBuilder = AlertDialog.Builder(context)

    var name : String
    val edittext = EditText(context)

    dialogBuilder.setView(edittext)
    dialogBuilder.setTitle("Add a new task")
    dialogBuilder.setNegativeButton("Cancel", { _: DialogInterface, _: Int ->
        //            do nothing
    })
    dialogBuilder.setPositiveButton("Add!", { _: DialogInterface, _: Int ->
        name = edittext.text.toString()
        val new_index = TaskContent.TASKS.lastIndex+1
        val new_task = TaskContent.TaskItem(1234, name, ArrayList(),456, false)
        TaskContent.TASKS.add(new_index, new_task)

        (context as Activity).task_list.adapter.notifyItemInserted(new_index)
    })

    val alertDialog = dialogBuilder.create()
    alertDialog.show()
}



fun addTimeDialog(context : Context) {

    val dialogBuilder = AlertDialog.Builder(context)

    var name : String
    val edittext = EditText(context)

    val button_set_begin_time = Button(context)
    val button_set_end_time = Button(context)

//    dialogBuilder.setView(edittext)
//    dialogBuilder.setView(button_set_begin_time)
//    dialogBuilder.setView(button_set_end_time)

    dialogBuilder.setTitle("Title")
    dialogBuilder.setItems(arrayOf<CharSequence>("button 1", "button 2", "button 3", "button 4")
    ) { dialog, which ->
        // The 'which' argument contains the index position
        // of the selected item
        when (which) {
            0 -> Toast.makeText(context, "clicked 1", Toast.LENGTH_SHORT).show()
            1 -> Toast.makeText(context, "clicked 2", Toast.LENGTH_SHORT).show()
            2 -> Toast.makeText(context, "clicked 3", Toast.LENGTH_SHORT).show()
            3 -> Toast.makeText(context, "clicked 4", Toast.LENGTH_SHORT).show()
        }
    }


    dialogBuilder.setTitle("Add a new time")
    dialogBuilder.setNegativeButton("Cancel", { _: DialogInterface, _: Int ->
        //            do nothing
    })
    dialogBuilder.setPositiveButton("Add!", { _: DialogInterface, _: Int ->

        name = edittext.text.toString()
        val new_index = TaskContent.TASKS.lastIndex+1
        val new_task = TaskContent.TaskItem(1234, name, ArrayList(),456, false)
        TaskContent.TASKS.add(new_index, new_task)

        (context as Activity).time_list.adapter.notifyItemInserted(new_index)
    })


    button_set_begin_time.setOnClickListener {
        Toast.makeText(context,"not yet implemented", Toast.LENGTH_LONG).show()
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
        TaskContent.TASKS[task.pos].title = new_name
        (context as Activity).task_list.adapter.notifyItemChanged(index)
    })
    val alertDialog = dialogBuilder.create()
    alertDialog.show()
}
