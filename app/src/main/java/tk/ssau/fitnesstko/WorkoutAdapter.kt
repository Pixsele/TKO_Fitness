package tk.ssau.fitnesstko

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tk.ssau.fitnesstko.databinding.ItemWorkoutBinding
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
            binding.tvLikeCount.text = workout.likeCount.toString()
            binding.ivLike.setImageResource(
                if (workout.liked) R.drawable.ic_liked
                else R.drawable.ic_not_liked
            )

            // Клик на лайк
            binding.ivLike.setOnClickListener {
                onLikeClick(workout)
            }

            binding.tvWorkoutName.setOnClickListener {
                onItemClick(workout)
            }

        }
    }

}