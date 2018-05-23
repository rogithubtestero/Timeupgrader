package com.robinrosenstock.timeupgrader

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.app.Activity
import android.os.Environment

import kotlinx.android.synthetic.main.activity_file.*
import java.io.*

class Fileactor : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file)
        setSupportActionBar(toolbar)


//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }



        val et1 = findViewById (R.id.et1) as EditText
        val et2 = findViewById (R.id.et2) as EditText

        val button1 = findViewById (R.id.button1) as Button
        button1.setOnClickListener {
            try {
                val card = Environment.getExternalStorageDirectory ()
                val file = File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOCUMENTS), et1.text.toString())
                val osw = OutputStreamWriter (FileOutputStream (file))
                osw.write (et2.text.toString ())
                osw.flush ()
                osw.close ()
                Toast.makeText (this, "The data was recorded correctly", Toast.LENGTH_SHORT) .show ()
                et1.setText ("")
                et2.setText ("")
            } catch (ioe: IOException) {
                Toast.makeText (this, "Could not burn", Toast.LENGTH_SHORT) .show ()
            }
        }

        val button2 = findViewById (R.id.button2) as Button
        button2.setOnClickListener {
            val card = Environment.getExternalStorageDirectory ()
            val file = File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS), et1.text.toString())
            try {
                val fIn = FileInputStream (file)
                val file = InputStreamReader (fIn)
                val br = BufferedReader (file)
                var line = br.readLine ()
                val all = StringBuilder ()
                while (line!= null) {
                    all.append (line + "\n")
                    line = br.readLine ()
                }
                br.close ()
                file.close ()
                et2.setText (all)

            } catch (e: IOException) {
                Toast.makeText (this, "Could not read", Toast.LENGTH_SHORT) .show ()
            }
        }



    }

}
