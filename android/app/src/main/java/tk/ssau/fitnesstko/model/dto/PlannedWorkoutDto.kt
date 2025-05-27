package tk.ssau.fitnesstko.model.dto

data class PlannedWorkoutDto(
    val id: Long?,
    val userId: Long?,
    val workoutId: Long?,
    val date: String,
    val status: String?
)