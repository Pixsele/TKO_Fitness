package tk.ssau.fitnesstko

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val hardcodedToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzQ0NjY0MjA3LCJleHAiOjE3NDQ2Njc4MDd9.mnCt6DCHKH3PojBEr4cdzswLB6XZtzp0phHnQE70fVo" //

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $hardcodedToken")
            .build()

        return chain.proceed(request)
    }
}