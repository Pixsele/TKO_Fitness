package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.databinding.FragmentExercisePickerBinding

class ExercisePickerFragment() : Fragment(R.layout.fragment_exercise_picker) {

    private lateinit var binding: FragmentExercisePickerBinding
    private lateinit var viewModel: SharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentExercisePickerBinding.bind(view)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        loadExercises()
    }

    private fun loadExercises() {
        ApiService.exerciseService.getExercises()
            .enqueue(object : Callback<PagedResponse<Exercise>> {
                override fun onResponse(
                    call: Call<PagedResponse<Exercise>>,
                    response: Response<PagedResponse<Exercise>>
                ) {
                    val allExercises = response.body()?.content ?: emptyList()
                    val unselectedExercises = filterUnselected(allExercises)
                    setupRecycler(unselectedExercises)
                }

                override fun onFailure(call: Call<PagedResponse<Exercise>>, t: Throwable) {
                    Toast.makeText(context, "Ошибка загрузки", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun filterUnselected(allExercises: List<Exercise>): List<Exercise> {
        val selectedIds = viewModel.selectedExercises.value?.map { it.id } ?: emptyList()
        return allExercises.filter { it.id !in selectedIds }
    }

    private fun setupRecycler(exercises: List<Exercise>) {
        binding.rvExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExercises.adapter = ExerciseAdapter(exercises) { exercise ->
            viewModel.addExercise(exercise) // Передача выбранного упражнения
            parentFragmentManager.popBackStack() // Возврат к предыдущему фрагменту
        }
    }
}