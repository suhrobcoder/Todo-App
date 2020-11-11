package uz.suhrob.todoapp.ui.home.adapters

import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.data.database.entity.CheckList
import uz.suhrob.todoapp.data.database.entity.CheckListItem
import uz.suhrob.todoapp.data.database.entity.Note
import uz.suhrob.todoapp.data.model.TwoItemType
import uz.suhrob.todoapp.databinding.CheckListItemLayoutBinding
import uz.suhrob.todoapp.databinding.NoteItemLayoutBinding
import uz.suhrob.todoapp.ui.base.BaseTwoItemTypeRecyclerAdapter
import uz.suhrob.todoapp.ui.base.BaseViewHolder
import uz.suhrob.todoapp.util.FIRST_ITEM_TYPE
import javax.inject.Inject

class NotesAndCheckListsAdapter @Inject constructor() :
    BaseTwoItemTypeRecyclerAdapter<Note, CheckList>() {
    override val differ: AsyncListDiffer<TwoItemType<Note, CheckList>> =
        AsyncListDiffer(this, diffCallback)
    var editListener: ((CheckListItem) -> Unit)? = null

    fun submitList(notesAndCheckLists: List<TwoItemType<Note, CheckList>>) {
        differ.submitList(notesAndCheckLists)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == FIRST_ITEM_TYPE) {
            NoteViewHolder(
                NoteItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            CheckListViewHolder(
                CheckListItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NoteViewHolder -> holder.bind(items[position].first)
            is CheckListViewHolder -> holder.bind(items[position].second)
        }
    }

    inner class NoteViewHolder(private val binding: NoteItemLayoutBinding) :
        BaseViewHolder<Note>(binding) {
        override fun bind(item: Note?) {
            item?.let { note ->
                binding.noteColor.setBackgroundColor(note.color)
                binding.noteDescription.text = note.description
            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        if (holder is CheckListViewHolder) {
            for (checkBox in holder.checkListItems) {
                checkBox.setOnClickListener(null)
            }
        }
        super.onViewRecycled(holder)
    }

    inner class CheckListViewHolder(private val binding: CheckListItemLayoutBinding) :
        BaseViewHolder<CheckList>(binding) {
        private var checkListItemsAdded = false
        val checkListItems = arrayListOf<CheckBox>()

        override fun bind(item: CheckList?) {
            item?.let { checkList ->
                binding.checkListColor.setBackgroundColor(checkList.color)
                binding.checkListTitle.text = checkList.title
                if (checkListItemsAdded) {
                    return@let
                }
                Log.d("AppDebug", checkList.toString())
                for (checkListItem in checkList.items) {
                    Log.d("AppDebug", checkListItem.toString())
                    val checkBox = CheckBox(binding.root.context).apply {
                        text = checkListItem.title
                        typeface = Typeface.create(
                            ResourcesCompat.getFont(context, R.font.avenir_medium),
                            Typeface.NORMAL
                        )
                        textSize = 24F
                        isChecked = checkListItem.checked
                        paintFlags = if (checkListItem.checked) {
                            paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        } else {
                            paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                        }
                        setButtonDrawable(R.drawable.checklist_item_selector)
                        setOnCheckedChangeListener(null)
                        setOnCheckedChangeListener { _, isChecked ->
                            editListener?.invoke(checkListItem.apply { checked = isChecked })
                            paintFlags = if (isChecked) {
                                paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                            } else {
                                paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                            }
                        }
                    }
                    checkListItems.add(checkBox)
                    binding.checkListRoot.addView(checkBox)
                }
                checkListItemsAdded = true
            }
        }
    }
}