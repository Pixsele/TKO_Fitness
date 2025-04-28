package tk.ssau.fitnesstko

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import tk.ssau.fitnesstko.ApiService.WorkoutApi.LikesApi
import tk.ssau.fitnesstko.ApiService.WorkoutApi.WorkoutExerciseApi
import tk.ssau.fitnesstko.model.dto.ExerciseDto
import tk.ssau.fitnesstko.model.dto.ExerciseForPageDto
import tk.ssau.fitnesstko.model.dto.LikesExerciseDto
import tk.ssau.fitnesstko.model.dto.LikesWorkoutDto
import tk.ssau.fitnesstko.model.dto.WorkoutDto
import tk.ssau.fitnesstko.model.dto.WorkoutExerciseDto
import tk.ssau.fitnesstko.model.dto.WorkoutForPageDto

object ApiService {
    private const val BASE_URL = "http://85.236.187.180:8080/" // Замените на ваш URL
    private lateinit var authManager: AuthManager

    // Инициализация (вызовите в Application классе или MainActivity)
    fun initialize(context: Context) {
        authManager = AuthManager(context)
    }

    // Клиент с интерцепторами
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY // Логирование запросов
            })
            .build()
    }

    // Retrofit экземпляр
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Сервисы API
    val workoutService: WorkoutApi by lazy {
        retrofit.create(WorkoutApi::class.java)
    }

    val exerciseService: ExerciseApi by lazy {
        retrofit.create(ExerciseApi::class.java)
    }

    val workoutExerciseService: WorkoutExerciseApi by lazy { retrofit.create(WorkoutExerciseApi::class.java) }
    val likesService: LikesApi by lazy { retrofit.create(LikesApi::class.java) }

    interface ExerciseApi {
        @GET("api/exercise/page")
        fun getExercises(
            @Query("page") page: Int = 0,
            @Query("size") size: Int = 20
        ): Call<PagedResponse<ExerciseForPageDto>>

        @GET("api/exercise/{id}")
        fun getExerciseDetails(@Path("id") id: Long): Call<ExerciseDto>
    }


    // Пример интерфейса API
    interface WorkoutApi {
        @GET("api/workout/page")
        fun getWorkouts(
            @Query("page") page: Int = 0,
            @Query("size") size: Int = 10
        ): Call<PagedResponse<WorkoutForPageDto>>

        @POST("api/workout")
        fun createWorkout(@Body workoutDto: WorkoutDto): Call<WorkoutDto>

        /*
        @PUT("api/workout/{id}/like")  //Обработка лайка
        fun toggleLike(@Path("id") id: Long): Call<WorkoutForPageDto>
         */
        interface WorkoutExerciseApi {
            @POST("api/workout-exercise")
            fun createWorkoutExercise(@Body dto: WorkoutExerciseDto): Call<Unit>
        }

        interface LikesApi {
            @POST("api/like-workout")
            fun likeWorkout(@Body like: LikesWorkoutDto): Call<Unit>

            @HTTP(method = "DELETE", path = "api/like-workout", hasBody = true)
            fun deleteLikeWorkout(@Body like: LikesWorkoutDto): Call<Unit>

            @POST("api/likes/exercise")
            fun likeExercise(@Body like: LikesExerciseDto): Call<Unit>
        }

    }
}

data class PagedResponse<T>(
    val content: List<T>,
    val page: PageInfo,
)

data class PageInfo(
    val size: Int,
    val number: Int,
    val totalElements: Int,
    val totalPages: Int
)
//"size":20,"number":0,"totalElements":20,"totalPages":1