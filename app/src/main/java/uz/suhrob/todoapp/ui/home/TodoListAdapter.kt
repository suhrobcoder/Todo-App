package uz.suhrob.todoapp.ui.home

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.prolificinteractive.materialcalendarview.CalendarDay
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.data.database.entity.Todo
import uz.suhrob.todoapp.data.model.DateViewType
import uz.suhrob.todoapp.data.model.FilterMode
import uz.suhrob.todoapp.data.model.TwoItemType
import uz.suhrob.todoapp.databinding.DateItemLayoutBinding
import uz.suhrob.todoapp.databinding.TodoItemLayoutBinding
import uz.suhrob.todoapp.ui.base.BaseTwoItemTypeRecyclerAdapter
import uz.suhrob.todoapp.ui.base.BaseViewHolder
import uz.suhrob.todoapp.util.*
import javax.inject.Inject

class TodoListAdapter @Inject constructor() : BaseTwoItemTypeRecyclerAdapter<Todo, DateViewType>() {
    override val differ: AsyncListDiffer<TwoItemType<Todo, DateViewType>> =
        AsyncListDiffer(this, diffCallback)
    private val allTodos = ArrayList<Todo>()
    var editListener: ((Todo) -> Unit)? = null
    var deleteListener: ((Todo) -> Unit)? = null
    var todoClickListener: ((Todo) -> Unit)? = null
    private val days = ArrayList<CalendarDay>()
    private val dayMap = HashMap<CalendarDay, Int>()
    private val viewBinderHelper = ViewBinderHelper().apply { setOpenOnlyOne(true) }
    private var primaryColor: Int = 0
    private var secondaryColor: Int = 0
    private var filterMode = FilterMode.ALL

    fun submitList(todoList: List<Todo>, fromOutside: Boolean = true) {
        if (fromOutside) {
            allTodos.clear()
            allTodos.addAll(todoList)
            filter()
            return
        }
        val list = ArrayList<TwoItemType<Todo, DateViewType>>()
        var lastDate = 0L
        days.clear()
        dayMap.clear()
        for ((position, todo) in todoList.withIndex()) {
            if (todo.dueDate / 86400000 > lastDate) {
                lastDate = todo.dueDate / 86400000
                val day = todo.dueDate.toCalendar().getCalendarDay()
                days.add(day)
                dayMap[day] = position
                list.add(TwoItemType(id = todo.id, second = DateViewType(todo.dueDate)))
            }
            list.add(TwoItemType(todo.id, first = todo))
        }
        differ.submitList(list)
    }

    fun filter(mode: FilterMode = filterMode) {
        when (mode) {
            FilterMode.ALL -> submitList(allTodos, false)
            FilterMode.INCOMPLETE -> submitList(allTodos.filter { todo -> !todo.isDone }, false)
            FilterMode.COMPLETED -> submitList(allTodos.filter { todo -> todo.isDone }, false)
        }
        filterMode = mode
    }

    fun getDays(): List<CalendarDay> = days

    fun getDatePosition(calendarDay: CalendarDay): Int {
        for (entry in dayMap) {
            val day = entry.key
            if (day > calendarDay) {
                return entry.value
            }
        }
        return items.size - 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (primaryColor == 0) {
            primaryColor = ContextCompat.getColor(parent.context, R.color.colorPrimary)
            secondaryColor = ContextCompat.getColor(parent.context, R.color.colorSecondary)
        }
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == FIRST_ITEM_TYPE) {
            TodoViewHolder(TodoItemLayoutBinding.inflate(inflater, parent, false))
        } else {
            DateViewHolder(DateItemLayoutBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TodoViewHolder) {
            viewBinderHelper.bind(holder.binding.swipeLayout, items[position].id.toString())
            holder.bind(items[position].first)
        } else if (holder is DateViewHolder) {
            holder.bind(items[position].second)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is TodoViewHolder) {
            holder.binding.todoItemCheck.setOnCheckedChangeListener(null)
        }
        super.onViewRecycled(holder)
    }

    inner class TodoViewHolder(val binding: TodoItemLayoutBinding) :
        BaseViewHolder<Todo>(binding) {
        private var todoItem: Todo? = null

        init {
            binding.todoItemEdit.setOnClickListener {
                todoItem?.let {
                    editListener?.invoke(it) // TODO add edit screen
                }
            }
            binding.todoItemDelete.setOnClickListener {
                todoItem?.let {
                    deleteListener?.invoke(it)
                }
            }
        }

        override fun bind(item: Todo?) {
            item?.let { todo ->
                todoItem = todo
                binding.todoItemCheck.isChecked = todo.isDone
                binding.todoItemTitle.text = todo.title
                binding.todoItemTime.text = todo.dueDate.toFormattedTime()
                binding.todoItemEndView.setBackgroundColor(if (todo.isDone) primaryColor else secondaryColor)
                binding.todoItemCheck.setOnCheckedChangeListener { _, isChecked ->
                    editListener?.invoke(todo.apply { isDone = isChecked })
                    notifyItemChanged(adapterPosition)
                }
                if (todo.isDone) {
                    binding.todoItemTitle.paintFlags =
                        binding.todoItemTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    binding.todoItemTime.paintFlags =
                        binding.todoItemTime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    binding.todoItemTitle.paintFlags =
                        binding.todoItemTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    binding.todoItemTime.paintFlags =
                        binding.todoItemTime.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
                binding.mainLayout.setOnClickListener {
                    todoClickListener?.invoke(todo)
                }
            }
        }
    }

    inner class DateViewHolder(private val binding: DateItemLayoutBinding) :
        BaseViewHolder<DateViewType>(binding) {
        override fun bind(item: DateViewType?) {
            item?.let { date ->
                binding.todoDateHeaderText.text = date.getDateString()
            }
        }
    }
}