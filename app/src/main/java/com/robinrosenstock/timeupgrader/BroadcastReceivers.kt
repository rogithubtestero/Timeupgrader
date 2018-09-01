package com.robinrosenstock.timeupgrader

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast


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

        writeFile(TaskContent.TASKS)

//see here: https://stackoverflow.com/questions/47989313/how-can-i-update-recyclerview-with-broadcast-receiver-from-service-class

        Toast.makeText(context, tasktitle, Toast.LENGTH_SHORT).show()
//        Toast.makeText(context, taskindex, Toast.LENGTH_LONG).show()
    }
}