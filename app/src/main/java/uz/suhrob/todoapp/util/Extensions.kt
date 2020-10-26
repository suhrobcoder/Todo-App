package uz.suhrob.todoapp.util

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun Activity.startNewActivity(activity: Class<*>) {
    startActivity(Intent(applicationContext, activity))
    finish()
}

fun Fragment.startNewActivity(nextActivity: Class<*>) {
    startActivity(Intent(requireContext(), nextActivity))
    (activity as AppCompatActivity).finish()
}

fun Fragment.displayBackButton() {
    (activity as AppCompatActivity).supportActionBar?.apply {
        setDisplayHomeAsUpEnabled(true)
        setDisplayShowHomeEnabled(true)
    }
}

fun Fragment.onBackPressed() {
    (activity as AppCompatActivity).onBackPressed()
}

fun Activity.setStatusBarColor(color: Int) {
    this.window.apply {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            statusBarColor = color
        }
    }
}

fun Fragment.setToolbar(toolbar: Toolbar) {
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
}

fun Fragment.toast(text: String) {
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT)
}

fun String.parseColor(): Int =
    Color.parseColor(this)