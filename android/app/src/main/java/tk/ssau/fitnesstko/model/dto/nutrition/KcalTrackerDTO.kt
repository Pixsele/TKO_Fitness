package tk.ssau.fitnesstko.model.dto.nutrition

import java.time.LocalDate

data class KcalTrackerDTO(
    val id: Long?,
    val userId: Long,
    val date: LocalDate
)
