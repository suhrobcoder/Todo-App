package uz.suhrob.todoapp.data.model

import uz.suhrob.todoapp.util.FIRST_ITEM_TYPE
import uz.suhrob.todoapp.util.SECOND_ITEM_TYPE

data class TwoItemType<F, S>(
    val id: Int,
    val first: F? = null,
    val second: S? = null
) {
    fun getItemType(): Int = if (first != null) {
            FIRST_ITEM_TYPE
        } else {
            SECOND_ITEM_TYPE
        }

    fun areContentsTheSame(other: TwoItemType<F, S>): Boolean {
        if (getItemType() != other.getItemType()) {
            return false
        }
        return if (getItemType() == FIRST_ITEM_TYPE) {
            first == other.first
        } else {
            second == other.second
        }
    }
}