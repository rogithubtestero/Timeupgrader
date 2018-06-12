package com.robinrosenstock.timeupgrader

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.time_detail.*
import kotlinx.android.synthetic.main.time_detail_fragment.view.*

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [MainTaskList]
 * in two-pane mode (on tablets) or a [TimeDetail]
 * on handsets.
 */
class TimeDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: TaskContent.TaskItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ITEM_POS)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = TaskContent.TASKS[it.getInt(ITEM_POS)]
                activity?.toolbar_layout_time?.title = item?.title
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.time_detail_fragment, container, false)


        val builder = StringBuilder()

        item!!.interval_list.forEach {
            builder.append(it.getBeginTimeFormatted() + "\n")
            builder.append(it.getEndTimeFormatted() + "\n")
            builder.append("\n")
        }

        rootView.item_detail.text = builder.toString()

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ITEM_POS = "item_id"
    }
}
