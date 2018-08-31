package com.robinrosenstock.timeupgrader

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import kotlinx.android.synthetic.main.task_fragment.view.*
import kotlinx.android.synthetic.main.task_list_recyclerview.*
import org.joda.time.DateTime


class RecyclerViewAdapterForTasks(private val parentActivity: MainTaskList,
                                  private val values: List<TaskContent.TaskItem>,
                                  private val twoPane: Boolean) :
        RecyclerView.Adapter<RecyclerViewAdapterForTasks.ViewHolder>() {

    private val onClickListener: View.OnClickListener
    private val onLongClickListener: View.OnLongClickListener


    init {
        onClickListener = View.OnClickListener { v ->
            val task = v.tag as TaskContent.TaskItem
            if (twoPane) {
                val fragment = TimeDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(TimeDetailFragment.ITEM_POS, task.pos.toString())
                    }
                }
                parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
            } else {
                val intent = Intent(v.context, TimeDetail::class.java).apply {
                    putExtra(TimeDetailFragment.ITEM_POS, task.pos.toString())
                }
                v.context.startActivity(intent)
            }
        }



        onLongClickListener = View.OnLongClickListener {
            val popup = PopupMenu(this.parentActivity, it)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId){
                    R.id.rename_task -> {

                        val task = it.tag as TaskContent.TaskItem
                        renameTaskDialog(task, it.context)
                        true

                    }
                    R.id.delete_task -> {
                        val task = it.tag as TaskContent.TaskItem

                        val index = TaskContent.TASKS.indexOf(task)
                        TaskContent.TASKS.removeAt(index)
                        parentActivity.task_list.adapter.notifyItemRemoved(index)

                        val snackbar = Snackbar.make(it, task.toString() + " DELETED!", 5000)
                            snackbar.setAction("undo", UndoTaskDelete(parentActivity.task_list, task, index))
                            snackbar.show()

                        true
                    }
                    else -> false
                }
            }
            popup.inflate(R.menu.context_task_menu)
            popup.show()
            true

        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_fragment, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val task = values[position]
        holder.contentView.text = task.title
        val index = TaskContent.TASKS.indexOf(task)


        if(task.ongoing){
            holder.buttonView.isChecked = true
        }else{
            holder.buttonView.isChecked = false
        }

        holder.buttonView.setOnClickListener {

            if(holder.buttonView.isChecked) {

                val intervall_item =  TaskContent.IntervalItem(123, DateTime.now(), null)
                task.interval_list.add(0,intervall_item)
                task.ongoing = true

            }
            else{

                task.interval_list.forEach {intervalitem ->
                    if (intervalitem.end_time == null) {
                        intervalitem.end_time = DateTime.now()
                    }
                }
                task.ongoing = false
            }

            parentActivity.task_list.adapter.notifyItemChanged(index)
        }

        with(holder.itemView) {
            tag = task
            setOnClickListener(onClickListener)
            setOnLongClickListener(onLongClickListener)
        }
    }


    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //            val idView: TextView = view.id_text
        val contentView: TextView = view.content
        val buttonView: ToggleButton = view.push_button
    }
}