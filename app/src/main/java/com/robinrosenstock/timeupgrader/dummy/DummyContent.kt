package com.robinrosenstock.timeupgrader.dummy

import android.os.Environment
import android.util.Log
import android.widget.Toast
import java.io.*
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

//        retrieve the saved time.txt file

        val file = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "tttestfile")
        try {
            val fIn = FileInputStream (file)
            val file = InputStreamReader (fIn)
            val br = BufferedReader (file)
            var line = br.readLine ()
            val all = StringBuilder ()
            var item_id = 0

            while (line != null) {
                if (line.isNotBlank()){
                    val neger = DummyContent.DummyItem(item_id.toString(), line, "details will be filled later")
                    DummyContent.ITEMS.add(neger)

                    all.append (line + "\n")
                    item_id = item_id.inc()
                }
                line = br.readLine ()

            }
            br.close ()
            file.close ()

        } catch (e: IOException) {
//            Toast.makeText (, "Could not read", Toast.LENGTH_SHORT) .show ()
            Log.d("tag","text")
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
