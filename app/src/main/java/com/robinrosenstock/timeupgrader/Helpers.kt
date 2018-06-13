package com.robinrosenstock.timeupgrader

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast


class UndoTaskDelete(var task_list : RecyclerView, var task : TaskContent.TaskItem, var index : Int) : View.OnClickListener {

    override fun onClick(v: View) {

        TaskContent.TASKS.add(index, task)
        task_list.adapter.notifyItemInserted(index)
        Toast.makeText(v.context, "Undelete!", Toast.LENGTH_LONG).show()
        // Code to undo the user's last action
    }
}


class UndoTimeDelete(var task_list : RecyclerView, var task : TaskContent.TaskItem, var interval : TaskContent.IntervalItem, var index : Int) : View.OnClickListener {

    override fun onClick(v: View) {

        task.interval_list.add(index, interval)
        task_list.adapter.notifyItemInserted(index)
        Toast.makeText(v.context, "Undelete!", Toast.LENGTH_LONG).show()
        // Code to undo the user's last action
    }
}