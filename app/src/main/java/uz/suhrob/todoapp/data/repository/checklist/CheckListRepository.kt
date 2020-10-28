package uz.suhrob.todoapp.data.repository.checklist

import kotlinx.coroutines.flow.Flow
import uz.suhrob.todoapp.data.database.entity.CheckListItem
import uz.suhrob.todoapp.data.database.entity.CheckListWithItems

interface CheckListRepository {
    suspend fun insertCheckList(checkListWithItems: CheckListWithItems)
    suspend fun updateCheckListItem(checkListItem: CheckListItem)
    fun getAllCheckLists(): Flow<List<CheckListWithItems>>
}