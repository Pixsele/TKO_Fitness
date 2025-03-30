package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import tk.ssau.fitnesstko.databinding.FragmentAddWeightBinding

class AddWeightFragment : Fragment(R.layout.fragment_add_weight) {

    private lateinit var binding: FragmentAddWeightBinding
    private lateinit var prefs: PreferencesManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddWeightBinding.bind(view)
        prefs = (activity as MainActivity).prefs

        val lastWeight = prefs.getLastWeight()
        binding.tvLastWeight.text = "Последнее взвешивание: $lastWeight кг"

        binding.btnSaveWeight.setOnClickListener {
            val newWeight = binding.etWeight.text.toString()
            if (newWeight.isNotEmpty()) {
                prefs.saveWeight(newWeight)
                parentFragmentManager.popBackStack()
            } else {
                binding.etWeight.error = "Введите вес"
            }
        }
    }
}