package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import tk.ssau.fitnesstko.databinding.WorkoutBinding
import java.time.LocalTime

class FragmentWorkout : Fragment(R.layout.workout) {
    private lateinit var binding: WorkoutBinding
    private lateinit var prefs: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = WorkoutBinding.bind(view)
        prefs = (activity as MainActivity).prefs
        observeUserName()
    }

    private fun observeUserName() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                tickerFlow(60_000).collect {
                    updateGreeting()
                }
            }
        }
    }
    private fun tickerFlow(periodMillis: Long): Flow<Unit> = flow {
        while (true) {
            emit(Unit)
            delay(periodMillis)
        }
    }

    private fun updateGreeting() {
        val currentTime = LocalTime.now()
        val greeting = when (currentTime) {
            in GoodMorning -> "Доброе утро"
            in GoodAfternoon -> "Добрый день"
            in GoodEvening -> "Добрый вечер"
            else -> "Доброй ночи"
        }

        binding.tvGreeting.text = "$greeting, ${prefs.getFullName()}!"
    }

    // Современный подход к определению временных диапазонов
    private companion object TimeRanges {
        val GoodMorning = LocalTime.of(5, 0)..LocalTime.of(11, 59)
        val GoodAfternoon = LocalTime.of(12, 0)..LocalTime.of(16, 59)
        val GoodEvening = LocalTime.of(17, 0)..LocalTime.of(22, 59)
    }
}