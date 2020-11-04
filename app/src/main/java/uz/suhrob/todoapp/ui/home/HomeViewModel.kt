package uz.suhrob.todoapp.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uz.suhrob.todoapp.data.database.entity.*
import uz.suhrob.todoapp.data.repository.checklist.CheckListRepository
import uz.suhrob.todoapp.data.repository.note.NoteRepository
import uz.suhrob.todoapp.data.repository.tag.TagRepository
import uz.suhrob.todoapp.data.repository.todo.TodoRepository

class HomeViewModel @ViewModelInject constructor(
    private val tagRepository: TagRepository,
    private val todoRepository: TodoRepository,
    private val noteRepository: NoteRepository,
    private val checkListRepository: CheckListRepository
) : ViewModel() {
    val allTags get() = tagRepository.getAllTags().asLiveData()
    val allTodos get() = todoRepository.getAllTodos().asLiveData()
    val allNotes get() = noteRepository.getAllNotes().asLiveData()
    val allCheckLists get() = checkListRepository.getAllCheckLists().asLiveData()

    fun newTag(tag: Tag) = viewModelScope.launch {
        tagRepository.insertTag(tag)
    }

    fun updateTag(tag: Tag) = viewModelScope.launch {
        tagRepository.insertTag(tag)
    }

    fun newTodo(todo: Todo) = viewModelScope.launch {
        todoRepository.insertTodo(todo)
    }

    fun updateTodo(todo: Todo) = viewModelScope.launch {
        todoRepository.updateTodo(todo)
    }

    fun deleteTodo(todo: Todo) = viewModelScope.launch {
        todoRepository.deleteTodo(todo)
    }

    fun newNote(note: Note) = viewModelScope.launch {
        noteRepository.insertNote(note)
    }

    fun newCheckList(checkListWithItems: CheckListWithItems) = viewModelScope.launch {
        checkListRepository.insertCheckList(checkListWithItems)
    }

    fun updateCheckListItem(item: CheckListItem) = viewModelScope.launch {
        checkListRepository.updateCheckListItem(item)
    }
}