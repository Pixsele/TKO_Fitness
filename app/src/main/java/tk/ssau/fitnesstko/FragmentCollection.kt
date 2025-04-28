package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.databinding.CollectionBinding
import tk.ssau.fitnesstko.model.dto.WorkoutForPageDto

class FragmentCollection : Fragment(R.layout.collection) {

    private lateinit var binding: CollectionBinding
    private lateinit var workoutAdapter: WorkoutAdapter
    private var workouts = mutableListOf<WorkoutForPageDto>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CollectionBinding.inflate(inflater, container, false)
        setupRecyclers()
        loadWorkouts()
        return binding.root
    }

    private fun setupRecyclers() {
        val categories = listOf("Тренировки", "Программы")
        binding.rvCategories.adapter = CategoriesAdapter(categories)
        binding.rvCategories.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        workoutAdapter = WorkoutAdapter(
            workouts,
            { workout -> openWorkoutDetails(workout) },
            { workout -> handleLikeClick(workout) }
        )
        binding.rvWorkouts.adapter = workoutAdapter
        binding.rvWorkouts.layoutManager = LinearLayoutManager(requireContext())

        binding.ibAddExercises.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, CreateWorkoutFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun openWorkoutDetails(workout: WorkoutForPageDto) {
        // Реализация открытия деталей тренировки
    }

    private fun handleLikeClick(workout: WorkoutForPageDto) {
        val updatedWorkout = workout.copy(
            liked = !workout.liked,
            likeCount = (workout.likeCount ?: 0) + if (workout.liked) -1 else 1
        )

        // Обновляем локальное хранилище
        (activity as? MainActivity)?.prefs?.updateLocalWorkout(updatedWorkout)

        // Находим позицию и обновляем список
        val position = workouts.indexOfFirst { it.id == workout.id }
        if (position != -1) {
            workouts = workouts.toMutableList().apply {
                set(position, updatedWorkout)
            }
            workoutAdapter.notifyItemChanged(position)
        }
    }

    private fun updateWorkoutInList(updatedWorkout: WorkoutForPageDto) {
        val index = workouts.indexOfFirst { it.id == updatedWorkout.id }
        if (index != -1) {
            workouts[index] = updatedWorkout
            workoutAdapter.notifyItemChanged(index)
        }
    }

    internal fun loadWorkouts() {
        ApiService.workoutService.getWorkouts()
            .enqueue(object : Callback<PagedResponse<WorkoutForPageDto>> {
                override fun onResponse(
                    call: Call<PagedResponse<WorkoutForPageDto>>,
                    response: Response<PagedResponse<WorkoutForPageDto>>
                ) {
                    if (response.isSuccessful) {
                        val serverWorkouts = response.body()?.content ?: emptyList()
                        val localWorkouts = getLocalWorkouts()

                        workouts.clear()
                        workouts.addAll(serverWorkouts + localWorkouts)
                        workoutAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<PagedResponse<WorkoutForPageDto>>, t: Throwable) {
                    workouts.clear()
                    workouts.addAll(getLocalWorkouts())
                    workoutAdapter.notifyDataSetChanged()
                }
            })
    }

    private fun getLocalWorkouts() =
        (activity as? MainActivity)?.prefs?.getLocalWorkouts() ?: emptyList()

    fun getWorkoutNames() = workouts.map { it.name }

    override fun onResume() {
        super.onResume()
        loadWorkouts()
    }
}