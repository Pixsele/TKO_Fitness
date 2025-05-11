package tk.ssau.fitnesstko.model.dto.nutrition

data class KcalProductDTO(
    val id: Long?,
    val kcalTrackerId: Long,
    val productId: Long,
    val count: Int,
    val typeMeal: String
)
