package uz.suhrob.todoapp.data.repository.todo

import kotlinx.coroutines.flow.Flow
import uz.suhrob.todoapp.data.database.entity.Todo

interface TodoRepository {
    suspend fun insertTodo(todo: Todo)
    suspend fun updateTodo(todo: Todo)
    suspend fun deleteTodo(todo: Todo)
    fun getAllTodos(): Flow<List<Todo>>
}