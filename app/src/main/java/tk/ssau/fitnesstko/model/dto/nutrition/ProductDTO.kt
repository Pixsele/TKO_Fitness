package tk.ssau.fitnesstko.model.dto.nutrition

import org.threeten.bp.LocalDateTime
import java.math.BigDecimal

data class ProductDTO(
    val id: Long?,
    val name: String,
    val kcal: Int,
    val unit: String,
    val grams: BigDecimal,
    val portion: BigDecimal,
    val createdAt: String,
    val fats: BigDecimal,
    val carbs: BigDecimal,
    val proteins: BigDecimal
)
