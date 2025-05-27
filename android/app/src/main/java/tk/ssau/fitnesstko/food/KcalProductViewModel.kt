package tk.ssau.fitnesstko.food

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tk.ssau.fitnesstko.model.dto.nutrition.KcalProductDTO

class KcalProductViewModel : ViewModel() {
    private val _products = MutableLiveData<MutableList<KcalProductDTO>>(mutableListOf())
    val products: LiveData<MutableList<KcalProductDTO>> = _products

    fun addProduct(product: KcalProductDTO) {
        val currentList = _products.value ?: mutableListOf()
        if (currentList.none { it.productId == product.productId }) {
            currentList.add(product)
            _products.postValue(currentList)
            Log.d("ViewModel", "Добавлен продукт: ${product.productId}")
        }
    }
}