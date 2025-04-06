package tk.ssau.fitnesstko

import android.content.Context
import androidx.core.content.edit

class PreferencesManager(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("fitness_prefs", Context.MODE_PRIVATE)

    // Сохранение данных пользователя
    fun saveUserData(firstName: String, lastName: String, age: String) {
        sharedPreferences.edit {
            putString("firstName", firstName)
            putString("lastName", lastName)
            putString("age", age)
        }
    }

    fun getFirstName() = sharedPreferences.getString("firstName", "") ?: ""
    fun getLastName() = sharedPreferences.getString("lastName", "") ?: ""

    // Получение полного имени
    fun getFullName(): String {
        val firstName = sharedPreferences.getString("firstName", "") ?: ""
        val lastName = sharedPreferences.getString("lastName", "") ?: ""
        return if (firstName.isNotEmpty() && lastName.isNotEmpty()) "$firstName $lastName" else "Имя Фамилия"
    }

    // Сохранение тренировки
    fun saveWorkout(date: String, type: String) {
        val workouts = getWorkouts().toMutableSet()
        workouts.add("$date|$type")
        sharedPreferences.edit {
            putStringSet("workouts", workouts)
        }
    }

    fun saveWeight(weight: String) {
        sharedPreferences.edit {
            putString("lastWeight", weight)
        }
    }

    fun getLastWeight(): String =
        sharedPreferences.getString("lastWeight", "Нет данных") ?: "Нет данных"

    // Получение всех тренировок
    fun getWorkouts(): Set<String> =
        sharedPreferences.getStringSet("workouts", emptySet()) ?: emptySet()

    // Получение возраста
    fun getAge(): String =
        sharedPreferences.getString("age", "") ?: ""
}