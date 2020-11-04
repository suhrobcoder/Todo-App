package uz.suhrob.todoapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "check_list")
data class CheckList(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "color") val color: Int
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0
}