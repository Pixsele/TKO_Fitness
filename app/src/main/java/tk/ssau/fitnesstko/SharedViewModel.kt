package tk.ssau.fitnesstko

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tk.ssau.fitnesstko.model.dto.ExerciseForPageDto

class SharedViewModel : ViewModel() {
    private val _selectedExercises =
        MutableLiveData<MutableList<ExerciseForPageDto>>(mutableListOf())
    val selectedExercises: LiveData<MutableList<ExerciseForPageDto>> = _selectedExercises

    fun addExercise(exercise: ExerciseForPageDto) {
        val currentList = _selectedExercises.value ?: mutableListOf()
        currentList.add(exercise)
        _selectedExercises.value = currentList
    }
}