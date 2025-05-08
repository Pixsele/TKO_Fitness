package tk.ssau.fitnesstko.model.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciseForPageDto(
    val id: Long?,
    val name: String?,
    val likeCount: Int?,
    val liked: Boolean
) : Parcelable
