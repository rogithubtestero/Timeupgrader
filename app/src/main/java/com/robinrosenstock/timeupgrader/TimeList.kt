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
//import kotlinx.android.synthetic.main.time_detail.*
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

        val dividerItemDecoration = DividerItemDecoration(time_list.context,1 )
        time_list.addItemDecoration(dividerItemDecoration)

//        get the clicked task
        clicked_task_id = intent.extras.getString(TimeDetailFragment.ITEM_POS).toInt()
        clicked_task = TaskContent.TASKS[clicked_task_id]

        toolbar_time.title = clicked_task.title


        fab_time.setOnClickListener {
        }

        time_list.adapter = SimpleItemRecyclerViewAdapter(this, clicked_task.interval_list, twoPane)
    }




    class SimpleItemRecyclerViewAdapter(private val parentActivity: TimeDetail,
                                        private val values: List<TaskContent.IntervalItem>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener
        private val onLongClickListener: View.OnLongClickListener

        init {
            onClickListener = View.OnClickListener { _ ->

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


            onLongClickListener = View.OnLongClickListener {
                val popup = PopupMenu(this.parentActivity, it)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId){
                        R.id.rename_task -> {

                            true
                        }
                        R.id.delete_task -> {


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
