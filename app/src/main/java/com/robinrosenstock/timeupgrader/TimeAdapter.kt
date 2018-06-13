package com.robinrosenstock.timeupgrader

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment
import kotlinx.android.synthetic.main.time_fragment.view.*
import java.text.SimpleDateFormat
import java.util.*
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment
import android.R.attr.button
import com.codetroopers.betterpickers.timepicker.TimePickerBuilder
import kotlinx.android.synthetic.main.time_list_recyclerview.*
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.DateTime


class RecyclerViewAdapterForTime(private val parentActivity: TimeDetail,
                                 private val values: List<TaskContent.IntervalItem>,
                                 private val twoPane: Boolean,
                                 private val task: TaskContent.TaskItem) :
        RecyclerView.Adapter<RecyclerViewAdapterForTime.ViewHolder>() {


    private val onClickListener: View.OnClickListener
    private val onLongClickListener: View.OnLongClickListener

    init {
        onClickListener = View.OnClickListener { v ->

            val clicked_interval = v.tag as TaskContent.IntervalItem
            val clicked_index = task.interval_list.indexOf(clicked_interval)

            val dateTimeDialogFragment1 = SwitchDateTimeDialogFragment.newInstance("Begin_time", "OK", "Cancel", "Set Null"
            )
            dateTimeDialogFragment1.startAtTimeView()
            dateTimeDialogFragment1.set24HoursMode(true)
            dateTimeDialogFragment1.setDefaultDateTime(clicked_interval.begin_time?.toDate())


//// Define new day and month format
//            try {
//                dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(SimpleDateFormat("dd MMMM", Locale.getDefault()));
//            } catch (e : SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException ) {
////    Log.e(TAG, e.getMessage());
//            }


            dateTimeDialogFragment1.setOnButtonClickListener(object : SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener{
                override fun onPositiveButtonClick(date : Date){

//                    set begin time and go on
                    clicked_interval.begin_time = DateTime(date)
                    parentActivity.time_list.adapter.notifyItemChanged(clicked_index)


                    val dateTimeDialogFragment2 = SwitchDateTimeDialogFragment.newInstance("End time", "OK", "Cancel", "Set Null"
                    )
                    dateTimeDialogFragment2.startAtTimeView()
                    dateTimeDialogFragment2.set24HoursMode(true)
                    dateTimeDialogFragment2.setDefaultDateTime(clicked_interval.end_time?.toDate())


                    dateTimeDialogFragment2.setOnButtonClickListener(
                            object : SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener{

                        override fun onPositiveButtonClick(date: Date)
                        {

//                    set end time:
                            clicked_interval.end_time = DateTime(date)
                            parentActivity.time_list.adapter.notifyItemChanged(clicked_index)

                        }

                        override fun onNegativeButtonClick(date: Date)
                        {

//                    cancel
                        }

                        override fun onNeutralButtonClick(date: Date)
                        {
    //                    set end time null
                            Toast.makeText(parentActivity.baseContext,"not yet implemented", Toast.LENGTH_LONG).show()
                        }
                    })


                    dateTimeDialogFragment2.show(parentActivity.supportFragmentManager, "dialog_time")

                }
                override fun onNegativeButtonClick(date: Date) {

//                    cancel

                    val dateTimeDialogFragment3 = SwitchDateTimeDialogFragment.newInstance("End time", "OK", "Cancel", "Set Null"
                    )
                    dateTimeDialogFragment3.startAtTimeView()
                    dateTimeDialogFragment3.set24HoursMode(true)
                    dateTimeDialogFragment3.setDefaultDateTime(clicked_interval.end_time?.toDate())


                    dateTimeDialogFragment3.setOnButtonClickListener(
                            object : SwitchDateTimeDialogFragment.OnButtonWithNeutralClickListener{

                                override fun onPositiveButtonClick(date: Date)
                                {

            //                    set end time:
                                    clicked_interval.end_time = DateTime(date)
                                    parentActivity.time_list.adapter.notifyItemChanged(clicked_index)

                                }

                                override fun onNegativeButtonClick(date: Date)
                                {
            //                    cancel
                                }

                                override fun onNeutralButtonClick(date: Date)
                                {
                //                    set end time null
                                    Toast.makeText(parentActivity.baseContext,"not yet implemented", Toast.LENGTH_LONG).show()
                                }
                            })


                    dateTimeDialogFragment3.show(parentActivity.supportFragmentManager, "dialog_time")

                }

                override fun onNeutralButtonClick(date: Date){

                    //                    set begin time null and go on
                    Toast.makeText(parentActivity.baseContext,"not yet implemented", Toast.LENGTH_LONG).show()
                }

            }
            )
            dateTimeDialogFragment1.show(parentActivity.supportFragmentManager, "dialog_time")
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