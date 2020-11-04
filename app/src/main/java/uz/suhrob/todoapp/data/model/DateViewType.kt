package uz.suhrob.todoapp.data.model

import uz.suhrob.todoapp.util.toFormattedDate

data class DateViewType(
    val date: Long
) {
    fun getDateString(): String {
        return date.toFormattedDate()
    }
}