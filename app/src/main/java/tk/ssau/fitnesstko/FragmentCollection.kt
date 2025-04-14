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

        // Список тренировок
        workoutAdapter = WorkoutAdapter(workouts) { workout ->
            // Обработка клика на тренировку
            //WorkoutDetailsFragment.newInstance(workout.id)
                //.show(parentFragmentManager, "workout_details")
        }
        binding.rvWorkouts.adapter = workoutAdapter
        binding.rvWorkouts.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadWorkouts() {
        ApiService.workoutService.getWorkouts()
            .enqueue(object : Callback<PagedResponse<Workout>> {
                override fun onResponse(
                    call: Call<PagedResponse<Workout>>,
                    response: Response<PagedResponse<Workout>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.content?.let { newWorkouts ->
                            workouts.clear()
                            workouts.addAll(newWorkouts)
                            workoutAdapter.notifyDataSetChanged()
                        }
                    } else {
                        // Обработка ошибок
                    }
                }

                override fun onFailure(call: Call<PagedResponse<Workout>>, t: Throwable) {
                    // Обработка ошибок сети
                }
            })
    }
}