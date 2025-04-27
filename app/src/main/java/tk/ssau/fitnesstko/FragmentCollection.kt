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
        // Категории
        val categories = listOf("Тренировки", "Программы")
        binding.rvCategories.adapter = CategoriesAdapter(categories)
        binding.rvCategories.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.ibAddExercises.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, CreateWorkoutFragment())
                .addToBackStack(null)
                .commit()
        }

        // Список тренировок
        workoutAdapter = WorkoutAdapter(
            workouts,
            { workout -> /* клик на элемент */ },
            { workout -> handleLikeClick(workout) } // Передаем обработчик
        )
        binding.rvWorkouts.adapter = workoutAdapter
        binding.rvWorkouts.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun handleLikeClick(workout: WorkoutForPageDto) {

        /*if (workout.id == null || workout.id <= 0L) {
            // Локальная тренировка - обновляем локально
            updateLocalWorkout(workout)
        }

         */
        updateLocalWorkout(workout)
        /*else {
            // Серверная тренировка - отправляем запрос
            ApiService.workoutService.toggleLike(workout.id)
                .enqueue(object : Callback<WorkoutForPageDto> {
                    override fun onResponse(
                        call: Call<WorkoutForPageDto>,
                        response: Response<WorkoutForPageDto>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let { updatedWorkout ->
                                updateWorkoutInList(updatedWorkout)
                            }
                        }
                    }

                    override fun onFailure(call: Call<WorkoutForPageDto>, t: Throwable) {
                        Toast.makeText(context, "Ошибка сети", Toast.LENGTH_SHORT).show()
                        // Откатываем состояние
                        revertLikeState(workout)
                    }
                })
                */

        // Временное обновление UI до ответа сервера
        val tempWorkout = workout.copy(
            liked = !workout.liked,
            likeCount = workout.likeCount?.plus(if (workout.liked) -1 else 1)
        )
        updateWorkoutInList(tempWorkout)
    }

    private fun updateLocalWorkout(workout: WorkoutForPageDto) {
        val updatedWorkout = workout.copy(
            liked = !workout.liked,
            likeCount = workout.likeCount?.plus(if (workout.liked) -1 else 1)
        )

        (activity as? MainActivity)?.prefs?.updateLocalWorkout(updatedWorkout)
        updateWorkoutInList(updatedWorkout)
    }

    private fun updateWorkoutInList(updatedWorkout: WorkoutForPageDto) {
        val index = workouts.indexOfFirst { it.id == updatedWorkout.id }
        if (index != -1) {
            workouts[index] = updatedWorkout
            workoutAdapter.notifyItemChanged(index)

            // Принудительно обновляем источник данных
            workoutAdapter = WorkoutAdapter(
                workouts,
                { workout -> /* клик на элемент */ },
                { workout -> handleLikeClick(workout) }
            )
            binding.rvWorkouts.adapter = workoutAdapter
        }
    }

    private fun revertLikeState(workout: WorkoutForPageDto) {
        val revertedWorkout = workout.copy(
            liked = !workout.liked,
            likeCount = workout.likeCount?.plus(if (workout.liked) 1 else -1)
        )
        updateWorkoutInList(revertedWorkout)
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
                        val localWorkouts =
                            (activity as? MainActivity)?.prefs?.getLocalWorkouts() ?: emptyList()

                        // Объединяем и обновляем список
                        workouts.clear()
                        workouts.addAll(serverWorkouts + localWorkouts)
                        workoutAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<PagedResponse<WorkoutForPageDto>>, t: Throwable) {
                    // Показать только локальные данные при ошибке сети
                    val localWorkouts =
                        (activity as? MainActivity)?.prefs?.getLocalWorkouts() ?: emptyList()
                    workouts.clear()
                    workouts.addAll(localWorkouts)
                    workoutAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        loadWorkouts()
    }
}