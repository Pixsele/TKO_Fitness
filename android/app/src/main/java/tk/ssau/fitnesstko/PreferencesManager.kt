package tk.ssau.fitnesstko

import android.content.Context
import androidx.core.content.edit
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tk.ssau.fitnesstko.model.dto.PlannedWorkoutDto
import tk.ssau.fitnesstko.model.dto.WorkoutForPageDto
import tk.ssau.fitnesstko.model.dto.user.WeightDto
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PreferencesManager(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences("fitness_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val authManager = AuthManager(context)

    companion object {
        const val PLANNED_WORKOUTS_KEY = "planned_workouts"
    }


    fun saveUserData(firstName: String, lastName: String, birthDay: String) {
        sharedPreferences.edit {
            putString("firstName", firstName)
            putString("lastName", lastName)
            putString("birthDay", birthDay)
            apply()
        }
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun saveUserProfile(name: String, birthDay: String, weight: Double, height: Double) {
        sharedPreferences.edit {
            putString("user_name", name)
            putString("birthDay", birthDay)
            putFloat("weight", weight.toFloat())
            putFloat("height", height.toFloat())
        }
    }

    fun saveAndSendWeight(weight: Float) {
        val userId = authManager.getUserId() ?: return

        coroutineScope.launch {
            try {
                val currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                val weightDto = WeightDto(
                    userId = userId,
                    weight = weight,
                    timeDate = currentTime
                )

               ApiService.weightService.postWeight(weightDto).execute()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveWeightsBatch(weightEntries: List<String>) {
        sharedPreferences.edit {
            val currentWeights = getWeightsRaw().toMutableSet()
            currentWeights.addAll(weightEntries)
            putStringSet("weightHistory", currentWeights)
            apply()
        }
    }

    fun saveWeight(weight: Float) {
        val currentTime = System.currentTimeMillis()

        sharedPreferences.edit {
            putStringSet(
                "weightHistory",
                getWeightsRaw().plus("$currentTime|$weight").toSet()
            )
            apply()
        }
        saveAndSendWeight(weight)
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
                    } catch (_: Exception) {
                        null
                    }
                } else null
            }
            ?.sortedBy { it.first } ?: emptyList()
    }

    fun getLocalWorkouts(): List<WorkoutForPageDto> {
        val json = sharedPreferences.getString("local_workouts", null)
        return if (json != null) {
            gson.fromJson(json, Array<WorkoutForPageDto>::class.java).toList()
        } else {
            emptyList()
        }
    }

    fun updateLocalWorkout(updatedWorkout: WorkoutForPageDto) {
        val workouts = getLocalWorkouts().toMutableList()
        val index = workouts.indexOfFirst { it.id == updatedWorkout.id }

        if (index != -1) {
            workouts[index] = updatedWorkout
            sharedPreferences.edit {
                putString("local_workouts", gson.toJson(workouts))
                apply()
            }
        }
    }

    fun saveKcalTrackerId(date: String, trackerId: Long) {
        sharedPreferences.edit {
            putLong("kcalTracker_$date", trackerId)
        }
    }

    fun getKcalTrackerId(date: String): Long? {
        val id = sharedPreferences.getLong("kcalTracker_$date", -1L)
        return if (id != -1L) id else null
    }

    fun saveSelectedDate(date: LocalDate) {
        val dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        sharedPreferences.edit {
            putString("selectedDate", dateStr)
        }
    }

    fun getSelectedDate(): LocalDate? {
        val dateStr = sharedPreferences.getString("selectedDate", null)
        return dateStr?.let { LocalDate.parse(it) }
    }

    fun saveAvatarUri(uri: String) {
        sharedPreferences.edit {
            putString("avatar_uri", uri)
            apply()
        }
    }

    fun getAvatarUri(): String? {
        return sharedPreferences.getString("avatar_uri", null)
    }

    fun clearUserData() {
        sharedPreferences.edit {
            clear()
            apply()
        }
    }

    fun savePlannedWorkouts(workouts: List<PlannedWorkoutDto>) {
        val json = Gson().toJson(workouts)
        sharedPreferences.edit { putString(PLANNED_WORKOUTS_KEY, json) }
    }

    fun getPlannedWorkouts(): List<PlannedWorkoutDto> {
        val json = sharedPreferences.getString(PLANNED_WORKOUTS_KEY, null)
        return if (json != null) {
            Gson().fromJson(json, object : TypeToken<List<PlannedWorkoutDto>>() {}.type)
        } else {
            emptyList()
        }
    }
}