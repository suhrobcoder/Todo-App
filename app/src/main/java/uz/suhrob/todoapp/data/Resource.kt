package uz.suhrob.todoapp.data

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    class Success<T>(val data: T) : Resource<T>()
    class Error(val error: String) : Resource<Nothing>()
}