package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.databinding.FragmentProgramDetailBinding
import tk.ssau.fitnesstko.model.dto.TrainingsProgramDto
import tk.ssau.fitnesstko.model.dto.WorkoutDto
import tk.ssau.fitnesstko.model.dto.WorkoutForPageDto
import tk.ssau.fitnesstko.model.dto.WorkoutProgramDto

class FragmentProgramDetail : Fragment() {
    private var _binding: FragmentProgramDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var workoutAdapter: WorkoutAdapter
    private var programId: Long = -1L
    private var workouts = mutableListOf<WorkoutForPageDto>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProgramDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        programId = arguments?.getLong("programId") ?: -1L

        setupToolbar()
        setupRecyclerView()
        loadProgramData()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        workoutAdapter = WorkoutAdapter(
            workouts = emptyList(),
            fragmentManager = parentFragmentManager,
            onDataUpdated = { updatedWorkout ->
                // Обработка обновлений, если нужно
            },
            authManager = AuthManager(requireContext())
        )
        binding.rvWorkouts.adapter = workoutAdapter
    }

    private fun loadProgramData() {
        showLoading(true)
        // Загрузка деталей программы
        ApiService.programService.getProgramById(programId).enqueue(object : Callback<TrainingsProgramDto> {
            override fun onResponse(call: Call<TrainingsProgramDto>, response: Response<TrainingsProgramDto>) {
                if (response.isSuccessful) {
                    response.body()?.let { program ->
                        binding.tvProgramName.text = program.name
                        binding.tvDescription.text = program.description
                        binding.tvDifficulty.text = "Сложность: ${program.difficult}"
                        loadWorkouts()
                    }
                } else {
                    showError("Ошибка загрузки программы")
                }
                showLoading(false)
            }

            override fun onFailure(call: Call<TrainingsProgramDto>, t: Throwable) {
                showError("Ошибка соединения")
                showLoading(false)
            }
        })
    }

    private fun loadWorkouts() {
        // Загрузка списка тренировок программы
        ApiService.workoutProgramService.getWorkoutsByProgram(programId).enqueue(object : Callback<List<WorkoutProgramDto>> {
            override fun onResponse(call: Call<List<WorkoutProgramDto>>, response: Response<List<WorkoutProgramDto>>) {
                if (response.isSuccessful) {
                    response.body()?.let { workoutPrograms ->
                        val workoutIds = workoutPrograms.map { it.workoutId }
                        loadWorkoutDetails(workoutIds)
                    }
                }
            }

            override fun onFailure(call: Call<List<WorkoutProgramDto>>, t: Throwable) {
                showError("Ошибка загрузки тренировок")
            }
        })
    }

    private fun loadWorkoutDetails(workoutIds: List<Long>) {
        workouts.clear()
        workoutIds.forEach { id ->
            ApiService.workoutService.getWorkoutById(id).enqueue(object : Callback<WorkoutDto> {
                override fun onResponse(call: Call<WorkoutDto>, response: Response<WorkoutDto>) {
                    if (response.isSuccessful) {
                        response.body()?.let { workout ->
                            workouts.add(
                                WorkoutForPageDto(
                                    id = workout.id,
                                    name = workout.name,
                                    likeCount = workout.likeCount,
                                    liked = false // Здесь можно добавить проверку лайков
                                )
                            )
                            if (workouts.size == workoutIds.size) {
                                workoutAdapter.updateWorkouts(workouts.sortedBy { it.id })
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<WorkoutDto>, t: Throwable) {
                    showError("Ошибка загрузки тренировки")
                }
            })
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}