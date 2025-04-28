package tk.ssau.fitnesstko

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val hardcodedToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzQ1ODU4NzkzLCJleHAiOjE3NDU5MzA3OTN9.UpRlMzfqF64Yx8SVjcbq5PA1Zo15mw9CIxjYRI35NsY" //

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $hardcodedToken")
            .build()

        return chain.proceed(request)
    }
}