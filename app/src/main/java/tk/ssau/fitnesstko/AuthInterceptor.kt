package tk.ssau.fitnesstko

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val hardcodedToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNzQ2NzE1MDczLCJleHAiOjE3NDY3ODcwNzN9.VPHYYw-spa3soU0fRhB2hfBf9Qy1hqfI2ViGLSubly4"

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $hardcodedToken")
            .build()

        Log.d("Network", "URL: ${request.url}")
        Log.d("Network", "Headers: ${request.headers}")


        return chain.proceed(request)
    }
}