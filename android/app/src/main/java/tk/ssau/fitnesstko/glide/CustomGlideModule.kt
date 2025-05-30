package tk.ssau.fitnesstko.glide

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import tk.ssau.fitnesstko.ApiService
import java.io.InputStream

/**
 * Класс для интеграции Glide с OkHttp для выполнения сетевых запросов с авторизацией
 */
@GlideModule
class CustomGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {

        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(ApiService.client)
        )
    }

    override fun isManifestParsingEnabled(): Boolean = false
}