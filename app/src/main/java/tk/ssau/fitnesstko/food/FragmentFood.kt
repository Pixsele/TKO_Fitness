package tk.ssau.fitnesstko.food

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.ApiService
import tk.ssau.fitnesstko.AuthManager
import tk.ssau.fitnesstko.PreferencesManager
import tk.ssau.fitnesstko.R
import tk.ssau.fitnesstko.model.dto.nutrition.KcalProductDTO
import tk.ssau.fitnesstko.model.dto.nutrition.KcalTrackerDTO
import tk.ssau.fitnesstko.model.dto.nutrition.ProductDTO
import java.io.EOFException
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FragmentFood : Fragment() {

    private lateinit var authManager: AuthManager
    private lateinit var selectedDate: LocalDate
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private var currentWeekDates = mutableListOf<LocalDate>()
    private var currentTrackerId: Long? = null
    private lateinit var viewModel: KcalProductViewModel

    private data class SectionTotals(
        var fats: BigDecimal = BigDecimal.ZERO,
        var carbs: BigDecimal = BigDecimal.ZERO,
        var proteins: BigDecimal = BigDecimal.ZERO,
        var kcal: Int = 0,
        var rskPercent: Int = 0
    )

    private val sections = mutableMapOf(
        "BREAKFAST" to SectionTotals(),
        "LUNCH" to SectionTotals(),
        "DINNER" to SectionTotals(),
        "SNACK" to SectionTotals()
    )

    private var globalTotals = SectionTotals()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[KcalProductViewModel::class.java]

        authManager = AuthManager(requireContext())
        setupDates()
        setupRadioGroup()
        setupSections()
        loadKcalData()
    }

    private fun setupDates() {
        val prefs = PreferencesManager(requireContext())
        selectedDate = prefs.getSelectedDate() ?: run {
            val today = LocalDate.now()
            prefs.saveSelectedDate(today)
            today
        }
        currentWeekDates = (0..6).map { selectedDate.plusDays(it.toLong()) }.toMutableList()
    }

    private fun setupRadioGroup() {
        val preferences = PreferencesManager(requireContext())
        view?.findViewById<RadioGroup>(R.id.daysRadioGroup)?.apply {
            if (childCount != 7) return@apply

            currentWeekDates.forEachIndexed { index, date ->
                (getChildAt(index) as? RadioButton)?.let {
                    it.text = date.dayOfMonth.toString()
                    it.tag = date
                    if (date == selectedDate) it.isChecked = true
                } ?: run {
                    val rb = RadioButton(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            1f
                        )
                        text = date.dayOfMonth.toString()
                        tag = date
                        setTextColor(resources.getColor(R.color.white))
                    }
                    addView(rb)
                }
            }

            setOnCheckedChangeListener { _, checkedId ->
                (findViewById<RadioButton>(checkedId)?.tag as? LocalDate)?.let { date ->
                    selectedDate = date
                    preferences.saveSelectedDate(date)
                    updateUIForDate()
                    loadKcalData()
                }
            }
        }
    }

    private fun setupSections() {
        listOf(
            R.id.breakfastSection to Pair("BREAKFAST", "Завтрак"),
            R.id.lunchSection to Pair("LUNCH", "Обед"),
            R.id.dinnerSection to Pair("DINNER", "Ужин"),
            R.id.snackSection to Pair("SNACK", "Перекус")
        ).forEach { (id, data) ->
            view?.findViewById<View>(id)?.let { section ->
                val (mealType, title) = data
                section.findViewById<TextView>(R.id.sectionTitle).text = mealType
                section.findViewById<Button>(R.id.addButton).setOnClickListener {
                    if (!selectedDate.isEqual(LocalDate.now())) {
                        Toast.makeText(
                            requireContext(),
                            "Добавление возможно только для сегодняшней даты",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    }

                    val args = Bundle().apply { putString("typeMeal", mealType) }
                    val fragment = AddProductFragment().apply { arguments = args }
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.flFragment, fragment)
                        .addToBackStack(null)
                        .commit()
                }
                section.findViewById<LinearLayout>(R.id.productList).visibility = View.VISIBLE
                section.findViewById<View>(R.id.sectionTitle).setOnClickListener {
                    toggleSectionVisibility(section)
                }
            }
        }
    }

    private fun toggleSectionVisibility(section: View) {
        section.findViewById<LinearLayout>(R.id.productList).let { productList ->
            if (productList.isVisible) {
                productList.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_up))
                productList.visibility = View.GONE
            } else {
                productList.visibility = View.VISIBLE
                productList.startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_down))
            }
        }
    }

    private fun loadKcalData() {
        clearSections()
        val dateStr = selectedDate.format(dateFormatter)

        ApiService.kcalTrackerService.getKcalByDate(dateStr)
            .enqueue(object : Callback<KcalTrackerDTO> {
                override fun onResponse(
                    call: Call<KcalTrackerDTO>,
                    response: Response<KcalTrackerDTO>
                ) {
                    when {
                        response.isSuccessful && response.body() != null -> {
                            response.body()!!.let { tracker ->
                                tracker.id?.let {
                                    PreferencesManager(requireContext()).saveKcalTrackerId(
                                        selectedDate.format(dateFormatter),
                                        it
                                    )
                                }
                                currentTrackerId = tracker.id
                                loadProductsForTracker(tracker.id)
                            }
                        }

                        response.isSuccessful -> {
                            createKcalTracker()
                        }

                        else -> showError("Ошибка: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<KcalTrackerDTO>, t: Throwable) {
                    when (t) {
                        is EOFException -> createKcalTracker()
                        else -> showError("Ошибка сети: ${t.message}")
                    }
                    Log.e("FragmentFood", "Ошибка сети", t)
                }
            })
    }

    private fun createKcalTracker() {
        val userId = authManager.getUserId() ?: run {
            showError("Не авторизован")
            return
        }

        val trackerRequest = KcalTrackerDTO(
            id = null,
            userId = userId,
            date = selectedDate
        )

        val dateStr = selectedDate.format(dateFormatter)

        ApiService.kcalTrackerService.createKcalTracker(trackerRequest)
            .enqueue(object : Callback<KcalTrackerDTO> {
                override fun onResponse(
                    call: Call<KcalTrackerDTO>,
                    response: Response<KcalTrackerDTO>
                ) {
                    response.body()?.let { tracker ->
                        currentTrackerId = tracker.id
                        tracker.id?.let {
                            PreferencesManager(requireContext()).saveKcalTrackerId(
                                selectedDate.format(dateFormatter),
                                it
                            )
                        }
                        loadProductsForTracker(tracker.id)
                    } ?: showError("Пустой ответ сервера")
                }

                override fun onFailure(call: Call<KcalTrackerDTO>, t: Throwable) {
                    showError("Ошибка создания трекера")
                }
            })
    }

    private fun loadProductsForTracker(trackerId: Long?) {
        trackerId?.let { id ->
            ApiService.kcalTrackerService.getProductsByTracker(id)
                .enqueue(object : Callback<List<KcalProductDTO>> {
                    override fun onResponse(
                        call: Call<List<KcalProductDTO>>,
                        response: Response<List<KcalProductDTO>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let { products ->
                                products.groupBy { it.typeMeal }.forEach { (type, products) ->
                                    products.forEach { loadProductDetails(it) }
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<KcalProductDTO>>, t: Throwable) {
                        showError("Ошибка загрузки продуктов")
                    }
                })
        } ?: showError("ID трекера не найден")
    }

    private fun loadProductDetails(kcalProduct: KcalProductDTO) {
        ApiService.productService.getProductDetails(kcalProduct.productId)
            .enqueue(object : Callback<ProductDTO> {
                override fun onResponse(
                    call: Call<ProductDTO>,
                    response: Response<ProductDTO>
                ) {
                    response.body()?.let { product ->
                        displayProduct(product, kcalProduct)
                        updateTotals(kcalProduct.typeMeal, product, kcalProduct.count)
                    }
                }

                override fun onFailure(call: Call<ProductDTO>, t: Throwable) {
                    showError("Ошибка загрузки продукта")
                }
            })
    }

    private fun displayProduct(product: ProductDTO, kcalProduct: KcalProductDTO) {
        val sectionId = when (kcalProduct.typeMeal) {
            "BREAKFAST" -> R.id.breakfastSection
            "LUNCH" -> R.id.lunchSection
            "DINNER" -> R.id.dinnerSection
            "SNACK" -> R.id.snackSection
            else -> return
        }

        view?.findViewById<View>(sectionId)?.let { section ->
            val productView = LayoutInflater.from(context)
                .inflate(R.layout.item_product, null).apply {
                    findViewById<TextView>(R.id.tvProductName).text = product.name
                    val count = kcalProduct.count.toBigDecimal()

                    findViewById<TextView>(R.id.tvFats).text = formatValue(product.fats * count)
                    findViewById<TextView>(R.id.tvCarbs).text = formatValue(product.carbs * count)
                    findViewById<TextView>(R.id.tvProtein).text =
                        formatValue(product.proteins * count)
                    findViewById<TextView>(R.id.tvKcal).text =
                        (product.kcal * kcalProduct.count).toString()
                    findViewById<TextView>(R.id.tvRSK).text =
                        (product.percent?.times(kcalProduct.count)?.toString() ?: "0")
                }

            section.findViewById<LinearLayout>(R.id.productList).apply {
                addView(productView)
                visibility = View.VISIBLE
            }
        }
    }

    private fun updateTotals(mealType: String, product: ProductDTO, count: Int) {
        val section = sections[mealType] ?: return
        val multiplier = count.toBigDecimal()

        with(section) {
            fats += product.fats.multiply(multiplier)
            carbs += product.carbs.multiply(multiplier)
            proteins += product.proteins.multiply(multiplier)
            kcal += product.kcal * count
            rskPercent += (product.percent ?: 0) * count
        }

        with(globalTotals) {
            fats += product.fats.multiply(multiplier)
            carbs += product.carbs.multiply(multiplier)
            proteins += product.proteins.multiply(multiplier)
            kcal += product.kcal * count
            rskPercent += (product.percent ?: 0) * count
        }

        updateSectionUI(mealType, section)
        updateGlobalUI()
    }
    private fun updateSectionUI(mealType: String, totals: SectionTotals) {
        val sectionId = when (mealType) {
            "BREAKFAST" -> R.id.breakfastSection
            "LUNCH" -> R.id.lunchSection
            "DINNER" -> R.id.dinnerSection
            "SNACK" -> R.id.snackSection
            else -> return
        }

        view?.findViewById<View>(sectionId)?.let { section ->
            section.findViewById<TextView>(R.id.tvFatsValue).text = formatValue(totals.fats)
            section.findViewById<TextView>(R.id.tvCarbohydratesValue).text =
                formatValue(totals.carbs)
            section.findViewById<TextView>(R.id.tvProteinValue).text = formatValue(totals.proteins)
            section.findViewById<TextView>(R.id.tvKcalValue).text =
                totals.kcal.takeIf { it > 0 }?.toString() ?: "-"
            section.findViewById<TextView>(R.id.tvRSKValue)?.text =
                totals.rskPercent.takeIf { it > 0 }?.toString() ?: "-"
        }
    }

    private fun updateGlobalUI() {
        view?.let {
            it.findViewById<TextView>(R.id.tvFatsValue).text = formatValue(globalTotals.fats)
            it.findViewById<TextView>(R.id.tvCarbohydratesValue).text =
                formatValue(globalTotals.carbs)
            it.findViewById<TextView>(R.id.tvProteinValue).text = formatValue(globalTotals.proteins)
            it.findViewById<TextView>(R.id.tvKcalValue).text =
                globalTotals.kcal.takeIf { it > 0 }?.toString() ?: "-"
            view?.findViewById<TextView>(R.id.tvRSKValue)?.text =
                globalTotals.rskPercent.takeIf { it > 0 }?.toString() ?: "-"
        }
    }

    private fun formatValue(value: BigDecimal): String {
        return if (value > BigDecimal.ZERO) {
            value.setScale(1, RoundingMode.HALF_UP).toString()
        } else {
            "-"
        }
    }

    private fun clearSections() {
        sections.values.forEach {
            it.fats = BigDecimal.ZERO
            it.carbs = BigDecimal.ZERO
            it.proteins = BigDecimal.ZERO
            it.kcal = 0
            it.rskPercent = 0
        }
        globalTotals = SectionTotals()

        listOf(R.id.breakfastSection, R.id.lunchSection, R.id.dinnerSection, R.id.snackSection)
            .forEach { sectionId ->
                view?.findViewById<View>(sectionId)?.let {
                    it.findViewById<LinearLayout>(R.id.productList).removeAllViews()
                    arrayOf(
                        R.id.tvFatsValue,
                        R.id.tvCarbohydratesValue,
                        R.id.tvProteinValue,
                        R.id.tvKcalValue,
                        R.id.tvRSKValue
                    )
                        .forEach { tvId ->
                            it.findViewById<TextView>(tvId).text = "-"
                        }
                }
            }

        arrayOf(R.id.tvFatsValue, R.id.tvCarbohydratesValue, R.id.tvProteinValue, R.id.tvKcalValue,R.id.tvRSKValue)
            .forEach { tvId ->
                view?.findViewById<TextView>(tvId)?.text = "-"
            }
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun updateUIForDate() {
        val isToday = selectedDate.isEqual(LocalDate.now())

        listOf(R.id.breakfastSection, R.id.lunchSection, R.id.dinnerSection, R.id.snackSection)
            .forEach { sectionId ->
                view?.findViewById<View>(sectionId)?.findViewById<Button>(R.id.addButton)?.visibility =
                    if (isToday) View.VISIBLE else View.GONE
            }
    }

    override fun onResume() {
        super.onResume()
    }

}
/*
GET http://85.236.187.180:8080/api/kcal-tracker/by-date/2025-05-12
<-- 200 http://85.236.187.180:8080/api/kcal-tracker/by-date/2025-05-12 (76ms)

 */