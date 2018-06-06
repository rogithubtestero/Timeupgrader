package com.robinrosenstock.timeupgrader

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import com.robinrosenstock.timeupgrader.dummy.TaskContent
import kotlinx.android.synthetic.main.time_detail.*
import kotlinx.android.synthetic.main.time_fragment.view.*
import kotlinx.android.synthetic.main.time_list.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.io.File

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [MainTaskList].
 */
class TimeDetail : AppCompatActivity() {


    private var twoPane: Boolean = false

    var clicked_task_id : Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_list)
        setSupportActionBar(toolbar_time)
        toolbar_time.title = title



//        get the clicked task
        clicked_task_id = intent.extras.getString(TimeDetailFragment.ARG_ITEM_ID).toInt()




        fab_time.setOnClickListener {

            ////// start other display/class:
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)

//          ////////  add a task through a custom dialog, learned from here: <https://www.youtube.com/watch?v=Z9LhAgBSlhU> /////
//            addNewTaskDialog(it)
        }



        if (item_detail_container_time != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }


//        if first start of app then make the time.txt file:



            // Permission has already been granted

            val filedirectory = Environment.getExternalStoragePublicDirectory("/time")
            val file = File(filedirectory, "time.txt")


            if (file.canWrite()) {
//                parseFile("time.txt")

            }
            else{
                filedirectory.mkdirs()
                file.createNewFile()
            }

            setupRecyclerView(findViewById(R.id.time_list))



    }



    private fun setupRecyclerView(recyclerView: RecyclerView) {

        val muh = TaskContent.TASKS[clicked_task_id].interval_list

        recyclerView.adapter = SimpleItemRecyclerViewAdapter2(this, muh, twoPane)

    }

    class SimpleItemRecyclerViewAdapter2(private val parentActivity: TimeDetail,
                                        private val values: List<TaskContent.IntervalItem>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter2.ViewHolder>() {

        private val onClickListener: View.OnClickListener
        private val onLongClickListener: View.OnLongClickListener


        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as TaskContent.TaskItem
                if (twoPane) {
                    val fragment = TimeDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(TimeDetailFragment.ARG_ITEM_ID, item.pos.toString())
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, TimeDetail::class.java).apply {
                        putExtra(TimeDetailFragment.ARG_ITEM_ID, item.pos.toString())
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

//                            parentActivity.renameTaskDialog(task, it)


                            true
                        }
                        R.id.delete_task -> {
                            val task = it.tag as TaskContent.TaskItem

                            removeWholeTask(task)
                            reLoad()

                            val snackbar = Snackbar.make(it, task.toString() + " DELETED!", 5000)
                            snackbar.setAction("undo", TimeDetail.MyUndoListener())
                            snackbar.show()
                            parentActivity.setupRecyclerView(parentActivity.findViewById(R.id.task_list))

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
                    .inflate(R.layout.time_fragment, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.contentView.text = item.begin_time_number.toString()


//            item.interval_list.forEach {
//                if (it.end_time == null) {
//                    holder.buttonView.isChecked = true
//                }
//                else{
//                    holder.buttonView.isChecked =false
//                }
//            }


//            holder.buttonView.setOnCheckedChangeListener { buttonView, isChecked ->
//
//                if(isChecked) {
//
//                    val start_time = DateTime.now()
//
//                    val eins : Int?
//                    val zwei : Int?
//                    val intervalItem: TaskContent.IntervalItem
//
//
//                    if (item.interval_list.size > 0){
//
//                        eins = item.interval_list.last().begin_time_number
//                        zwei = item.interval_list.last().end_time_number
//                        intervalItem = TaskContent.IntervalItem(start_time, null, eins!!.plus(3), eins!!.plus(4))
//                        addIntervalItemToFile("time.txt", intervalItem, item.interval_list.last().end_time_number)
//
//                    }else{
//
//                        eins = item.line_number
//                        intervalItem = TaskContent.IntervalItem(start_time, null, eins!!.plus(2), eins!!.plus(3))
//                        addIntervalItemToFile("time.txt", intervalItem, item.line_number)
//
//                    }
//
//                    reLoad()
//                    Snackbar.make(buttonView, item.toString() + " START!", 4000).show()
//                    parentActivity.setupRecyclerView(parentActivity.findViewById(R.id.task_list))
//
//                }
//                else{
//                    val ongoingIntervalItem = searchOngoingIntervalItem(item)
//                    ongoingIntervalItem!!.end_time = DateTime.now()
//                    val linenumber = ongoingIntervalItem.end_time_number
//                    val text = ongoingIntervalItem.end_time!!.toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))
//
//                    replaceLineInFile("time.txt", linenumber, text)
//                    reLoad()
//                    Snackbar.make(buttonView, item.toString() + " STOP!", 4000).show()
//                    parentActivity.setupRecyclerView(parentActivity.findViewById(R.id.task_list))
//                }
//            }

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
                setOnLongClickListener(onLongClickListener)

            }
        }



        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            //            val idView: TextView = view.id_text
            val contentView: TextView = view.content_time
            val buttonView: ToggleButton = view.push_button_time
        }
    }


    class MyUndoListener : View.OnClickListener {

        override
        fun onClick(v: View) {

            Toast.makeText(v.context,"not yet implemented", Toast.LENGTH_LONG).show()
            // Code to undo the user's last action
        }
    }





//    private fun renameTaskDialog(taskToRename : TaskContent.TaskItem, rootview : View) {
//
//        val dialogBuilder = AlertDialog.Builder(this@TimeDetail)
//        dialogBuilder.setTitle("Rename Task")
////        dialogBuilder.setMessage("I am a alert dialog!")
//        val view = layoutInflater.inflate(R.layout.alert_dialog, null)
//        dialogBuilder.setView(view)
//
//        view.alert_dialog_text.text = "What is the new name?"
//        view.alert_dialog_button.text = "Rename!"
//
//        val alertDialog = dialogBuilder.create()
//
//        alertDialog.show()
//
//        view.alert_dialog_button.setOnClickListener{
//
//            val new_task_name = view.alert_dialog_text_input.text.toString()
//
//            renameTask(taskToRename, new_task_name)
//            reLoad()
//
//            alertDialog.dismiss()
//
//
//            Snackbar.make(rootview, "${taskToRename.title} RENAMED to: $new_task_name", 5000).show()
//
//            setupRecyclerView(findViewById(R.id.item_list))
//        }
//    }



//    private fun addNewTaskDialog(rootview : View) {
//
//        val dialogBuilder = AlertDialog.Builder(this@TimeDetail)
//        dialogBuilder.setTitle("Add a new task")
////        dialogBuilder.setMessage("I am a alert dialog!")
//        val view = layoutInflater.inflate(R.layout.alert_dialog, null)
//        dialogBuilder.setView(view)
//
//        val alertDialog = dialogBuilder.create()
//
//        alertDialog.show()
//
//        view.alert_dialog_button.setOnClickListener{
//
//            val lastpos = TaskContent.TASKS.last().pos
//            val task_name = view.alert_dialog_text_input.text.toString()
//            val task_entry = TaskContent.TaskItem(lastpos+1, task_name, ArrayList(), 5)
//            TaskContent.TASKS.add(task_entry)
//            TaskContent.TASK_MAP.put(task_entry.pos.toString(), task_entry)
//
////            append the new task to the end of the file:
////            val time_entry_format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
//            File(Environment.getExternalStoragePublicDirectory("/time"), "time.txt").appendText("\n\n# $task_name")
//
//            reLoad()
//            alertDialog.dismiss()
//
//            Snackbar.make(rootview, "$task_name ADDED!", 4000).show()
//
//            setupRecyclerView(findViewById(R.id.item_list))
//        }
//    }




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
