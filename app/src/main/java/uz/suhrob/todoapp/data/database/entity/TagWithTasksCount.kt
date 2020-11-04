package uz.suhrob.todoapp.data.database.entity

import androidx.room.Embedded

data class TagWithTasksCount(
    @Embedded val tag: Tag,
    val tasksCount: Int
)