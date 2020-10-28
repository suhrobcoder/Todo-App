package uz.suhrob.todoapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "check_list_item")
data class CheckListItem(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "check_list_id") var checkListId: Int,
    @ColumnInfo(name = "checked") val checked: Boolean
)