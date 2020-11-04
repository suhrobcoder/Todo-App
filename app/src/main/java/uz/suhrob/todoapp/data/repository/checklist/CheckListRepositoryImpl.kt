package uz.suhrob.todoapp.data.repository.checklist

import kotlinx.coroutines.flow.Flow
import uz.suhrob.todoapp.data.database.dao.CheckListWithItemsDao
import uz.suhrob.todoapp.data.database.entity.CheckListItem
import uz.suhrob.todoapp.data.database.entity.CheckListWithItems

class CheckListRepositoryImpl(
    private val checkListWithItemsDao: CheckListWithItemsDao
) : CheckListRepository {
    override suspend fun insertCheckList(checkListWithItems: CheckListWithItems) =
        checkListWithItemsDao.insertCheckListAndItems(checkListWithItems)

    override suspend fun updateCheckListItem(checkListItem: CheckListItem) =
        checkListWithItemsDao.updateCheckListItem(checkListItem)

    override fun getAllCheckLists(): Flow<List<CheckListWithItems>> =
        checkListWithItemsDao.getAllCheckLists()
}