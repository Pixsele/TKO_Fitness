package tk.ssau.fitnesstko

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import tk.ssau.fitnesstko.databinding.WorkoutBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentWorkout : Fragment(R.layout.workout) {

    private lateinit var binding: WorkoutBinding
    private lateinit var prefs: PreferencesManager
    private val currentCalendar: Calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WorkoutBinding.bind(view)
        prefs = (activity as MainActivity).prefs
        binding.btnPlanWorkout.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, ScheduleWorkoutFragment())
                .addToBackStack(null)
                .commit()
        }
        setupNavigation()
        setupCalendar()
    }

    private fun setupNavigation() {
        binding.btnPrevMonth.setOnClickListener { changeMonth(-1) }
        binding.btnNextMonth.setOnClickListener { changeMonth(1) }
    }

    private fun changeMonth(monthsToAdd: Int) {
        currentCalendar.add(Calendar.MONTH, monthsToAdd)
        setupCalendar()
    }

    private fun setupCalendar() {
        binding.calendarGrid.removeAllViews()
        updateMonthHeader()
        setupDaysOfWeek()
        fillCalendarDays()
    }

    private fun updateMonthHeader() {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        binding.tvMonth.text = dateFormat.format(currentCalendar.time)
    }

    private fun setupDaysOfWeek() {
        val daysOfWeek = listOf("пн", "вт", "ср", "чт", "пт", "сб", "вс")
        daysOfWeek.forEach { day ->
            val dayView = DayView(requireContext()).apply {
                text = day
                setTextAppearance(android.R.style.TextAppearance_Medium)
            }
            addViewToGrid(dayView)
        }
    }

    private fun fillCalendarDays() {
        val tempCalendar = currentCalendar.clone() as Calendar
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1)

        val firstDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK)
        val offset = calculateOffset(firstDayOfWeek)

        repeat(offset) { addEmptyView() }

        val maxDay = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (day in 1..maxDay) {
            val dayView = createDayView(day)
            addViewToGrid(dayView)
        }
    }

    private fun calculateOffset(firstDayOfWeek: Int): Int {
        return when (firstDayOfWeek) {
            Calendar.MONDAY -> 0
            else -> (firstDayOfWeek - Calendar.MONDAY + 7) % 7
        }
    }

    private fun createDayView(day: Int): DayView {
        return DayView(requireContext()).apply {
            text = day.toString()
            state = calculateDayState(day)
            setOnClickListener { handleDayClick(day) }
        }
    }

    private fun calculateDayState(day: Int): DayView.WorkoutState {
        val workoutDate = currentCalendar.clone() as Calendar
        workoutDate.set(Calendar.DAY_OF_MONTH, day)

        return when {
            isWorkoutCompleted(workoutDate) -> DayView.WorkoutState.COMPLETED
            isToday(workoutDate) -> DayView.WorkoutState.TODAY
            isWorkoutPlanned(workoutDate) -> DayView.WorkoutState.PLANNED
            else -> DayView.WorkoutState.NONE
        }
    }

    private fun isWorkoutCompleted(date: Calendar): Boolean {
        val formattedDate = formatDate(date)
        return date.before(Calendar.getInstance()) && prefs.getWorkouts().contains(formattedDate)
    }

    private fun isToday(date: Calendar): Boolean {
        val today = Calendar.getInstance()
        return date.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) &&
                date.get(Calendar.YEAR) == today.get(Calendar.YEAR)
    }

    private fun isWorkoutPlanned(date: Calendar): Boolean {
        return prefs.getWorkouts().contains(formatDate(date))
    }

    private fun formatDate(calendar: Calendar): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

    private fun addEmptyView() {
        binding.calendarGrid.addView(View(context).apply {
            layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
        })
    }

    private fun addViewToGrid(view: View) {
        val params = GridLayout.LayoutParams().apply {
            width = 0
            height = GridLayout.LayoutParams.WRAP_CONTENT
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            setMargins(4, 4, 4, 4)
        }
        binding.calendarGrid.addView(view, params)
    }

    private fun handleDayClick(day: Int) {
        // Обработка клика по дню
    }

    class DayView(context: android.content.Context) :
        androidx.appcompat.widget.AppCompatTextView(context) {

        enum class WorkoutState { NONE, PLANNED, TODAY, COMPLETED }

        var state: WorkoutState = WorkoutState.NONE
            set(value) {
                field = value
                updateAppearance()
            }

        private fun updateAppearance() {
            when (state) {
                WorkoutState.PLANNED -> setBackgroundWithDashedBorder()
                WorkoutState.TODAY -> setSolidBackground(Color.GREEN)
                WorkoutState.COMPLETED -> setSolidBackground(Color.GRAY)
                else -> background = null
            }
        }

        private fun setBackgroundWithDashedBorder() {
            val shape = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setStroke(2, Color.BLACK, 5f, 10f)
            }
            background = shape
        }

        private fun setSolidBackground(color: Int) {
            val shape = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(color)
            }
            background = shape
        }
    }
}