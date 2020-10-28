package uz.suhrob.todoapp.data.database.dao

import android.graphics.Color
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uz.suhrob.todoapp.data.database.TodoDatabase
import uz.suhrob.todoapp.data.database.entity.Tag
import uz.suhrob.todoapp.getOrAwaitValue

@RunWith(AndroidJUnit4::class)
@SmallTest
class TagDaoTest {
    private lateinit var database: TodoDatabase
    private lateinit var dao: TagDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TodoDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.getTagDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun insertTag() = runBlockingTest {
        val tag = Tag(0, "Personal", Color.RED)
        dao.insertTag(tag)
        val allTags = dao.getAllTags().asLiveData().getOrAwaitValue()
        assert(allTags.isNotEmpty())
    }
}