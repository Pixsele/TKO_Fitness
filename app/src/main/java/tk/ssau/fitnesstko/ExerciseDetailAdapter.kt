package tk.ssau.fitnesstko

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tk.ssau.fitnesstko.databinding.ItemExerciseDetailBinding
import tk.ssau.fitnesstko.model.dto.ExerciseDto

class ExerciseDetailAdapter(
    private var exercises: List<ExerciseDto>
) : RecyclerView.Adapter<ExerciseDetailAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(val binding: ItemExerciseDetailBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Обновление списка упражнений
    fun updateExercises(newExercises: List<ExerciseDto>) {
        exercises = newExercises
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding = ItemExerciseDetailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        with(holder.binding) {
            // Установка названия упражнения
            tvExerciseName.text = exercise.name

            // Загрузка изображения через Glide
            Glide.with(root.context)
                .load("${ApiService.BASE_URL}api/exercise/image/${exercise.id}")
                .placeholder(R.drawable.ic_placeholder) // Заглушка на время загрузки
                .error(R.drawable.cat) // Иконка при ошибке
                .into(ivExerciseImage)
        }
    }

    override fun getItemCount(): Int = exercises.size
}