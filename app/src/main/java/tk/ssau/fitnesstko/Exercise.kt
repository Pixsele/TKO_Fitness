package tk.ssau.fitnesstko

data class Exercise(
    val id: Long,
    val name: String,
    var likeCount: Int,
    var liked: Boolean
)
