package tk.ssau.fitnesstko

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.databinding.ItemWorkoutBinding
import tk.ssau.fitnesstko.model.dto.LikesWorkoutDto
import tk.ssau.fitnesstko.model.dto.WorkoutForPageDto

class WorkoutAdapter(
    private var workouts: List<WorkoutForPageDto>,
    private val onItemClick: (WorkoutForPageDto) -> Unit,
    private val onLikeClick: (WorkoutForPageDto) -> Unit
) : RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val binding = ItemWorkoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(workouts[position])
        //holder.itemView.setOnClickListener { onItemClick(workouts[position]) }
    }

    override fun getItemCount(): Int = workouts.size

    inner class WorkoutViewHolder(private val binding: ItemWorkoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workout: WorkoutForPageDto) {
            binding.tvWorkoutName.text = workout.name
            binding.tvLikeCount.text = workout.likeCount?.toString() ?: "0"
            binding.ivLike.setImageResource(
                if (workout.liked) R.drawable.ic_liked
                else R.drawable.ic_not_liked
            )

            binding.ivLike.setOnClickListener {
                val newLikedState = !workout.liked
                val newLikeCount = (workout.likeCount ?: 0) + if (newLikedState) 1 else -1

                // Создаем новую копию DTO
                val updatedWorkout = workout.copy(
                    liked = newLikedState,
                    likeCount = newLikeCount
                )

                // Обновляем UI и отправляем запрос
                updateWorkoutInList(updatedWorkout)
                sendLikeRequest(workout, updatedWorkout)
            }
        }

        private fun updateWorkoutInList(updatedWorkout: WorkoutForPageDto) {
            val position = workouts.indexOfFirst { it.id == updatedWorkout.id }
            if (position != -1) {
                workouts = workouts.toMutableList().apply {
                    set(position, updatedWorkout)
                }
                notifyItemChanged(position)
            }
        }

        private fun sendLikeRequest(original: WorkoutForPageDto, updated: WorkoutForPageDto) {
            val likeDto = LikesWorkoutDto(
                userId = 1L,
                workoutId = updated.id!!,
                id = null
            )

            val call = if (updated.liked) {
                ApiService.likesService.likeWorkout(likeDto)
            } else {
                ApiService.likesService.deleteLikeWorkout(likeDto)
            }

            call.enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        revertChanges(original)
                    }
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    revertChanges(original)
                }

                private fun revertChanges(originalWorkout: WorkoutForPageDto) {
                    val position = workouts.indexOfFirst { it.id == originalWorkout.id }
                    if (position != -1) {
                        workouts = workouts.toMutableList().apply {
                            set(position, originalWorkout)
                        }
                        notifyItemChanged(position)
                    }
                }
            })
        }
    }

}