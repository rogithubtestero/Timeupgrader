package com.robinrosenstock.timeupgrader

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.task_list_recyclerview.*
//import kotlinx.android.synthetic.main.time_detail.*
import kotlinx.android.synthetic.main.time_list.*
import kotlinx.android.synthetic.main.time_list_recyclerview.*
import java.util.*

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [MainTaskList].
 */
class TimeDetail : AppCompatActivity() {


    private var twoPane: Boolean = false

    var clicked_task_id : Int = -1
    lateinit var clicked_task : TaskContent.TaskItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_list)
        setSupportActionBar(toolbar_time)

        val dividerItemDecoration = DividerItemDecoration(time_list.context,1 )
        time_list.addItemDecoration(dividerItemDecoration)

//        get the clicked task
        clicked_task_id = intent.extras.getString(TimeDetailFragment.ITEM_POS).toInt()
        clicked_task = TaskContent.TASKS[clicked_task_id]

        toolbar_time.title = clicked_task.title + " - Times"


        fab_time.setOnClickListener {
            addTimeDialog(this, clicked_task_id)
        }


        time_list.adapter = RecyclerViewAdapterForTime(this, clicked_task.interval_list, twoPane, clicked_task)
    }



    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    // This ID represents the Home or Up button. In the case of this
                    // activity, the Up button is shown. For
                    // more details, see the Navigation pattern on Android Design:
                    //
                    // http://developer.android.com/design/patterns/navigation.html#up-vs-back

                    navigateUpTo(Intent(this, MainTaskList::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
