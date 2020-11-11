package uz.suhrob.todoapp.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import com.google.android.material.animation.ArgbEvaluatorCompat
import uz.suhrob.todoapp.util.parseColor

class OnboardingWaveView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mWidth = 0F
    private var mHeight = 0F
    private val paint1 = Paint()
    private val paint2 = Paint()
    private val path1 = Path()
    private val path2 = Path()
    @ColorInt
    private var color: Int = Color.BLUE
    private val colors = arrayOf("#F96160", "#6074F9", "#8561F9")

    init {
        paint1.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        paint2.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }
    }

    fun setScroll(position: Int, positionOffset: Float) {
        val evaluator = ArgbEvaluatorCompat()
        color = if (position == 2) {
            colors[2].parseColor()
        } else {
            evaluator.evaluate(
                positionOffset,
                colors[position].parseColor(),
                colors[position + 1].parseColor()
            )
        }
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
        paint1.color = color
        paint2.color = ColorUtils.setAlphaComponent(color, 100)
        canvas?.drawPath(path2, paint2)
        canvas?.drawPath(path1, paint1)
    }

    private fun setDrawData() {
        path1.reset()
        path1.moveTo(0F, 120F)
        path1.quadTo(mWidth / 4, 200F, mWidth / 2, 100F)
        path1.quadTo(mWidth * 3 / 4, 0F, mWidth, 100F)
        path1.lineTo(mWidth, mHeight)
        path1.lineTo(0F, mHeight)
        path1.close()
        path2.reset()
        path2.moveTo(0F, 100F)
        path2.quadTo(mWidth / 4, 20F, mWidth / 5 * 3, 200F)
        path2.lineTo(0F, 200F)
        path2.close()
    }
}