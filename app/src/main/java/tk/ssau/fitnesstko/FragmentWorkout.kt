package tk.ssau.fitnesstko

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.databinding.WorkoutBinding
import tk.ssau.fitnesstko.model.dto.PlannedWorkoutDto
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FragmentWorkout : Fragment(R.layout.workout) {

    private lateinit var binding: WorkoutBinding
    private lateinit var prefs: PreferencesManager
    private val currentCalendar: Calendar = Calendar.getInstance()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private var plannedWorkoutsCall: Call<List<PlannedWorkoutDto>>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WorkoutBinding.bind(view)
        prefs = (activity as MainActivity).prefs

        currentCalendar.timeInMillis = System.currentTimeMillis()

        initializeChart()
        setupCalendar()
        setupNavigation()
        setupPlanWorkoutButton()
        refreshCalendarData()
    }

    private fun initializeChart() {
        with(binding.weightChart) {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            legend.isEnabled = false

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                textColor = Color.WHITE
                gridLineWidth = 2f
                axisLineWidth = 2f


                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return try {
                            timeFormat.format(Date(value.toLong())) + "\n" +
                                    dateFormat.format(Date(value.toLong()))
                        } catch (_: Exception) {
                            ""
                        }
                    }
                }
                labelRotationAngle = -45f
            }

            axisLeft.apply {
                gridLineWidth = 2f
                axisLineWidth = 2f
                extraBottomOffset = 15f
                textColor = Color.WHITE
                granularity = 0.5f
                axisMinimum = 30f
                axisMaximum = 150f

            }

            axisRight.isEnabled = false
        }
        refreshChartData()
    }

    private fun refreshChartData() {
        val allWeights = prefs.getWeights().sortedBy { it.first }

        if (allWeights.isEmpty()) {
            binding.weightChart.clear()
            return
        }

        val displayedWeights = if (allWeights.size <= 6) {
            allWeights
        } else {
            allWeights.takeLast(6)
        }

        val entries = displayedWeights.mapIndexed { index, (timestamp, weight) ->
            Entry(index.toFloat(), weight).apply {
                data = timestamp
            }
        }

        val dataSet = LineDataSet(entries, "Вес (кг)").apply {
            color = "#ffffff".toColorInt()
            valueTextColor = Color.WHITE
            lineWidth = 5f
            valueTextSize = 10f
            setDrawCircles(true)
            setCircleColor("#9BFF20".toColorInt())
            circleHoleColor = "#9BFF20".toColorInt()
            circleRadius = 6f
            mode = LineDataSet.Mode.LINEAR
            setDrawValues(true)
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "%.1f кг".format(value)
                }
            }
        }

        binding.weightChart.apply {
            data = LineData(dataSet)
            xAxis.apply {
                valueFormatter = object : ValueFormatter() {
                    private val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
                    override fun getFormattedValue(value: Float): String {
                        val timestamp =
                            displayedWeights.getOrNull(value.toInt())?.first ?: return ""
                        return dateFormat.format(Date(timestamp))
                    }
                }
                labelCount = 6
                granularity = 1f
                axisMinimum = -0.5f
                axisMaximum = 5.5f
            }
            marker = CustomMarkerView(requireContext(), R.layout.chart_marker)
            animateXY(500, 500)
            invalidate()
        }
    }

    private fun setupCalendar() {
        binding.calendarGrid.removeAllViews()
        updateMonthHeader()
        setupDaysOfWeekHeader()
        fillCalendarDays()
        binding.weightChart.invalidate()
    }

    private fun updateMonthHeader() {
        binding.tvMonth.text = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
            .format(currentCalendar.time)
    }

    private fun setupDaysOfWeekHeader() {
        listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс").forEach { day ->
            DayView(requireContext()).apply {
                text = day
                setTextColor(Color.WHITE)
                addToGrid()
            }
        }
    }

    private fun fillCalendarDays() {
        val tempCalendar = currentCalendar.clone() as Calendar
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1)

        repeat(calculateOffset(tempCalendar.get(Calendar.DAY_OF_WEEK))) { addEmptyView() }

        (1..tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)).forEach { day ->
            DayView(requireContext()).apply {
                text = day.toString()
                state = calculateDayState(day)
                setTextColor(Color.WHITE)
                setOnClickListener { handleDayClick(day) }
                addToGrid()
            }
        }
    }

    private fun calculateOffset(firstDayOfWeek: Int): Int {
        return when (firstDayOfWeek) {
            Calendar.MONDAY -> 0
            else -> (firstDayOfWeek - Calendar.MONDAY + 7) % 7
        }
    }

    private fun calculateDayState(day: Int): DayView.WorkoutState {
        val workoutDate = currentCalendar.clone() as Calendar
        workoutDate.set(Calendar.DAY_OF_MONTH, day)
        workoutDate.normalizeDate()

        return when {
            workoutDate.before(today()) && hasWorkout(workoutDate) -> DayView.WorkoutState.COMPLETED
            workoutDate == today() && hasWorkout(workoutDate) -> DayView.WorkoutState.TODAY
            workoutDate.after(today()) && hasWorkout(workoutDate) -> DayView.WorkoutState.PLANNED
            else -> DayView.WorkoutState.NONE
        }
    }

    private fun today(): Calendar = Calendar.getInstance().apply { normalizeDate() }

    private fun Calendar.normalizeDate() {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    private fun hasWorkout(date: Calendar): Boolean {
        return prefs.getPlannedWorkouts().any { it.date == dateFormat.format(date.time) }
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

    private fun DayView.addToGrid() {
        binding.calendarGrid.addView(this, GridLayout.LayoutParams().apply {
            width = 0
            height = GridLayout.LayoutParams.WRAP_CONTENT
            columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
        })
    }

    private fun setupPlanWorkoutButton() {
        binding.btnPlanWorkout.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, ScheduleWorkoutFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setupNavigation() {
        binding.btnPrevMonth.setOnClickListener { changeMonth(-1) }
        binding.btnNextMonth.setOnClickListener { changeMonth(1) }
    }

    private fun changeMonth(monthsToAdd: Int) {
        currentCalendar.add(Calendar.MONTH, monthsToAdd)
        currentCalendar.set(Calendar.DAY_OF_MONTH, 1)
        refreshCalendarData()
    }

    private fun refreshCalendarData() {
        // Создаем копии календаря для расчетов
        val tempFromCalendar = currentCalendar.clone() as Calendar
        tempFromCalendar.set(Calendar.DAY_OF_MONTH, 1)
        val from = dateFormat.format(tempFromCalendar.time)

        val tempToCalendar = currentCalendar.clone() as Calendar
        tempToCalendar.add(Calendar.MONTH, 1)
        val to = dateFormat.format(tempToCalendar.time)

        plannedWorkoutsCall = ApiService.workoutService.getPlannedWorkouts(
            AuthManager(requireContext()).getUserId(),
            from,
            to
        )

        plannedWorkoutsCall?.enqueue(object : Callback<List<PlannedWorkoutDto>> {
            override fun onResponse(
                call: Call<List<PlannedWorkoutDto>>,
                response: Response<List<PlannedWorkoutDto>>
            ) {
                response.body()?.let {
                    prefs.savePlannedWorkouts(it)
                    setupCalendar() // Обновляем отображение
                }
            }

            override fun onFailure(call: Call<List<PlannedWorkoutDto>>, t: Throwable) {
                Toast.makeText(context, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleDayClick(day: Int) {
        currentCalendar.set(Calendar.DAY_OF_MONTH, day)
        val date = dateFormat.format(currentCalendar.time)

        prefs.getPlannedWorkouts().find { it.date == date }?.let { plannedWorkout ->
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.flFragment,
                    WorkoutDetailFragment().apply {
                        arguments = Bundle().apply {
                            plannedWorkout.workoutId?.let { putLong("workoutId", it) }
                            putString("date", date)
                        }
                    }
                )
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        plannedWorkoutsCall?.cancel()
    }
}