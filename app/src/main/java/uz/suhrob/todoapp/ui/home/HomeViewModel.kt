package uz.suhrob.todoapp.ui.home

import android.graphics.Bitmap
import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.suhrob.todoapp.data.Resource
import uz.suhrob.todoapp.data.database.entity.*
import uz.suhrob.todoapp.data.model.TwoItemType
import uz.suhrob.todoapp.data.model.User
import uz.suhrob.todoapp.data.pref.TodoPreferences
import uz.suhrob.todoapp.data.repository.checklist.CheckListRepository
import uz.suhrob.todoapp.data.repository.note.NoteRepository
import uz.suhrob.todoapp.data.repository.profile.ProfileRepository
import uz.suhrob.todoapp.data.repository.tag.TagRepository
import uz.suhrob.todoapp.data.repository.todo.TodoRepository

class HomeViewModel @ViewModelInject constructor(
    private val pref: TodoPreferences,
    private val tagRepository: TagRepository,
    private val todoRepository: TodoRepository,
    private val noteRepository: NoteRepository,
    private val checkListRepository: CheckListRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    val allTags = tagRepository.getAllTags().asLiveData()
    val allTodos = todoRepository.getAllTodos().asLiveData()
    val allCheckListAndNotes = noteRepository.getAllNotes()
        .combine(checkListRepository.getAllCheckLists()) { notes, checkListWithItems ->
            val list = ArrayList<TwoItemType<Note, CheckList>>()
            val checkLists = checkListWithItems.map { it.checkList.apply { items = it.checkListItems } }
            val notesSize = notes.size
            val checkListsSize = checkLists.size
            var noteIndex = 0
            var checkListIndex = 0
            while (noteIndex < notesSize && checkListIndex < checkListsSize) {
                val note = notes[noteIndex]
                val checkList = checkLists[checkListIndex]
                if (note.id > checkList.id) {
                    list.add(TwoItemType(checkList.id, second = checkList))
                    checkListIndex++
                } else {
                    list.add(TwoItemType(note.id, first = note))
                    noteIndex++
                }
            }
            while (noteIndex < notesSize) {
                val note = notes[noteIndex++]
                list.add(TwoItemType(note.id, first = note))
            }
            while (checkListIndex < checkListsSize) {
                val checkList = checkLists[checkListIndex++]
                list.add(TwoItemType(checkList.id, second = checkList))
            }
            list.toList()
        }.asLiveData()
    val userName = profileRepository.getUserName().asLiveData()
    val userEmail = profileRepository.getUserEmail().asLiveData()
    val userProfilePicture = profileRepository.getProfilePicture().asLiveData()
    val createdTasksCount = profileRepository.getCreatedTasksCount().asLiveData()
    val completedTasksCount = profileRepository.getCompletedTasks().asLiveData()

    private var _uploadPictureState = MutableLiveData<Resource<String>>()
    val uploadPictureState: LiveData<Resource<String>> get() = _uploadPictureState

    private var latestProfilePicture: ByteArray? = null

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

    fun uploadProfilePicture(arr: ByteArray) {
        latestProfilePicture = arr
        profileRepository.uploadPicture(arr).onEach {
            _uploadPictureState.value = it
        }.launchIn(viewModelScope)
    }

    fun retryUploadProfilePicture() {
        profileRepository.uploadPicture(latestProfilePicture!!).onEach {
            _uploadPictureState.value = it
        }.launchIn(viewModelScope)
    }

    fun removeProfilePicture() = viewModelScope.launch {
        profileRepository.removePicture()
    }

    fun clearAllData() = viewModelScope.launch {
        todoRepository.clearTable()
        tagRepository.clearTable()
        noteRepository.clearTable()
        checkListRepository.clearTables()
        pref.saveUser(User())
    }
}