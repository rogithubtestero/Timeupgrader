package com.robinrosenstock.timeupgrader

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.robinrosenstock.timeupgrader.dummy.TaskContent
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.item_detail.view.*

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [ItemListActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: TaskContent.TaskItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = TaskContent.TASK_MAP[it.getString(ARG_ITEM_ID)]
                activity?.toolbar_layout?.title = item?.title
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.item_detail, container, false)


        val builder = StringBuilder()
//        for (text in item?.interval_list.toString()) {
//            builder.append(text + "\n")
//        }

        item?.interval_list?.forEach { intervalItem ->
            builder.append(intervalItem.getTimeFormatted(intervalItem.begin_time) + "\n")
            builder.append(intervalItem.getTimeFormatted(intervalItem.end_time) + "\n")
            builder.append("\n")
        }


        rootView.item_detail.text = builder.toString()


        // Show the dummy content as text in a TextView.
//        item?.let {




//            rootView.item_detail.text = it.interval_list.joinToString()

//            it.interval_list.forEach {
//                rootView.item_detail.text = it.toString()
//            }
//        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}
