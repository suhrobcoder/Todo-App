package uz.suhrob.todoapp.ui.custom

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import uz.suhrob.todoapp.R

class ColorPickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private val pickerList: List<ImageView>
    private val colorList: List<Int>
    var selectedColor: Int

    init {
        inflate(context, R.layout.color_picker_layout, this)
        pickerList = listOf(
            findViewById(R.id.color_picker1),
            findViewById(R.id.color_picker2),
            findViewById(R.id.color_picker3),
            findViewById(R.id.color_picker4),
            findViewById(R.id.color_picker5)
        )
        colorList = listOf(
            ContextCompat.getColor(context, R.color.color_picker1),
            ContextCompat.getColor(context, R.color.color_picker2),
            ContextCompat.getColor(context, R.color.color_picker3),
            ContextCompat.getColor(context, R.color.color_picker4),
            ContextCompat.getColor(context, R.color.color_picker5)
        )
        selectedColor = colorList[0]
        for (i in pickerList.indices) {
            val drawable = GradientDrawable()
            drawable.cornerRadius = 16F
            drawable.setColor(colorList[i])
            pickerList[i].background = drawable
            pickerList[i].setOnClickListener(this)
        }
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                val itemSize = width / 29 * 5
                val itemMargin = width / 58
                for (i in pickerList.indices) {
                    val layoutParams = LayoutParams(itemSize, itemSize)
                    layoutParams.setMargins(
                        if (i > 0) itemMargin else 0,
                        0,
                        if (i < 4) itemMargin else 0,
                        0
                    )
                    pickerList[i].layoutParams = layoutParams
                    pickerList[i].requestLayout()
                }
            }
        })
    }

    override fun onClick(v: View?) {
        for (picker in pickerList) {
            picker.setImageResource(R.drawable.transparent_icon)
        }
        when (v?.id) {
            R.id.color_picker1 -> pickerClicked(0)
            R.id.color_picker2 -> pickerClicked(1)
            R.id.color_picker3 -> pickerClicked(2)
            R.id.color_picker4 -> pickerClicked(3)
            R.id.color_picker5 -> pickerClicked(4)
        }
    }

    private fun pickerClicked(pos: Int) {
        pickerList[pos].setImageResource(R.drawable.ic_picker_check)
        selectedColor = colorList[pos]
    }
}