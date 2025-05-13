package tk.ssau.fitnesstko

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import tk.ssau.fitnesstko.databinding.FragmentCreateWorkoutBinding
import tk.ssau.fitnesstko.model.dto.WorkoutDto
import tk.ssau.fitnesstko.model.dto.WorkoutExerciseDto

class CreateWorkoutFragment : Fragment() {

    private var _binding: FragmentCreateWorkoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SharedViewModel
    private var difficulty: String = "MEDIUM"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        setupUI()
        setupObservers()
        setupDifficultyGroup()
    }

    private fun setupUI() {
        binding.btnAddExercise.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .add(R.id.flFragment, ExercisePickerFragment())
                .addToBackStack("create_workout")
                .commit()
        }

        binding.btnSave.setOnClickListener {
            if (validateForm()) createWorkout()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        viewModel.selectedExercises.observe(viewLifecycleOwner) { exercises ->
            binding.containerExercises.removeAllViews()
            exercises.forEach { exercise ->
                val view = layoutInflater.inflate(
                    R.layout.item_selected_exercise_params,
                    binding.containerExercises,
                    false
                ).apply {
                    findViewById<TextView>(R.id.tvExerciseName).text = exercise.exercise.name
                    findViewById<TextView>(R.id.tvParams).text =
                        "Подходы: ${exercise.sets}, Повторения: ${exercise.reps}"
                }
                binding.containerExercises.addView(view)
            }
        }
    }

    private fun setupDifficultyGroup() {
        binding.rgDifficulty.setOnCheckedChangeListener { _, checkedId ->
            difficulty = when (checkedId) {
                R.id.rbEasy -> "EASY"
                R.id.rbMedium -> "MEDIUM"
                R.id.rbHard -> "HARD"
                else -> "MEDIUM"
            }
        }
    }

    private fun validateForm(): Boolean {
        return when {
            binding.etWorkoutName.text.isNullOrEmpty() -> {
                showError("Введите название тренировки")
                false
            }

            viewModel.selectedExercises.value.isNullOrEmpty() -> {
                showError("Добавьте хотя бы одно упражнение")
                false
            }

            else -> true
        }
    }

    private fun createWorkout() {
        lifecycleScope.launch {
            try {
                val workoutResponse = withContext(Dispatchers.IO) {
                    ApiService.workoutService.createWorkout(
                        WorkoutDto(
                            name = binding.etWorkoutName.text.toString(),
                            description = binding.etDescription.text.toString(),
                            difficult = difficulty,
                            likeCount = null,
                            id = null
                        )
                    ).execute()
                }

                if (workoutResponse.isSuccessful) {
                    workoutResponse.body()?.id?.let { workoutId ->
                        linkExercises(workoutId)
                    } ?: showError("Ошибка создания тренировки")
                } else {
                    showError("Ошибка сервера: ${workoutResponse.code()}")
                }
            } catch (e: Exception) {
                showError("Сетевая ошибка: ${e.message}")
            }
        }
    }

    private suspend fun linkExercises(workoutId: Long) {
        withContext(Dispatchers.IO) {
            try {
                val exercises = viewModel.selectedExercises.value ?: return@withContext
                var allSuccess = true

                for (exercise in exercises) {
                    val response = retryOnFailure(times = 3) {
                        ApiService.workoutExerciseService.createWorkoutExercise(
                            WorkoutExerciseDto(
                                workoutId = workoutId,
                                exerciseId = exercise.exercise.id?.toLong() ?: -1,
                                sets = exercise.sets,
                                reps = exercise.reps,
                                restTime = exercise.rest,
                                distance = 0.0,
                                duration = 0.0,
                                id = null,
                                exerciseOrder = null
                            )
                        ).execute()
                    }

                    if (!response.isSuccessful) {
                        allSuccess = false
                        withContext(Dispatchers.Main) {
                            showError("Ошибка сохранения: ${exercise.exercise.name}")
                        }
                        break
                    }
                }

                if (allSuccess) {
                    withContext(Dispatchers.Main) {
                        (activity as? MainActivity)?.refreshWorkouts()
                        parentFragmentManager.popBackStack()
                        viewModel.clearSelectedExercises()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Ошибка: ${e.message}")
                }
            }
        }
    }

    private suspend fun <T> retryOnFailure(
        times: Int = 3,
        initialDelay: Long = 1000,
        maxDelay: Long = 10000,
        block: suspend () -> Response<T>
    ): Response<T> {
        var currentDelay = initialDelay
        repeat(times - 1) {
            val response = block()
            if (response.isSuccessful) return response
            kotlinx.coroutines.delay(currentDelay)
            currentDelay = (currentDelay * 2).coerceAtMost(maxDelay)
        }
        return block()
    }

    private fun showError(message: String) {
        if (isAdded && context != null) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearSelectedExercises()
        _binding = null
    }
}