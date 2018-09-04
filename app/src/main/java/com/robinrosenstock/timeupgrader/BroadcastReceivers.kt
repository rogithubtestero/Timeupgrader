package com.robinrosenstock.timeupgrader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.task_list_recyclerview.*
import org.joda.time.DateTime
import kotlinx.android.synthetic.main.task_list.*
import android.support.v7.widget.RecyclerView
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.ContextCompat.startActivity
import android.content.ComponentName
import android.app.ActivityManager
import android.os.Build
import android.support.v4.content.LocalBroadcastManager


//class ToggleBroadcastReceiver : BroadcastReceiver() {
//
//    override fun onReceive(context: Context, intent: Intent) {
//        val message = intent.getStringExtra("toastMessage")
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//    }
//}


class StopBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val taskbundle = intent.getBundleExtra("taskbundle")
        var tasktitle = taskbundle.getString("tasktitle")
        var taskindex = taskbundle.getInt("taskindex")


        TaskContent.TASKS[taskindex].ongoing = false
        TaskContent.TASKS[taskindex].interval_list[0].end_time = DateTime.now()
        dismissNotification(context,taskindex)

        writeFile(TaskContent.TASKS)

//        TaskContent.TASKS.removeAll(TaskContent.TASKS)
//        readFile("time.txt")
//        task_list.adapter.notifyDataSetChanged()

    }


}


