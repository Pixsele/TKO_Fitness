package tk.ssau.fitnesstko

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val hardcodedToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzQ0NzAyNTc2LCJleHAiOjE3NDQ3MDYxNzZ9.HYFjIkBZTJ9IKi4001cRrGgyqwmdXy6sdhr8aA_VIHQ" //

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $hardcodedToken")
            .build()

        return chain.proceed(request)
    }
}