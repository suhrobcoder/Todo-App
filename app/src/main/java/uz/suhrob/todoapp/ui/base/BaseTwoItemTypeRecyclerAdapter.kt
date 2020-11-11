package uz.suhrob.todoapp.ui.base

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.suhrob.todoapp.data.model.TwoItemType

abstract class BaseTwoItemTypeRecyclerAdapter<F, S> :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected val diffCallback = object : DiffUtil.ItemCallback<TwoItemType<F, S>>() {
        override fun areItemsTheSame(
            oldItem: TwoItemType<F, S>,
            newItem: TwoItemType<F, S>
        ): Boolean =
            if (oldItem.getItemType() != newItem.getItemType()) {
                false
            } else {
                oldItem.id == newItem.id
            }

        override fun areContentsTheSame(
            oldItem: TwoItemType<F, S>,
            newItem: TwoItemType<F, S>
        ): Boolean = oldItem.areContentsTheSame(newItem)
    }

    abstract val differ: AsyncListDiffer<TwoItemType<F, S>>
    protected val items: List<TwoItemType<F, S>>
        get() = differ.currentList

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = items[position].getItemType()
}