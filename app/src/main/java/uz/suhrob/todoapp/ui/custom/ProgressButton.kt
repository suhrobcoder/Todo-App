package uz.suhrob.todoapp.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import uz.suhrob.todoapp.R


class ProgressButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private val progressText: TextView
    private val progressBar: ProgressBar

    init {
        inflate(context, R.layout.progress_button, this)
        progressText = findViewById(R.id.progress_button_text)
        progressBar = findViewById(R.id.progress_button_progressbar)
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ProgressButton)
            val text = typedArray.getString(R.styleable.ProgressButton_progress_text)
            progressText.text = text
            typedArray.recycle()
        }
    }

    fun setLoading(isLoading: Boolean) {
        isEnabled = !isLoading
        progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        progressText.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
    }
}