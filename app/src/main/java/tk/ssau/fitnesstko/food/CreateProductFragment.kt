package tk.ssau.fitnesstko.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.ApiService
import tk.ssau.fitnesstko.databinding.FragmentCreateProductBinding
import tk.ssau.fitnesstko.model.dto.nutrition.ProductDTO
import java.math.BigDecimal

class CreateProductFragment : Fragment() {

    private lateinit var binding: FragmentCreateProductBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            if (validateInput()) {
                createProduct()
            }
        }
    }

    private fun validateInput(): Boolean {
        return when {
            binding.etName.text.isNullOrEmpty() -> {
                showError("Введите название")
                false
            }
            binding.etKcal.text.isNullOrEmpty() -> {
                showError("Введите калории")
                false
            }
            binding.etUnit.text.isNullOrEmpty() -> {
                showError("Введите единицу измерения")
                false
            }
            else -> true
        }
    }

    private fun createProduct() {
        val product = ProductDTO(
            id = null,
            name = binding.etName.text.toString(),
            kcal = binding.etKcal.text.toString().toInt(),
            unit = binding.etUnit.text.toString(),
            grams = binding.etGrams.text.toString().toBigDecimalOrNull() ?: BigDecimal.ZERO,
            portion = binding.etPortion.text.toString().toBigDecimalOrNull() ?: BigDecimal.ONE,
            fats = binding.etFats.text.toString().toBigDecimalOrNull() ?: BigDecimal.ZERO,
            carbs = binding.etCarbs.text.toString().toBigDecimalOrNull() ?: BigDecimal.ZERO,
            proteins = binding.etProteins.text.toString().toBigDecimalOrNull() ?: BigDecimal.ZERO,
            createdAt = null,
            percent = null
        )

        ApiService.productService.createProduct(product)
            .enqueue(object : Callback<ProductDTO> {
                override fun onResponse(call: Call<ProductDTO>, response: Response<ProductDTO>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Продукт создан", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    } else {
                        showError("Ошибка: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ProductDTO>, t: Throwable) {
                    showError("Ошибка сети: ${t.message}")
                }
            })
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}