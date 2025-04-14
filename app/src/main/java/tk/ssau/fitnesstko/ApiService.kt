package tk.ssau.fitnesstko

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

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

    // Пример интерфейса API
    interface WorkoutApi {
        @GET("api/workout/page")
        fun getWorkouts(
            @Query("page") page: Int = 0,
            @Query("size") size: Int = 10
        ): Call<PagedResponse<Workout>>
    }
}

data class PagedResponse<T>(
    val content: List<T>,
    val pageable: PageInfo,
    val totalPages: Int,
    val totalElements: Int
)

data class PageInfo(
    val pageNumber: Int,
    val pageSize: Int
)