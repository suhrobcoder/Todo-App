package uz.suhrob.todoapp.data.model

import org.junit.Test
import uz.suhrob.todoapp.data.database.entity.Note
import uz.suhrob.todoapp.data.database.entity.Todo
import uz.suhrob.todoapp.util.FIRST_ITEM_TYPE
import uz.suhrob.todoapp.util.SECOND_ITEM_TYPE

class TwoItemTypeTest {

    @Test
    fun getItemType() {
        val note = Note(1, "desc", 0)
        val todo = Todo(1, "title", "desc", 0, 1)
        val noteType = TwoItemType<Note, Todo>(
            note.id,
            first = note
        )
        val todoType = TwoItemType<Note, Todo>(
            todo.id,
            second = todo
        )
        assert(noteType.getItemType() == FIRST_ITEM_TYPE && todoType.getItemType() == SECOND_ITEM_TYPE)
    }

    @Test
    fun areContentsTheSame() {
        val note1 = Note(1, "desc", 0)
        val note2 = Note(1, "desc", 0)
        val note3 = Note(1, "desc1", 0)
        val todo = Todo(1, "title", "desc", 0, 1)
        val note1Type = TwoItemType<Note, Todo>(
            note1.id,
            first = note1
        )
        val note2Type = TwoItemType<Note, Todo>(
            note2.id,
            first = note2
        )
        val note3Type = TwoItemType<Note, Todo>(
            note3.id,
            first = note3
        )
        val todoType = TwoItemType<Note, Todo>(
            todo.id,
            second = todo
        )
        assert(
            note1Type.areContentsTheSame(note2Type)
                    && !note1Type.areContentsTheSame(todoType)
                    && !note1Type.areContentsTheSame(note3Type)
        )
    }
}