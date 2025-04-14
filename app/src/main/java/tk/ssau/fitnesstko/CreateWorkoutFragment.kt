package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import tk.ssau.fitnesstko.databinding.FragmentCreateWorkoutBinding

class CreateWorkoutFragment : Fragment(R.layout.fragment_create_workout) {
    private lateinit var binding: FragmentCreateWorkoutBinding
    private var difficulty: String = "MEDIUM"
    private lateinit var viewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateWorkoutBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        setupUI()
        setupDifficultyGroup()
        observeSelectedExercises()
    }

    private fun observeSelectedExercises() {
        viewModel.selectedExercises.observe(viewLifecycleOwner) { exercises ->
            binding.containerExercises.removeAllViews()
            exercises.forEach { exercise ->
                val view = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_selected_exercise, null)
                view.findViewById<TextView>(R.id.tvExerciseName).text = exercise.name
                binding.containerExercises.addView(view)
            }
        }
    }

    private fun setupUI() {
        binding.btnAddExercise.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, ExercisePickerFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.btnSave.setOnClickListener { saveWorkout() }
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

    private fun addExerciseToContainer(exercise: Exercise) {
        val view = LayoutInflater.from(requireContext())
            .inflate(R.layout.item_selected_exercise, null)

        view.findViewById<TextView>(R.id.tvExerciseName).text = exercise.name
        binding.containerExercises.addView(view)
    }

    private fun saveWorkout() {
        if (validateInput()) {
            val workout = Workout(
                id = System.currentTimeMillis(), // Уникальный ID для локальных тренировок
                name = binding.etWorkoutName.text.toString(),
                description = binding.etDescription.text.toString(),
                difficulty = difficulty,
                exercises = viewModel.selectedExercises.value?: emptyList(),
                likeCount = 0,
                liked = false
            )

            // Сохранение в локальное хранилище
            (activity as? MainActivity)?.prefs?.saveLocalWorkout(workout)

            // Обновление списка тренировок
            (activity as? MainActivity)?.refreshWorkouts()
            parentFragmentManager.popBackStack()
        }
    }

    private fun validateInput(): Boolean {
        return when {
            binding.etWorkoutName.text.isNullOrEmpty() -> {
                showError("Введите название тренировки")
                false
            }

            viewModel.selectedExercises.value?.isEmpty() == true -> {
                showError("Добавьте хотя бы одно упражнение")
                false
            }

            else -> true
        }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}