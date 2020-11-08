package uz.suhrob.todoapp.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import uz.suhrob.todoapp.R
import uz.suhrob.todoapp.data.database.entity.CheckListItem

class CheckListItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val itemTitle: TextView
    private val closeBtn: ImageButton
    var closeBtnListener: (() -> Unit)? = null

    init {
        inflate(context, R.layout.checklist_item, this)
        itemTitle = findViewById(R.id.item_title)
        closeBtn = findViewById(R.id.close_btn)
        closeBtn.setOnClickListener { closeBtnListener?.invoke() }
    }

    fun getCheckListItem(): CheckListItem? {
        val title = itemTitle.text.toString()
        if (title.isEmpty()) {
            itemTitle.error = "Title can't be empty"
            return null
        }
        return CheckListItem(title, 0, false)
    }
}