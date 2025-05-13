package tk.ssau.fitnesstko

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tk.ssau.fitnesstko.databinding.CollectionBinding
import tk.ssau.fitnesstko.model.dto.LikesTrainingsProgramDto
import tk.ssau.fitnesstko.model.dto.TrainingsProgramForPageDTO
import tk.ssau.fitnesstko.model.dto.WorkoutForPageDto

class FragmentCollection(private val authManager: AuthManager) : Fragment() {

    private var _binding: CollectionBinding? = null
    private val binding get() = _binding!!
    private lateinit var workoutAdapter: WorkoutAdapter
    private var workouts = mutableListOf<WorkoutForPageDto>()
    private lateinit var programAdapter: ProgramAdapter
    private var programs = mutableListOf<TrainingsProgramForPageDTO>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclers()
        loadWorkouts()
        setupClickListeners()
    }

    private fun setupRecyclers() {
        val categories = listOf("Тренировки", "Программы")
        binding.rvCategories.adapter = CategoriesAdapter(categories) { selectedCategory ->
            when (selectedCategory) {
                "Тренировки" -> showWorkouts()
                "Программы" -> showPrograms()
            }
        }
        workoutAdapter = WorkoutAdapter(
            workouts = emptyList(),
            fragmentManager = parentFragmentManager,
            onDataUpdated = { updatedWorkout ->
                (activity as? MainActivity)?.prefs?.updateLocalWorkout(updatedWorkout)
            },
            authManager = authManager
        )
        programAdapter = ProgramAdapter(
            programs = emptyList(),
            onProgramClick = { program ->
                program.id?.let { openProgramDetails(it) }
            },
            onLikeClick = { program ->
                handleProgramLike(program)
            }
        )
    }
    private fun showPrograms() {
        binding.rvWorkouts.adapter = programAdapter
        loadPrograms()
    }
    private fun syncLikedPrograms(programs: List<TrainingsProgramForPageDTO>): List<TrainingsProgramForPageDTO> {
        val likedWorkoutIds = getLocalWorkouts().filter { it.liked }.mapNotNull { it.id }.toSet()

        return programs.map { program ->
            if (program.id in likedWorkoutIds) {
                program.copy(liked = true)
            } else {
                program
            }
        }
    }

    private fun showWorkouts() {
        binding.rvWorkouts.adapter = workoutAdapter
        loadWorkouts()
    }
    private fun loadPrograms() {
        ApiService.programService.getPrograms().enqueue(object : Callback<PagedResponse<TrainingsProgramForPageDTO>> {
            override fun onResponse(call: Call<PagedResponse<TrainingsProgramForPageDTO>>, response: Response<PagedResponse<TrainingsProgramForPageDTO>>) {
                response.body()?.let {
                    programs.clear()
                    programs.addAll(it.content)
                    programAdapter.updatePrograms(programs)
                }
            }

            override fun onFailure(call: Call<PagedResponse<TrainingsProgramForPageDTO>>, t: Throwable) {
                Toast.makeText(context, "Ошибка загрузки программ", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleProgramLike(program: TrainingsProgramForPageDTO) {
        val newLikedState = !program.liked
        val newLikeCount = (program.likeCount ?: 0) + if (newLikedState) 1 else -1

        val updatedProgram = program.copy(
            liked = newLikedState,
            likeCount = newLikeCount
        )

        val position = programs.indexOfFirst { it.id == updatedProgram.id }
        if (position != -1) {
            programs[position] = updatedProgram
            programAdapter.notifyItemChanged(position)
            sendLikeRequest(originalProgram = program, updatedProgram = updatedProgram)
        }
    }

    private fun sendLikeRequest(
        originalProgram: TrainingsProgramForPageDTO,
        updatedProgram: TrainingsProgramForPageDTO
    ) {
        val userId = authManager.getUserId() ?: return

        val likeDto = LikesTrainingsProgramDto(
            id = null,
            userId = userId,
            trainingsProgramId = updatedProgram.id!!
        )

        val call = if (updatedProgram.liked) {
            ApiService.likesService.likeProgram(likeDto)
        } else {
            ApiService.likesService.deleteLikeProgram(likeDto)
        }

        call.enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    programs.indexOfFirst { it.id == originalProgram.id }.takeIf { it != -1 }?.let { pos ->
                        programs[pos] = originalProgram
                        programAdapter.notifyItemChanged(pos)
                    }
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                programs.indexOfFirst { it.id == originalProgram.id }.takeIf { it != -1 }?.let { pos ->
                    programs[pos] = originalProgram
                    programAdapter.notifyItemChanged(pos)
                }
            }
        })
    }

    private fun openProgramDetails(programId: Long) {
        val fragment = FragmentProgramDetail().apply {
            arguments = Bundle().apply {
                putLong("programId", programId)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupClickListeners() {
        binding.ibAddExercises.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, CreateWorkoutFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    internal fun loadWorkouts() {

        ApiService.workoutService.getWorkouts()
            .enqueue(object : Callback<PagedResponse<WorkoutForPageDto>> {
                override fun onResponse(
                    call: Call<PagedResponse<WorkoutForPageDto>>,
                    response: Response<PagedResponse<WorkoutForPageDto>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { pagedResponse ->
                            updateWorkoutsList(
                                serverWorkouts = pagedResponse.content,
                                localWorkouts = getLocalWorkouts()
                            )
                        }
                    }
                }

                override fun onFailure(call: Call<PagedResponse<WorkoutForPageDto>>, t: Throwable) {
                    updateWorkoutsList(localWorkouts = getLocalWorkouts())
                }
            })
    }

    private fun updateWorkoutsList(
        serverWorkouts: List<WorkoutForPageDto> = emptyList(),
        localWorkouts: List<WorkoutForPageDto> = emptyList()
    ) {
        workouts.clear()
        workouts.addAll(serverWorkouts + localWorkouts)
        workoutAdapter.updateWorkouts(workouts)
    }

    private fun getLocalWorkouts(): List<WorkoutForPageDto> {
        return (activity as? MainActivity)?.prefs?.getLocalWorkouts() ?: emptyList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}