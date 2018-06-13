package com.robinrosenstock.timeupgrader

import android.app.FragmentManager
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.SupportActivity
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import kotlinx.android.synthetic.main.time_detail.*
import kotlinx.android.synthetic.main.time_fragment.view.*
import kotlinx.android.synthetic.main.time_list.*
import kotlinx.android.synthetic.main.time_list_recyclerview.*
import net.steamcrafted.lineartimepicker.adapter.BaseTextAdapter
import net.steamcrafted.lineartimepicker.adapter.DateAdapter
import net.steamcrafted.lineartimepicker.adapter.LinearPickerAdapter
import net.steamcrafted.lineartimepicker.adapter.TimeAdapter
import net.steamcrafted.lineartimepicker.dialog.LinearTimePickerDialog
import net.steamcrafted.lineartimepicker.view.LinearPickerView
import net.steamcrafted.lineartimepicker.view.LinearTimePickerView
import org.joda.time.DateTime
import java.io.File
import java.text.AttributedCharacterIterator
import java.text.SimpleDateFormat
import java.util.*
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
    lateinit var clicked_task : TaskContent.TaskItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.time_list)
        setSupportActionBar(toolbar_time)
        toolbar_time.title = title

        val dividerItemDecoration = DividerItemDecoration(time_list.context,1 )
        time_list.addItemDecoration(dividerItemDecoration)

//        get the clicked task
        clicked_task_id = intent.extras.getString(TimeDetailFragment.ITEM_POS).toInt()
        clicked_task = TaskContent.TASKS[clicked_task_id]

        fab_time.setOnClickListener {
        }

//        if (item_detail_container_time != null) {
//            twoPane = true
//        }

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

    private fun timePickerDialog(rootview : View, linearPickerView : LinearPickerView) {

        val dialogBuilder = AlertDialog.Builder(this@TimeDetail)
//        dialogBuilder.setTitle("Add a new task")
//        dialogBuilder.setMessage("I am a alert dialog!")
//        val view = layoutInflater.inflate(R.layout.alert_dialog, null)

        dialogBuilder.setPositiveButton("YES"){dialog, which ->
            // Do something when user press the positive button
            Toast.makeText(applicationContext,"Ok, we change the app background.",Toast.LENGTH_SHORT).show()

            // Change the app background color
//            root_layout.setBackgroundColor(Color.RED)
        }

        dialogBuilder.setView(linearPickerView)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()

    }




    class OnSwipeTouchListener : View.OnTouchListener {

        lateinit var gestureDetector: GestureDetector


        fun OnSwipeTouchListener(ctx: Context) {
            gestureDetector = GestureDetector(ctx, GestureListener());
        }


        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            return gestureDetector.onTouchEvent(event)
        }


        private class GestureListener : GestureDetector.SimpleOnGestureListener() {

            val SWIPE_THRESHOLD: Int = 200
            val SWIPE_VELOCITY_THRESHOLD: Int = 200

            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                onItemTouch(e!!.getX(), e.getY())
                return true;

            }

            override fun onLongPress(e: MotionEvent?) {
                onItemLongTouch(e!!.getX(), e.getY())
            }


            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                var result: Boolean = false

                try {
                    val diffY = e2!!.getY() - e1!!.getY()
                    val diffX = e2!!.getX() - e1!!.getX()
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight()
                            } else {
                                onSwipeLeft()
                            }
                        } else {
                            onItemTouch(e2.getX(), e2.getY())
                        }
                        result = true
                    }
                } catch (exception: Exception) {
                }
                return result


            }


            fun onSwipeRight() {
            }

            fun onSwipeLeft() {
            }

            fun onSwipeTop() {
            }

            fun onSwipeBottom() {
            }

            fun onItemTouch(x: Float, y: Float) {
            }

            fun onItemTouch() {
            }

            fun onItemLongTouch(x: Float, y: Float) {
            }

            fun onItemLongTouch() {
            }

        }
    }



    private fun setupRecyclerView(recyclerView: RecyclerView) {

        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, clicked_task.interval_list, twoPane)

    }

    class SimpleItemRecyclerViewAdapter(private val parentActivity: TimeDetail,
                                        private val values: List<TaskContent.IntervalItem>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {


        private val onClickListener: View.OnClickListener
        private val onLongClickListener: View.OnLongClickListener
//        private val onTouchListener: View.OnTouchListener

        init {
            onClickListener = View.OnClickListener { v ->

                val interval = v.tag as TaskContent.IntervalItem

//                ////////////// this is linear time picker
//                val pp = myLinearPickerAdapter()
//                val linearPickerView =  LinearPickerView(v.context)
//                val textPaint = Paint()
//                val colorAdap = ColorAdapter(v.context, textPaint)
//                val TimeAdap = TimeAdapter(v.context, textPaint)
//                linearPickerView.setAdapter(TimeAdap)
//                linearPickerView.setAdapter(pp)
//                parentActivity.timePickerDialog(v, linearPickerView)

                ////////////// this is SwitchDateTimePicker:
                val dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                        "Title example",
                        "OK",
                        "Cancel"
                )

                dateTimeDialogFragment.startAtTimeView()
                dateTimeDialogFragment.set24HoursMode(true)

                DateTime.now().toDate()

//                dateTimeDialogFragment.setDefaultDateTime(GregorianCalendar(2017, Calendar.MARCH, 4, 15, 20).getTime())

// Define new day and month format
                try {
                    dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(SimpleDateFormat("dd MMMM", Locale.getDefault()));
                } catch (e : SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException ) {
//    Log.e(TAG, e.getMessage());
                }


                dateTimeDialogFragment.setOnButtonClickListener(object : SwitchDateTimeDialogFragment.OnButtonClickListener{

                    override fun onPositiveButtonClick(date : Date){

                    }

                    override fun onNegativeButtonClick(date: Date) {

                    }
                }
                )

                dateTimeDialogFragment.show(parentActivity.supportFragmentManager, "dialog_time")
            }



//            onTouchListener = View.OnTouchListener()


//            onTouchListener =  View.OnTouchListener{ view: View, motionEvent: MotionEvent ->
//
//            }







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
//                setOnClickListener(onClickListener)
//                setOnLongClickListener(onLongClickListener)
//                setOnTouchListener(onTouchListener)

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
