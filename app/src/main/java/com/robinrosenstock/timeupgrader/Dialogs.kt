package com.robinrosenstock.timeupgrader

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.widget.EditText
import kotlinx.android.synthetic.main.task_list_recyclerview.*


fun addTaskDialog(context : Context) {

    val dialogBuilder = AlertDialog.Builder(context)

    var name : String
    val edittext = EditText(context)

    dialogBuilder.setView(edittext)
    dialogBuilder.setTitle("Add a new task")
    dialogBuilder.setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int ->
        //            do nothing
    })
    dialogBuilder.setPositiveButton("Add!", { dialogInterface: DialogInterface, i: Int ->
        name = edittext.text.toString()
        val new_index = TaskContent.TASKS.lastIndex+1
        val new_task = TaskContent.TaskItem(1234, name, ArrayList(),456, false)
        TaskContent.TASKS.add(new_index, new_task)

        (context as Activity).task_list.adapter.notifyItemInserted(new_index)
    })

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
    dialogBuilder.setNegativeButton("Cancel", { dialogInterface: DialogInterface, i: Int ->
        //do nothing
    })
    dialogBuilder.setPositiveButton("Rename!", { dialogInterface: DialogInterface, i: Int ->
        new_name = edittext.text.toString()
        TaskContent.TASKS[task.pos].title = new_name
        (context as Activity).task_list.adapter.notifyItemChanged(index)
    })
    val alertDialog = dialogBuilder.create()
    alertDialog.show()
}
