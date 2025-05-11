package tk.ssau.fitnesstko.model.dto.nutrition

import java.math.BigDecimal

data class ProductForPageDTO(
    val id: Long?,
    val name: String,
    val calories: Int,
    val percentOfTarget: Int,
    val unit: String,
    val portions: BigDecimal
)