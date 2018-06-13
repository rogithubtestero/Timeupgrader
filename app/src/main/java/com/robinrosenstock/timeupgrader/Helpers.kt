package com.robinrosenstock.timeupgrader

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast


class MyUndoListener(var task_list : RecyclerView, var task : TaskContent.TaskItem, var index : Int) : View.OnClickListener {

    override fun onClick(v: View) {

        TaskContent.TASKS.add(index, task)
        task_list.adapter.notifyItemInserted(index)
        Toast.makeText(v.context, "Undelete!", Toast.LENGTH_LONG).show()
        // Code to undo the user's last action
    }
}