package tk.ssau.fitnesstko.food

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.ApiService
import tk.ssau.fitnesstko.PreferencesManager
import tk.ssau.fitnesstko.R
import tk.ssau.fitnesstko.model.dto.nutrition.KcalProductDTO
import tk.ssau.fitnesstko.model.dto.nutrition.ProductDTO
import java.math.RoundingMode
import java.time.format.DateTimeFormatter

class AddProductDetailsFragment : Fragment() {

    private var productId: Long = 0
    private var typeMeal: String = ""
    private lateinit var product: ProductDTO
    private lateinit var viewModel: KcalProductViewModel
    private var trackerId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_product_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[KcalProductViewModel::class.java]

        productId = arguments?.getLong("productId") ?: 0
        typeMeal = arguments?.getString("typeMeal") ?: "BREAKFAST"

        val prefs = PreferencesManager(requireContext())
        val date = prefs.getSelectedDate()?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: run {
            Toast.makeText(context, "Дата не выбрана", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
            return
        }
        trackerId = prefs.getKcalTrackerId(date) ?: run {
            Toast.makeText(context, "Трекер не найден", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
            return
        }

        loadProductDetails()

        setupInputListeners()

        view.findViewById<Button>(R.id.btnSave).setOnClickListener {
            saveProductToViewModel()
        }
    }

    private fun loadProductDetails() {
        ApiService.productService.getProductDetails(productId)
            .enqueue(object : Callback<ProductDTO> {
                override fun onResponse(
                    call: Call<ProductDTO>,
                    response: Response<ProductDTO>
                ) {
                    response.body()?.let {
                        product = it
                        updateUI(1)
                    }
                }

                override fun onFailure(call: Call<ProductDTO>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Ошибка загрузки продукта",
                        Toast.LENGTH_SHORT
                    ).show()
                    parentFragmentManager.popBackStack()
                }
            })
    }

    private fun setupInputListeners() {
        view?.findViewById<EditText>(R.id.etPortionCount)?.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }

                override fun afterTextChanged(s: Editable?) {
                    val count = s.toString().toIntOrNull() ?: 1
                    updateUI(count)
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(count: Int) {
        val calories = product.kcal * count
        val fats = product.fats.multiply(count.toBigDecimal())
        val carbs = product.carbs.multiply(count.toBigDecimal())
        val protein = product.proteins.multiply(count.toBigDecimal())

        view?.findViewById<TextView>(R.id.tvCalories)?.text = "Калории: $calories"
        view?.findViewById<TextView>(R.id.tvFats)?.text =
            "Жиры: ${fats.setScale(1, RoundingMode.HALF_UP)} г"
        view?.findViewById<TextView>(R.id.tvCarbs)?.text =
            "Углеводы: ${carbs.setScale(1, RoundingMode.HALF_UP)} г"
        view?.findViewById<TextView>(R.id.tvProtein)?.text =
            "Белки: ${protein.setScale(1, RoundingMode.HALF_UP)} г"
    }

    private fun saveProductToViewModel() {
        val count = view?.findViewById<EditText>(R.id.etPortionCount)
            ?.text.toString()
            .toIntOrNull() ?: 1

        val productDTO = KcalProductDTO(
            id = null,
            kcalTrackerId = trackerId,
            productId = productId,
            count = count,
            typeMeal = typeMeal
        )

        viewModel.addProduct(productDTO)

        parentFragmentManager.popBackStack()
    }
}