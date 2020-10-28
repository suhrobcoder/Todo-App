package uz.suhrob.todoapp.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CheckListWithItems(
    @Embedded val checkList: CheckList,
    @Relation(
        parentColumn = "id",
        entityColumn = "check_list_id"
    )
    val checkListItems: List<CheckListItem>
)