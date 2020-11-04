package uz.suhrob.todoapp.ui.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import uz.suhrob.todoapp.R

class CustomCalendarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val modeToggle: LinearLayout
    private val monthText: TextView
    private val calendarView: MaterialCalendarView
    private val arrowView: ImageView
    var dateSelectedListener: OnDateSelectedListener? = null
    private val months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    private val weekDays = arrayOf("M", "T", "W", "T", "F", "S", "S")

    init {
        inflate(context, R.layout.custom_calendar_view, this)
        modeToggle = findViewById(R.id.custom_calendar_mode_toggle)
        monthText = findViewById(R.id.custom_calendar_month)
        calendarView = findViewById(R.id.custom_calendar)
        arrowView = findViewById(R.id.custom_calendar_arrow)
        modeToggle.setOnClickListener {
            if (calendarView.calendarMode == CalendarMode.MONTHS) {
                calendarView.state()
                    .edit()
                    .setCalendarDisplayMode(CalendarMode.WEEKS)
                    .commit()
                arrowView.setImageResource(R.drawable.ic_arrow_down)
            } else {
                calendarView.state()
                    .edit()
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit()
                arrowView.setImageResource(R.drawable.ic_arrow_up)
            }
        }
        calendarView.apply {
            setOnMonthChangedListener { _, date ->
                monthText.text = months[date.month-1]
            }
            monthText.text = months[currentDate.month-1]
            setOnDateChangedListener(dateSelectedListener)
            topbarVisible = false
            setWeekDayLabels(weekDays)
            state().edit()
                .setCalendarDisplayMode(CalendarMode.WEEKS)
                .commit()
        }
        viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                val mWidth = width
                calendarView.tileHeight = mWidth / 7 * 5 / 8
                Log.d("AppDebug", width.toString())
            }
        })
    }

    fun setDecorateDays(days: List<CalendarDay>) {
        calendarView.removeDecorators()
        calendarView.addDecorator(DotsDecorator(days))
    }

    inner class DotsDecorator(private val days: List<CalendarDay>) : DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay?): Boolean = day in days

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(DotSpan(8F, Color.RED))
        }
    }
}