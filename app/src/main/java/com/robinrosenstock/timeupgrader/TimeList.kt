package com.robinrosenstock.timeupgrader

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.time_detail.*
import kotlinx.android.synthetic.main.time_fragment.view.*
import kotlinx.android.synthetic.main.time_list.*
import kotlinx.android.synthetic.main.time_list_recyclerview.*
import net.steamcrafted.lineartimepicker.adapter.DateAdapter
import net.steamcrafted.lineartimepicker.adapter.LinearPickerAdapter
import net.steamcrafted.lineartimepicker.adapter.TimeAdapter
import net.steamcrafted.lineartimepicker.dialog.LinearTimePickerDialog
import net.steamcrafted.lineartimepicker.view.LinearPickerView
import net.steamcrafted.lineartimepicker.view.LinearTimePickerView
import java.io.File
import java.text.AttributedCharacterIterator
import java.util.jar.Attributes
import kotlin.coroutines.experimental.coroutineContext

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

        val dividerItemDecoration = DividerItemDecoration(time_list.context,1 )
        time_list.addItemDecoration(dividerItemDecoration)

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


    class myLinearPickerAdapter : LinearPickerAdapter{
        override fun onDraw(canvas: Canvas?, elementBounds: Array<out Rect>?, gravity: LinearPickerAdapter.Gravity?) {
            // This method is called once by the LinearPickerView every time it draws itself
            // You can use this to draw a custom background as all the other elements will be drawn on top
            // The array elementBounds contains the exact space and location that every element on the linear dial may use
        }

        override fun onDrawHandle(index: Int, intermediate: Int, canvas: Canvas?, bounds: Rect?, gravity: LinearPickerAdapter.Gravity?, occluded: LinearPickerAdapter.ScreenHalf?) {
            // This method is called last. Draw the handle here.
            // index corresponds to the currently selected visible pip index.
            // intermediate corresponds to the invisible pip step (0 -> visible pip selected, > 0 -> invisible pip selected)
            // bounds The bounds inside which you should draw the handle (once again just a hint)
            // occluded Which half of the screen the user's finger is currently touching
            // gravity Currently unused, part of a future API
        }

        override fun getLargePipCount(): Int {
            // Should provide the number of large pips to display (constant value)
            return 30
        }

        override fun getSmallPipCount(): Int {
            // Should provide the number of small pips between 2 large pips to display (constant value)
            return 10
        }

        override fun onDrawElement(index: Int, canvas: Canvas?, bounds: Rect?, yOffset: Float, gravity: LinearPickerAdapter.Gravity?) {
            // This method is called by the LinearPickerView every time a dial element has to be drawn
            // index denotes the visible pip index of the dial element (see pip section)
            // bounds gives a hint to where you should draw the dial element and also the size of the element
            // yOffset An yOffset of 1f corresponds to a dial element that is located the distance between
            //     2 big pips from the handle. In the picker examples this is used to fade out the small pips
            // gravity Currently unused, part of a future API
        }



        override fun getInvisiblePipCount(visiblePipIndex: Int): Int {
            // Should provide the number of "invisible pips" or substeps between any 2 visible pips (can vary between pips)
            // For more info on the visiblePipIndex, see the pip section below

        return 1
        }

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


//                val interval = v.tag as TaskContent.IntervalItem
//
//                val pp = myLinearPickerAdapter()
//                val muh =  LinearPickerView(v.context)
//
//                muh.setAdapter(pp)
//
//                addNewTaskDialog()


                val dialog = LinearTimePickerDialog.Builder.with(v.context)
                        .setPickerBackgroundColor(Color.WHITE)
                        .setLineColor(Color.BLACK)
                        .setTextColor(Color.BLACK)
                        .setShowTutorial(false)
                        .setButtonColor(Color.BLACK)
                        .setTextBackgroundColor(Color.LTGRAY)
                        .build()

                dialog.show()






            }



            onLongClickListener = View.OnLongClickListener {
                val popup = PopupMenu(this.parentActivity, it)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId){
                        R.id.rename_task -> {

//                            val task = it.tag as TaskContent.TaskItem

//                            parentActivity.renameTaskDialog(task, it)


                            true
                        }
                        R.id.delete_task -> {

//                            val task = it.tag as TaskContent.TaskItem
//
////                            reLoad()
//
//                            val snackbar = Snackbar.make(it, task.toString() + " DELETED!", 5000)
//                            snackbar.setAction("undo", TimeDetail.MyUndoListener())
//                            snackbar.show()
//                            parentActivity.setupRecyclerView(parentActivity.findViewById(R.id.task_list))

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

            holder.contentView.text = item.getBeginTimeFormatted()
            holder.contentView2.text = item.getEndTimeFormatted()

            holder.time_duration.text = item.getFormattedDuration()

            holder.time_duration.setOnClickListener {
                Toast.makeText(it.rootView.context,"not yet implemented", Toast.LENGTH_LONG).show()

            }


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
            val contentView2: TextView = view.content_time2
//            val buttonView: ToggleButton = view.push_button_time
            val time_duration: TextView = view.textview_duration
        }
    }


    class MyUndoListener : View.OnClickListener {

        override
        fun onClick(v: View) {

            Toast.makeText(v.context,"not yet implemented", Toast.LENGTH_LONG).show()
            // Code to undo the user's last action
        }
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
