package tk.ssau.fitnesstko

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tk.ssau.fitnesstko.databinding.ItemExercisesBinding

class ExercisesAdapter(
    var exercises: List<Exercises>
) : RecyclerView.Adapter<ExercisesAdapter.ExercisesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExercisesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemExercisesBinding.inflate(inflater, parent, false)
        return ExercisesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ExercisesViewHolder,
        position: Int
    ) {
        holder.bind(exercises[position])
    }

    override fun getItemCount(): Int = exercises.size

    inner class ExercisesViewHolder(private val binding: ItemExercisesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exercise: Exercises) {
            binding.imageView.setImageResource(exercise.pic)
            binding.tvNameExercise.text = exercise.title
        }
    }
}