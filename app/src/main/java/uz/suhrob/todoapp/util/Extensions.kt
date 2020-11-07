package uz.suhrob.todoapp.util

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import java.text.SimpleDateFormat
import java.util.*

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
    Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
}

fun String.parseColor(): Int =
    Color.parseColor(this)

fun Long.toFormattedDate(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val calendarNow = Calendar.getInstance()
    val format = SimpleDateFormat("MMM dd/yyyy", Locale.US)
    val date = format.format(calendar.time)
    return when {
        calendarNow.daysBetweenDates(calendar) == 0 -> "Today, "
        calendarNow.daysBetweenDates(calendar) == 1 -> "Yesterday, "
        calendarNow.daysBetweenDates(calendar) == -1 -> "Tomorrow, "
        else -> ""
    } + " $date"
}

fun Long.toFormattedTime(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
}

fun Long.toCalendar(): Calendar = Calendar.getInstance().apply { timeInMillis = this@toCalendar }

fun Calendar.daysBetweenDates(other: Calendar): Int {
    val day1 = this.timeInMillis / 86400
    val day2 = other.timeInMillis / 86400
    return (day1 - day2).toInt()
}

fun Calendar.getCalendarDay() =
    CalendarDay.from(get(Calendar.YEAR), get(Calendar.MONTH)+1, get(Calendar.DAY_OF_MONTH))

operator fun CalendarDay.compareTo(other: CalendarDay): Int {
    val x1 = year * 366 + month * 12 + day
    val x2 = other.year * 366 + other.month * 12 + other.day
    return x1 - x2
}