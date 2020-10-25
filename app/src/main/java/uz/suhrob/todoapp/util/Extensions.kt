package uz.suhrob.todoapp.util

import android.app.Activity
import android.content.Intent
import android.graphics.Color

fun Activity.startNewActivity(activity: Class<*>) {
    startActivity(Intent(applicationContext, activity))
    finish()
}

fun Activity.setStatusBarColor(color: Int) {
    this.window.apply {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            statusBarColor = color
        }
    }
}

fun String.parseColor(): Int =
    Color.parseColor(this)