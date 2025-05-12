package tk.ssau.fitnesstko.profile

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import tk.ssau.fitnesstko.R

class CalculateCaloriesFragment : Fragment(R.layout.fragment_calculate_calories) {

    private val activityLevels = listOf(
        "Сидячий образ жизни" to 1.2,
        "Легкая активность" to 1.375,
        "Умеренная активность" to 1.55,
        "Высокая активность" to 1.725,
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_item,
            activityLevels.map { it.first }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        val spinner = view.findViewById<Spinner>(R.id.spActivity).apply {
            this.adapter = adapter
        }

        view.findViewById<Button>(R.id.btnCalculate).setOnClickListener {
            calculateCalories(spinner)
        }
    }

    private fun calculateCalories(spinner: Spinner) {
        val age = view?.findViewById<EditText>(R.id.etAge)?.text.toString().toIntOrNull()
        val weight = view?.findViewById<EditText>(R.id.etWeight)?.text.toString().toDoubleOrNull()
        val height = view?.findViewById<EditText>(R.id.etHeight)?.text.toString().toIntOrNull()
        val isMale = view?.findViewById<RadioButton>(R.id.rbMale)?.isChecked ?: false
        val activityLevel = activityLevels[spinner.selectedItemPosition].second

        if (age == null || weight == null || height == null) {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        val bmr = if (isMale) {
            10 * weight + 6.25 * height - 5 * age + 5
        } else {
            10 * weight + 6.25 * height - 5 * age - 161
        }

        val result = (bmr * activityLevel).toInt()
        view?.findViewById<TextView>(R.id.tvResult)?.text =
            "Ваша суточная норма: ${result} ккал"
    }
}