package tk.ssau.fitnesstko

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.core.content.edit


/**
 * Менеджер для безопасного хранения и управления JWT-токенами аутентификации.
 * Использует зашифрованные SharedPreferences для защиты конфиденциальных данных.
 *
 * Основные функции:
 * - Сохранение токена в зашифрованном хранилище
 * - Получение токена из хранилища
 * - Удаление токена
 */
class AuthManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * Сохраняет JWT-токен в зашифрованном хранилище.
     * @param token Токен для сохранения (например, "eyJhbGci...")
     */
    fun saveToken(token: String) {
        sharedPreferences.edit { putString("jwt_token", token).apply() }
    }

    fun refreshToken(newToken: String) {
        saveToken(newToken)
    }

    /**
     * Получает сохранённый JWT-токен.
     * @return Токен или null, если не найден
     */
    fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    /**
     * Удаляет JWT-токен из хранилища.
     * Используется при выходе пользователя из системы.
     */
    fun clearToken() {
        sharedPreferences.edit { remove("jwt_token").apply() }
    }
}