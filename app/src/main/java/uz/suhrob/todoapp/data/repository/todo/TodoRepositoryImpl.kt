package uz.suhrob.todoapp.data.repository.todo

import kotlinx.coroutines.flow.Flow
import uz.suhrob.todoapp.data.database.dao.TodoDao
import uz.suhrob.todoapp.data.database.entity.Todo

class TodoRepositoryImpl(
    private val todoDao: TodoDao
): TodoRepository {
    override suspend fun insertTodo(todo: Todo) = todoDao.insertTodo(todo)

    override suspend fun updateTodo(todo: Todo) = todoDao.updateTodo(todo)

    override suspend fun deleteTodo(todo: Todo) = todoDao.deleteTodo(todo)

    override fun getAllTodos(): Flow<List<Todo>> = todoDao.getAllTodos()

    override suspend fun clearTable() = todoDao.clearTable()
}