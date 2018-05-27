package com.robinrosenstock.timeupgrader

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.robinrosenstock.timeupgrader.dummy.DummyContent
import java.io.File




fun writeFile(filename: String){

    ////// save  entry/line to the file //////////
    File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOCUMENTS), filename).bufferedWriter().use { out ->
        DummyContent.ITEMS.forEach {
            out.write(it.content + "\n")
        }
    }
}



fun readFile(context: Context, filename: Uri?) {

    val input = context.getContentResolver().openInputStream(filename)
    input.bufferedReader().use {
        var line = it.readLine()

        while (line != null) {
            if (line.isNotBlank()) {
                val neger = DummyContent.DummyItem("222", line, "details will be filled later")
                DummyContent.ITEMS.add(neger)
            }
            line = it.readLine()
        }
    }
}