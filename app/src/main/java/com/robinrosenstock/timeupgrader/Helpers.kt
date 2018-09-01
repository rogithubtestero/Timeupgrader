package com.robinrosenstock.timeupgrader

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import android.app.PendingIntent
import android.content.Intent
import android.support.v4.app.TaskStackBuilder
import kotlinx.android.synthetic.main.task_list_recyclerview.*
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.os.Bundle






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


fun getNotificationIcon(): Int {
    val useWhiteIcon = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP
    return if (useWhiteIcon) R.drawable.notification_icon else R.mipmap.ic_launcher
}




fun dismissNotification(context: Context, taskindex: Int) {

    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.cancel(taskindex)
}


fun makeNotification(context: Context, task: TaskContent.TaskItem) {

    val taskindex = TaskContent.TASKS.indexOf(task)
    val tasktitle = task.title

    // Create an explicit intent for an Activity in your app
    val intent = Intent(context, MainTaskList::class.java)
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)


//    val broadcastIntent = Intent(context, ToggleBroadcastReceiver::class.java)
//    broadcastIntent.putExtra("toastMessage", "ttttttodddd")
//    val actionIntent = PendingIntent.getBroadcast(context,
//            0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)


    val taskbundle = Bundle()
    taskbundle.putString("tasktitle", tasktitle)
    taskbundle.putInt("taskindex", taskindex)


    val broadcastIntent2 = Intent(context, StopBroadcastReceiver::class.java)
    broadcastIntent2.putExtra("taskbundle", taskbundle)
    val actionIntent2 = PendingIntent.getBroadcast(context,
            0, broadcastIntent2, PendingIntent.FLAG_UPDATE_CURRENT)


    val mBuilder = NotificationCompat.Builder(context, "321")
            .setSmallIcon(getNotificationIcon())
            .setContentTitle(tasktitle)
            .setOngoing(true)
            .setColor(Color.parseColor("#bb7c00"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.mipmap.ic_launcher, "Stop", actionIntent2)
//            .addAction(R.mipmap.ic_launcher, "Dismiss", actionIntent)
            .setContentIntent(pendingIntent)
//            .setStyle(NotificationCompat.InboxStyle()
//                    .addLine("test1")
//                    .addLine("test2"))


    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(taskindex, mBuilder.build())



}

