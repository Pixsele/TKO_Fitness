package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import tk.ssau.fitnesstko.databinding.CollectionBinding

class FragmentCollection : Fragment(R.layout.collection) {

    private lateinit var binding: CollectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val collectionList = listOf("Тренировки", "Программы", "Питание")
        val adapter = CategoriesAdapter(collectionList)
        var exercises = mutableListOf(
            Exercises("Название упражнения", true, 0),
            Exercises("Название упражнения", true, 0),
            Exercises("Название упражнения", true, 0)
        )
        val adapter2 = ExercisesAdapter(exercises)
        binding = CollectionBinding.inflate(inflater, container, false)
        binding.rvExercises.adapter = adapter2
        binding.rvExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategories.adapter = adapter
        binding.rvCategories.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.HORIZONTAL, false
        )

        binding.ibAddExercises.setOnClickListener {
            exercises.add(Exercises("Название упражнения", true, 0))
            adapter2.notifyItemInserted(exercises.size - 1)
        }

        return binding.root
    }


}