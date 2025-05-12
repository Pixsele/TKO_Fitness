package tk.ssau.fitnesstko.model.dto.user

data class UserDTO(
    val id:Long?,
    val name: String,
    val login: String,
    val birthDay: String,
    val weight: Double,
    val height: Double,
    val targetKcal: Int,
    val gender: Gender,
    val photoUrl: String?,
    val role: String,
    val currentTrainingProgramId: Long?,
    val currentNutritionProgramId: Long?,
    val createdAt: String?
)
