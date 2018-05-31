package com.robinrosenstock.timeupgrader

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : AppCompatActivity() {
    val colors = mapOf("Black" to Color.BLACK, "Blue" to Color.BLUE, "Cyan" to Color.CYAN,
            "Dark Gray" to Color.DKGRAY, "Gray" to Color.GRAY, "Green" to Color.GREEN,
            "Light Gray" to Color.LTGRAY, "Magenta" to Color.MAGENTA, "Red" to Color.RED,
            "Transparent" to Color.TRANSPARENT, "White" to  Color.WHITE, "Yellow" to Color.YELLOW)

//    var prefs: Prefs? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bgColor = prefs.bgColor

        // using Anko for the layout
        verticalLayout {
            backgroundColor = bgColor

            radioGroup {
                colors.forEach {
                    radioButton {
                        val colorText = it.key
                        val colorInt = it.value
                        text = colorText
                        textColor = Color.WHITE
                        buttonTintList = ColorStateList.valueOf(Color.WHITE) // API 21+
                        onClick { selectColor(this@verticalLayout, colorInt) }
                    }
                }
            }
        }
    }



    private fun selectColor(view: View, color: Int) {
        view.backgroundColor = color

        prefs.bgColor = color
    }
}

