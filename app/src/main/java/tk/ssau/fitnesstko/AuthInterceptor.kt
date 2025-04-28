package tk.ssau.fitnesstko

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val hardcodedToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzQ1ODQ5OTYyLCJleHAiOjE3NDU4NTM1NjJ9.AqfzKedYck09uNuATA8nuEg7QHvshe33SFV_H-QSAMU" //

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $hardcodedToken")
            .build()

        return chain.proceed(request)
    }
}