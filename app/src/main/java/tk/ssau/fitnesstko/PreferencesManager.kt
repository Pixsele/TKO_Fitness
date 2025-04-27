package tk.ssau.fitnesstko

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import tk.ssau.fitnesstko.model.dto.WorkoutForPageDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PreferencesManager(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("fitness_prefs", Context.MODE_PRIVATE)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    // Сохранение данных пользователя
    fun saveUserData(firstName: String, lastName: String, age: String) {
        sharedPreferences.edit {
            putString("firstName", firstName)
            putString("lastName", lastName)
            putString("age", age)
        }
    }

    fun saveWeight(weight: Float) {
        val currentTime = System.currentTimeMillis() // Используем текущее время с миллисекундами

        sharedPreferences.edit {
            putStringSet(
                "weightHistory",
                getWeightsRaw().plus("$currentTime|$weight").toSet()
            )
            apply()
        }
    }

    private fun getWeightsRaw() = getWeights()
        .map { "${it.first}|${it.second}" }

    fun getWeights(): List<Pair<Long, Float>> {
        return sharedPreferences.getStringSet("weightHistory", emptySet())
            ?.toList()
            ?.mapNotNull {
                val parts = it.split("|")
                if (parts.size == 2) {
                    try {
                        val timestamp = parts[0].toLong()
                        val weight = parts[1].toFloat()
                        Pair(timestamp, weight)
                    } catch (e: Exception) {
                        null
                    }
                } else null
            }
            ?.sortedBy { it.first } ?: emptyList()
    }

    // Получение последнего веса
    fun getLastWeight(): String {
        return getWeights().lastOrNull()?.let {
            "%.1f кг (${dateFormat.format(Date(it.first))})".format(it.second)
        } ?: "Нет данных"
    }

    // Сохранение тренировок
    fun saveWorkout(date: String, type: String) {
        val workouts = getWorkouts().toMutableSet()
        workouts.add("$date|$type")
        sharedPreferences.edit {
            putStringSet("workouts", workouts)
        }
    }

    // Получение тренировок
    fun getWorkouts(): Set<String> {
        return sharedPreferences.getStringSet("workouts", emptySet()) ?: emptySet()
    }

    fun getFirstName() = sharedPreferences.getString("firstName", "") ?: ""
    fun getLastName() = sharedPreferences.getString("lastName", "") ?: ""
    fun getAge() = sharedPreferences.getString("age", "") ?: ""


    fun saveLocalWorkout(workout: WorkoutForPageDto) {
        val workouts = getLocalWorkouts().toMutableList()
        workouts.add(workout)
        sharedPreferences.edit {
            putStringSet("local_workouts", workouts.toJsonSet())
        }
    }

    private fun List<WorkoutForPageDto>.toJsonSet(): Set<String> {
        return map { Gson().toJson(it) }.toSet()
    }

    fun getLocalWorkouts(): List<WorkoutForPageDto> {
        return sharedPreferences.getStringSet("local_workouts", emptySet())
            ?.mapNotNull { Gson().fromJson(it, WorkoutForPageDto::class.java) }
            ?: emptyList()
    }
}