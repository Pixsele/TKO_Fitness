package tk.ssau.fitnesstko

import android.content.Context
import androidx.core.content.edit

class PreferencesManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUserData(firstName: String, lastName: String, age: String) {
        sharedPreferences.edit {
            putString("firstName", firstName)
            putString("lastName", lastName)
            putString("age", age)
        }
    }

    fun saveWeight(weight: String) {
        sharedPreferences.edit {
            putString("lastWeight", weight)
        }
    }

    fun getFullName(): String {
        val firstName = getFirstName()
        val lastName = getLastName()
        return if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
            "$firstName $lastName"
        } else {
            "Имя Фамилия"
        }
    }

    fun getLastWeight(): String =
        sharedPreferences.getString("lastWeight", "Нет данных") ?: "Нет данных"

    fun getFirstName() = sharedPreferences.getString("firstName", "") ?: ""
    fun getLastName() = sharedPreferences.getString("lastName", "") ?: ""
    fun getAge() = sharedPreferences.getString("age", "") ?: ""
}