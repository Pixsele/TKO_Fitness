package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.model.dto.PlannedWorkoutDto
import tk.ssau.fitnesstko.model.dto.WorkoutForPageDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScheduleWorkoutFragment : Fragment(R.layout.fragment_schedule_workout) {

    private var selectedDate: Long? = null
    private lateinit var prefs: PreferencesManager
    private var workoutsList = emptyList<WorkoutForPageDto>()
    private var currentCall: Call<PagedResponse<WorkoutForPageDto>>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = (activity as MainActivity).prefs

        setupDatePicker(view)
        setupWorkoutSpinner(view)
        setupSaveButton(view)
    }

    private fun setupDatePicker(view: View) {
        val etDate = view.findViewById<TextInputEditText>(R.id.etDate)
        etDate.setOnClickListener {
            val constraintsBuilder = CalendarConstraints.Builder()
                .setStart(MaterialDatePicker.todayInUtcMilliseconds())

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Выберите дату")
                .setCalendarConstraints(constraintsBuilder.build())
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { timestamp ->
                selectedDate = timestamp
                etDate.setText(formatDate(timestamp))
            }

            datePicker.show(parentFragmentManager, "DATE_PICKER_TAG")
        }
    }

    private fun setupWorkoutSpinner(view: View) {
        currentCall = ApiService.workoutService.getWorkouts()
        currentCall?.enqueue(object : Callback<PagedResponse<WorkoutForPageDto>> {
            override fun onResponse(
                call: Call<PagedResponse<WorkoutForPageDto>>,
                response: Response<PagedResponse<WorkoutForPageDto>>
            ) {
                if (response.isSuccessful && isAdded) {
                    response.body()?.content?.let { workouts ->
                        workoutsList = workouts
                        val workoutNames = workouts.map { it.name }

                        val adapter = ArrayAdapter(
                            requireContext(),
                            R.layout.spiner_white,
                            workoutNames
                        )

                        val actvWorkout = view.findViewById<AutoCompleteTextView>(R.id.actvWorkout)
                        actvWorkout.setAdapter(adapter)
                    }
                }
            }

            override fun onFailure(call: Call<PagedResponse<WorkoutForPageDto>>, t: Throwable) {
                if (isAdded) {
                    showToast("Ошибка загрузки списка тренировок")
                }
            }
        })
    }

    private fun setupSaveButton(view: View) {
        view.findViewById<Button>(R.id.btnSave).setOnClickListener {
            if (validateInput()) {
                saveWorkout()
            }
        }
    }

    private fun validateInput(): Boolean {
        return when {
            selectedDate == null -> {
                showToast("Выберите дату тренировки")
                false
            }

            view?.findViewById<AutoCompleteTextView>(R.id.actvWorkout)?.text.isNullOrEmpty() -> {
                showToast("Выберите тип тренировки")
                false
            }

            else -> true
        }
    }

    private fun saveWorkout() {
        val selectedWorkoutName = view?.findViewById<AutoCompleteTextView>(R.id.actvWorkout)?.text.toString()
        val selectedWorkout = workoutsList.find { it.name == selectedWorkoutName }

        selectedWorkout?.let { workout ->
            val plannedWorkout = PlannedWorkoutDto(
                id = null,
                userId = AuthManager(requireContext()).getUserId(),
                workoutId = workout.id,
                date = formatDate(selectedDate!!),
                status = "PLANNED"
            )

            ApiService.workoutService.createPlannedWorkout(plannedWorkout)
                .enqueue(object : Callback<PlannedWorkoutDto> {
                    override fun onResponse(
                        call: Call<PlannedWorkoutDto>,
                        response: Response<PlannedWorkoutDto>
                    ) {
                        if (response.isSuccessful && isAdded) {
                            showToast("Тренировка запланирована")
                            parentFragmentManager.popBackStack()
                        }
                    }

                    override fun onFailure(call: Call<PlannedWorkoutDto>, t: Throwable) {
                        if (isAdded) {
                            showToast("Ошибка сохранения")
                        }
                    }
                })
        } ?: showToast("Тренировка не найдена")
    }

    private fun formatDate(timestamp: Long): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(timestamp))
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentCall?.cancel()
    }
}