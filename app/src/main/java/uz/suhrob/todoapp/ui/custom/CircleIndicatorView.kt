package uz.suhrob.todoapp.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.ColorUtils
import uz.suhrob.todoapp.R
import kotlin.math.min

class CircleIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mColor: Int = Color.RED
    private val paint1 = Paint()
    private val paint2 = Paint()
    private val path1 = Path()
    private val path2 = Path()
    private var mWidth = 0F
    private var mHeight = 0F

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CircleIndicatorView)
            mColor = typedArray.getColor(R.styleable.ProgressButton_progress_text, Color.RED)
            typedArray.recycle()
        }
        paint1.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        paint2.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    fun setColor(color: Int) {
        mColor = color
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        mWidth = if (widthMode == MeasureSpec.EXACTLY) {
            widthSize.toFloat()
        } else {
            (paddingLeft + paddingRight).toFloat()
        }
        mHeight = if (heightMode == MeasureSpec.EXACTLY) {
            heightSize.toFloat()
        } else {
            (paddingTop + paddingBottom).toFloat()
        }
        setMeasuredDimension(mWidth.toInt(), mHeight.toInt())
        setDrawData()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint1.color = mColor
        paint2.color = ColorUtils.setAlphaComponent(mColor, 100)
        canvas?.drawPath(path2, paint2)
        canvas?.drawPath(path1, paint1)
    }

    private fun setDrawData() {
        path1.reset()
        path1.addCircle(mWidth / 2, mHeight / 2, min(mWidth, mHeight) / 4, Path.Direction.CW)
        path2.reset()
        path2.addCircle(mWidth / 2, mHeight / 2, min(mWidth, mHeight) / 2, Path.Direction.CW)
    }
}