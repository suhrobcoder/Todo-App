package uz.suhrob.todoapp.data.database.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.suhrob.todoapp.data.database.entity.CheckList
import uz.suhrob.todoapp.data.database.entity.CheckListItem
import uz.suhrob.todoapp.data.database.entity.CheckListWithItems

@Dao
interface CheckListWithItemsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckList(checkList: CheckList): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheckListItems(items: List<CheckListItem>)

    @Transaction
    suspend fun insertCheckListAndItems(checkListWithItems: CheckListWithItems) {
        val checkListItems = checkListWithItems.checkListItems
        val checkList = checkListWithItems.checkList
        val checkListId = insertCheckList(checkList).toInt()
        for (item in checkListItems) {
            item.checkListId = checkListId
        }
        insertCheckListItems(checkListItems)
    }

    @Update
    suspend fun updateCheckListItem(item: CheckListItem)

    @Query("SELECT * FROM check_list")
    fun getAllCheckLists(): Flow<List<CheckListWithItems>>
}