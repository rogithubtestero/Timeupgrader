package com.robinrosenstock.timeupgrader.dummy

import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.robinrosenstock.timeupgrader.ItemListActivity
import com.robinrosenstock.timeupgrader.readFile

import java.io.*
import java.security.AccessController.getContext
import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Androides, see aapt output above for details.
	at com.android.build.gradle.internal.res.LinkApplica template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<DummyItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, DummyItem> = HashMap()


    init {

//here take the last opened file uri!!!! from sharedprefs? for readFile

//        but how to get the context?
//    val dtrn = getContext()
        //    readFile(dtrn, )

        File(Environment.getExternalStoragePublicDirectory("/time"), "time.txt").bufferedReader().use {
            var line = it.readLine()

            while (line != null) {
                if (line.isNotBlank()) {
                    val neger = DummyContent.DummyItem("222", line, "details will be filled later")
                    DummyContent.ITEMS.add(neger)
                }
                line = it.readLine()
            }
        }


//        addItem(DummyItem("1", "string1", "details1"))
//        addItem(DummyItem("2", "string2", "details2"))
//        addItem(DummyItem("3", "string3", "details3"))
    }



//    private val COUNT = 25
//
//    init {
//        // Add some sample items.
//        for (i in 1..COUNT) {
//            addItem(createDummyItem(i))
//        }
//    }

    private fun addItem(item: DummyItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }


    private fun deleteItem(item: DummyItem) {
        ITEMS.remove(item)
        ITEM_MAP.remove(item.id)
    }


//
//    private fun createDummyItem(position: Int): DummyItem {
//        return DummyItem(position.toString(), "Item " + position, makeDetails(position))
//    }
//
//    private fun makeDetails(position: Int): String {
//        val builder = StringBuilder()
//        builder.append("Details about Item: ").append(position)
//        for (i in 0..position - 1) {
//            builder.append("\nMore details information here.")
//        }
//        return builder.toString()
//    }
//
//

    /**
     * A dummy item representing a piece of content.
     */
    data class DummyItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}
