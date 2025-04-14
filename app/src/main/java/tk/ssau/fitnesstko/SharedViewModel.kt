package tk.ssau.fitnesstko

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _selectedExercises = MutableLiveData<MutableList<Exercise>>(mutableListOf())
    val selectedExercises: LiveData<MutableList<Exercise>> = _selectedExercises

    fun addExercise(exercise: Exercise) {
        val currentList = _selectedExercises.value ?: mutableListOf()
        currentList.add(exercise)
        _selectedExercises.value = currentList
    }
}