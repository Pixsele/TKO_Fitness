package tk.ssau.fitnesstko.food

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tk.ssau.fitnesstko.model.dto.nutrition.KcalProductDTO

class KcalProductViewModel : ViewModel() {
    val products = MutableLiveData<MutableList<KcalProductDTO>>(mutableListOf())

    fun addProduct(product: KcalProductDTO) {
        products.value?.add(product)
        products.postValue(products.value)
    }

    fun clearProducts() {
        products.value?.clear()
        products.postValue(products.value)
    }
}