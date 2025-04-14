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

class FragmentCollection : Fragment(R.layout.collection) {

    private lateinit var binding: CollectionBinding
    private lateinit var workoutAdapter: WorkoutAdapter
    private var workouts = mutableListOf<Workout>()

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
        workoutAdapter = WorkoutAdapter(workouts) { workout ->
            // Обработка клика на тренировку
            //WorkoutDetailsFragment.newInstance(workout.id)
            //.show(parentFragmentManager, "workout_details")
        }
        binding.rvWorkouts.adapter = workoutAdapter
        binding.rvWorkouts.layoutManager = LinearLayoutManager(requireContext())
    }

    internal fun loadWorkouts() {
        ApiService.workoutService.getWorkouts()
            .enqueue(object : Callback<PagedResponse<Workout>> {
                override fun onResponse(
                    call: Call<PagedResponse<Workout>>,
                    response: Response<PagedResponse<Workout>>
                ) {
                    if (response.isSuccessful) {
                        val serverWorkouts = response.body()?.content ?: emptyList()
                        val localWorkouts = (activity as? MainActivity)?.prefs?.getLocalWorkouts() ?: emptyList()

                        // Объединяем и обновляем список
                        workouts.clear()
                        workouts.addAll(serverWorkouts + localWorkouts)
                        workoutAdapter.notifyDataSetChanged()
                    }
                }
                override fun onFailure(call: Call<PagedResponse<Workout>>, t: Throwable) {
                    // Показать только локальные данные при ошибке сети
                    val localWorkouts = (activity as? MainActivity)?.prefs?.getLocalWorkouts() ?: emptyList()
                    workouts.clear()
                    workouts.addAll(localWorkouts)
                    workoutAdapter.notifyDataSetChanged()
                }
            })
    }
}