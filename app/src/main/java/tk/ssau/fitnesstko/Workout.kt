package tk.ssau.fitnesstko

data class Workout(
    val id: Long,
    val name: String,
    var likeCount: Int,
    var liked: Boolean
)