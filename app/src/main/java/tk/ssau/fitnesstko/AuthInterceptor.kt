package tk.ssau.fitnesstko

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * OkHttp-интерцептор для добавления JWT-токена авторизации к HTTP-запросам.
 *
 * Основные функции:
 * - Добавляет заголовок `Authorization: Bearer <токен>` ко всем исходящим запросам.
 * - Логирует URL и заголовки запросов для отладки.
 *
 * Использование:
 * 1. Добавляется в цепочку OkHttp-клиента через `OkHttpClient.Builder().addInterceptor(AuthInterceptor())`.
 * 2. Автоматически применяется ко всем запросам через Retrofit.
 */
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val hardcodedToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzQ2ODAzMTMwLCJleHAiOjE3NDY4NzUxMzB9.Ehx-CWMOm4ElprezSU8BsQhjOOJIUNRJ31ndb-O_An4"

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $hardcodedToken")
            .build()

        Log.d("Network", "URL: ${request.url}")
        Log.d("Network", "Headers: ${request.headers}")


        return chain.proceed(request)
    }
}