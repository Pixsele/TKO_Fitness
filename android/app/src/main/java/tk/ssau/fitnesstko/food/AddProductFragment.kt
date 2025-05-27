package tk.ssau.fitnesstko.food

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.ApiService
import tk.ssau.fitnesstko.PreferencesManager
import tk.ssau.fitnesstko.R
import tk.ssau.fitnesstko.model.dto.nutrition.KcalProductDTO
import tk.ssau.fitnesstko.model.dto.nutrition.ProductForPageDTO
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class AddProductFragment : Fragment() {

    private lateinit var etSearch: EditText
    private lateinit var rvProducts: RecyclerView
    private lateinit var adapter: ProductAdapter
    private lateinit var viewModel: KcalProductViewModel
    private var trackerId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[KcalProductViewModel::class.java]

        view.findViewById<TextView>(R.id.tvDate).text = formatDate(LocalDate.now())

        etSearch = view.findViewById(R.id.etSearch)

        rvProducts = view.findViewById(R.id.rvProducts)
        rvProducts.layoutManager = LinearLayoutManager(context)
        adapter = ProductAdapter(emptyList())
        rvProducts.adapter = adapter

        view.findViewById<View>(R.id.btnSave).setOnClickListener {
            if (viewModel.products.value.isNullOrEmpty()) {
                Toast.makeText(context, "Нет продуктов для сохранения", Toast.LENGTH_SHORT).show()
            } else {
                sendProducts()
            }
        }

        etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }

        val prefs = PreferencesManager(requireContext())
        val selectedDate = prefs.getSelectedDate() ?: run {
            requireActivity().onBackPressed()
            return
        }

        if (!selectedDate.isEqual(LocalDate.now())) {
            Toast.makeText(context, "Добавление невозможно для выбранной даты", Toast.LENGTH_SHORT)
                .show()
            requireActivity().onBackPressed()
        }
        val date = prefs.getSelectedDate()?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: run {
            Toast.makeText(context, "Дата не выбрана", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
            return
        }
        trackerId = prefs.getKcalTrackerId(date) ?: run {
            Toast.makeText(context, "Трекер не найден", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
            return
        }
        view.findViewById<Button>(R.id.btnAddNew).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, CreateProductFragment())
                .addToBackStack("createProduct")
                .commit()
        }
    }

    private fun formatDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("d MMMM, EEE", Locale("ru"))
        return date.format(formatter)
    }

    private fun performSearch() {
        val query = etSearch.text.toString()
        if (query.isNotEmpty()) {
            ApiService.productService.searchProducts(query)
                .enqueue(object : Callback<List<ProductForPageDTO>> {
                    override fun onResponse(
                        call: Call<List<ProductForPageDTO>>,
                        response: Response<List<ProductForPageDTO>>
                    ) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                adapter.updateProducts(it)
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<ProductForPageDTO>>, t: Throwable) {
                        Toast.makeText(
                            requireContext(),
                            "Ошибка поиска: ${t.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    private inner class ProductAdapter(private var products: List<ProductForPageDTO>) :
        RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tvProductName)
            val tvGrams: TextView = view.findViewById(R.id.tvGrams)
            val tvRSK: TextView = view.findViewById(R.id.tvRSK)
            val rbSelect: RadioButton = view.findViewById(R.id.rbSelect)
        }

        fun updateProducts(newProducts: List<ProductForPageDTO>) {
            products = newProducts
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_product_simple, parent, false)
            )
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val product = products[position]
            val isSelected = viewModel.products.value?.any {
                it.productId == product.id
            } == true

            with(holder) {
                tvName.text = product.name
                tvGrams.text = "${product.grams.setScale(1, RoundingMode.HALF_UP)} гр"
                tvRSK.text = "РСК ${product.percentOfTarget}% - ${product.calories} ккал"
                rbSelect.isChecked = isSelected

                itemView.setOnClickListener {
                    if (isSelected) {
                        viewModel.products.value?.removeAll { it.productId == product.id }
                    } else {
                        if (viewModel.products.value?.any { it.productId == product.id } == true) {
                            Toast.makeText(context, "Продукт уже добавлен", Toast.LENGTH_SHORT)
                                .show()
                            return@setOnClickListener
                        }
                        if (product.id == null) {
                            Toast.makeText(context, "Ошибка: продукт не выбран", Toast.LENGTH_SHORT)
                                .show()
                            return@setOnClickListener
                        }

                        val args = Bundle().apply {
                            putLong("productId", product.id)
                            putString("typeMeal", arguments?.getString("typeMeal") ?: "BREAKFAST")
                        }

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.flFragment, AddProductDetailsFragment::class.java, args)
                            .addToBackStack("productDetails")
                            .commit()
                    }
                    notifyItemChanged(position)
                }
            }
        }

        override fun getItemCount() = products.size
    }

    private inner class SelectedProductAdapter(
        private var products: MutableList<KcalProductDTO>
    ) : RecyclerView.Adapter<SelectedProductAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tvProductName)
            val tvDetails: TextView = view.findViewById(R.id.tvGrams)
            val btnRemove: Button = view.findViewById(R.id.btnRemove)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_selected_product, parent, false)
            )
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val product = products[position]
            holder.tvName.text = "Продукт #${position + 1}"
            holder.tvDetails.text = "Порций: ${product.count}"
            holder.btnRemove.setOnClickListener {
                viewModel.products.value?.removeAt(position)
                notifyItemRemoved(position)
            }
        }

        override fun getItemCount() = products.size
    }

    private fun sendProducts() {
        viewModel.products.value?.let { products ->
            products.forEach { product ->
                ApiService.kcalTrackerService.createKcalProduct(product)
                    .enqueue(object : Callback<KcalProductDTO> {
                        override fun onResponse(
                            call: Call<KcalProductDTO>,
                            response: Response<KcalProductDTO>
                        ) {
                            if (response.isSuccessful) {
                                viewModel.products.value?.remove(product)
                            }
                        }

                        override fun onFailure(call: Call<KcalProductDTO>, t: Throwable) {
                            Toast.makeText(
                                requireContext(),
                                "Ошибка сохранения: ${product.productId}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
            requireActivity().onBackPressed()
        }
    }
}