package uz.suhrob.todoapp.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.suhrob.todoapp.data.database.entity.Tag
import uz.suhrob.todoapp.data.database.entity.TagWithTasksCount
import uz.suhrob.todoapp.databinding.AddBtnLayoutBinding
import uz.suhrob.todoapp.databinding.TagItemLayoutBinding
import uz.suhrob.todoapp.ui.base.BaseViewHolder
import javax.inject.Inject

class TagAdapter @Inject constructor() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items: List<TagWithTasksCount>
        get() = differ.currentList
    private val diffCallback = object : DiffUtil.ItemCallback<TagWithTasksCount>() {
        override fun areItemsTheSame(oldItem: TagWithTasksCount, newItem: TagWithTasksCount): Boolean = oldItem.tag.id == newItem.tag.id

        override fun areContentsTheSame(oldItem: TagWithTasksCount, newItem: TagWithTasksCount): Boolean = oldItem == newItem
    }
    private val differ = AsyncListDiffer(this, diffCallback)
    var tagListener: ((Tag) -> Unit)? = null
    var addListener: (() -> Unit)? = null

    fun submitList(list: List<TagWithTasksCount>) {
            differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            AddBtnViewHolder(AddBtnLayoutBinding.inflate(LayoutInflater.from(parent.context)))
        } else {
            TagViewHolder(TagItemLayoutBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TagViewHolder) {
            holder.bind(items[position])
        }
    }

    override fun getItemCount(): Int = items.size + 1

    override fun getItemViewType(position: Int): Int = if (position == items.size) 0 else 1

    inner class TagViewHolder(private val binding: TagItemLayoutBinding) :
        BaseViewHolder<TagWithTasksCount>(binding) {
        init {
            binding.root.setOnClickListener {
                tagListener?.invoke(items[adapterPosition].tag)
            }
        }

        @SuppressLint("SetTextI18n")
        override fun bind(item: TagWithTasksCount?) {
            item?.let {
                val tag = it.tag
                if (tag.title != "Empty") {
                    binding.tagIndicator.setColor(tag.color)
                    binding.tagTitle.text = tag.title
                    binding.tasksCount.text = "${it.tasksCount} ${if (it.tasksCount > 1) "tasks" else "task"}"
                } else {
                    binding.root.visibility = View.GONE
                }
            }
        }
    }

    inner class AddBtnViewHolder(binding: AddBtnLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                addListener?.invoke()
            }
        }
    }
}