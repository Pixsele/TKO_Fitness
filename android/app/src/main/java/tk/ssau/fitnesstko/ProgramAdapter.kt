package tk.ssau.fitnesstko

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tk.ssau.fitnesstko.databinding.ItemProgramBinding
import tk.ssau.fitnesstko.model.dto.TrainingsProgramForPageDTO

class ProgramAdapter(
    private var programs: List<TrainingsProgramForPageDTO>,
    private val onProgramClick: (TrainingsProgramForPageDTO) -> Unit,
    private val onLikeClick: (TrainingsProgramForPageDTO) -> Unit,
) : RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder>() {

    inner class ProgramViewHolder(private val binding: ItemProgramBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(program: TrainingsProgramForPageDTO) {
            binding.tvProgramName.text = program.name ?: "Без названия"
            binding.tvLikeCount.text = program.likeCount?.toString() ?: "0"
            binding.ivLike.setImageResource(
                if (program.liked) R.drawable.ic_liked
                else R.drawable.ic_not_liked
            )

            binding.ivLike.setOnClickListener {
                onLikeClick(program)
            }

            binding.root.setOnClickListener {
                onProgramClick(program)
            }
        }
    }

    fun updatePrograms(newPrograms: List<TrainingsProgramForPageDTO>) {
        programs = newPrograms
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramViewHolder {
        val binding = ItemProgramBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProgramViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProgramViewHolder, position: Int) {
        holder.bind(programs[position])
    }

    override fun getItemCount() = programs.size
}