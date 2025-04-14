package tk.ssau.fitnesstko

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val hardcodedToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzQ0NjU0NjYyLCJleHAiOjE3NDQ2NTgyNjJ9.yYK8fYOenkB1G3u_NdMhaMpyMkcqp5IGU1EMawyZNFE" //

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $hardcodedToken")
            .build()

        return chain.proceed(request)
    }
}