package uz.suhrob.todoapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pref: TodoPreferences,
    private val tagRepository: TagRepository,
    private val todoRepository: TodoRepository,
    private val noteRepository: NoteRepository,
    private val checkListRepository: CheckListRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {
    val allTags = tagRepository.getAllTags().asSharedFlow(viewModelScope)
    val allTodos = todoRepository.getAllTodos().asSharedFlow(viewModelScope)
    val allCheckListAndNotes = noteRepository.getAllNotes()
        .combine(checkListRepository.getAllCheckLists()) { notes, checkListWithItems ->
            val list = ArrayList<TwoItemType<Note, CheckList>>()
            val checkLists =
                checkListWithItems.map { it.checkList.apply { items = it.checkListItems } }
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
        }.asSharedFlow(viewModelScope)
    val userName = profileRepository.getUserName().asSharedFlow(viewModelScope)
    val userEmail = profileRepository.getUserEmail().asSharedFlow(viewModelScope)
    val userProfilePicture = profileRepository.getProfilePicture().asSharedFlow(viewModelScope)
    val createdTasksCount = profileRepository.getCreatedTasksCount().asSharedFlow(viewModelScope)
    val completedTasksCount = profileRepository.getCompletedTasks().asSharedFlow(viewModelScope)

    private var _uploadPictureState = MutableSharedFlow<Resource<String>>()
    val uploadPictureState: SharedFlow<Resource<String>> get() = _uploadPictureState

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
            _uploadPictureState.emit(it)
        }.launchIn(viewModelScope)
    }

    fun retryUploadProfilePicture() {
        profileRepository.uploadPicture(latestProfilePicture!!).onEach {
            _uploadPictureState.emit(it)
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

fun <T> Flow<T>.asSharedFlow(scope: CoroutineScope): SharedFlow<T> {
    return shareIn(scope, SharingStarted.WhileSubscribed())
}